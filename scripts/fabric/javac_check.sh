#!/bin/sh
# Compile Create with javac directly and summarize errors (see writeCompileClasspath in build.gradle).
# Usage: scripts/fabric/javac_check.sh [--refresh]   (--refresh re-exports the classpath from Gradle)
# Full diagnostics: build/javac-check/errors.txt; one line per error in errors-only.txt.
set -e
cd "$(dirname "$0")/../.."
OUT=build/javac-check
JDK21=${JDK21:-$(/usr/libexec/java_home -v 21)}
JDK25=${JDK25:-$(/usr/libexec/java_home -v 25)}

if [ "$1" = "--refresh" ] || [ ! -f "$OUT/classpath.txt" ]; then
	JAVA_HOME="$JDK25" ./gradlew writeCompileClasspath --console=plain -q
fi

rm -rf "$OUT/classes" && mkdir -p "$OUT/classes"
find $(cat "$OUT/sourcedirs.txt") -name '*.java' > "$OUT/sources.txt"
set +e
"$JDK21/bin/javac" -d "$OUT/classes" -encoding UTF-8 --release 21 -implicit:none -nowarn \
	-Xmaxerrs 100000 -Xlint:none -Xdiags:compact \
	-cp "$(cat "$OUT/classpath.txt")" -processorpath "$(cat "$OUT/processorpath.txt")" \
	@"$OUT/sources.txt" > "$OUT/errors.txt" 2>&1
STATUS=$?
set -e

grep -E '^/.*\.java:[0-9]+: error: ' "$OUT/errors.txt" | sed "s#^$PWD/##" > "$OUT/errors-only.txt" || true
ERRORS=$(wc -l < "$OUT/errors-only.txt" | tr -d ' ')
FILES=$(cut -d: -f1 "$OUT/errors-only.txt" | sort -u | wc -l | tr -d ' ')
echo "javac exit $STATUS: $ERRORS errors in $FILES files"
echo "By source root:"
cut -d: -f1 "$OUT/errors-only.txt" | sort -u | sed -E 's#^(registrate|neoforge-shim|src)/.*#\1#; s#^build/.*#generated#' | sort | uniq -c
echo "Top error kinds:"
sed -E 's/.*: error: //; s/ [a-zA-Z_$.]+\(.*//; s/(symbol|package|class|method) .*/\1/' "$OUT/errors-only.txt" | sort | uniq -c | sort -rn | head -8
