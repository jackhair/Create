package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import com.mojang.math.Transformation;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * The parts of NeoForge's {@code ITransformationExtension} Create uses, injected into {@link Transformation}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface ITransformationExtension {
	private Transformation self() {
		return (Transformation) this;
	}

	default boolean isIdentity() {
		return self().equals(Transformation.identity());
	}

	default void transformPosition(Vector4f position) {
		position.mul(self().getMatrix());
	}

	default void transformNormal(Vector3f normal) {
		normal.mul(new org.joml.Matrix3f(self().getMatrix()).invert().transpose()); // fabric: normal matrix computed here
		if (normal.lengthSquared() > 0)
			normal.normalize();
	}
}
