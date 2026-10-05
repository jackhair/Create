# Compile progress

`./gradlew compileJava` on `mc26.3/fabric/dev`, `-Xmaxerrs 10000`. Counts are lower bounds while imports are unresolved,
because javac reports attribution errors only once symbol resolution succeeds.

| Date | Commit | Errors | Files with errors | Note |
|---|---|---|---|---|
| 2026-10-05 | `dd4826ccf` | 3,021 | 591 | First Fabric compile after the Loom switch |
| 2026-10-05 | `7296d9f4d` | 2,407 | 450 | Shim batch 1: item handlers, datagen generators, utils (45 classes) |
| 2026-10-05 | `c4776f79c` | 1,776 | 411 | Bus, FML and registry shims; counts from scripts/fabric/javac_check.sh from here on |
| 2026-10-05 | `c36b1b895` | 1,103 | 328 | Fluids (droplets), ingredients, attachments shimmed |
