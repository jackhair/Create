package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

/**
 * Fluid amounts in the shim layer are Fabric droplets (81,000 per bucket), stored as {@code long}
 * (PORTING.md D3). Data keeps upstream's millibuckets: codecs read and write {@code "amount"} in mB, and
 * fall back to {@code "amount_droplets"} for amounts that aren't a whole number of mB, so nothing is lost.
 * <p>
 * Create code, not a NeoForge class.
 */
public final class FluidAmounts {
	/** Droplets per millibucket. */
	public static final long MB = FluidConstants.BUCKET / 1000;
	public static final long BUCKET = FluidConstants.BUCKET;

	private FluidAmounts() {}

	public static long fromMillibuckets(long millibuckets) {
		return millibuckets * MB;
	}

	/** Rounds down to whole millibuckets, for display. */
	public static long toMillibuckets(long droplets) {
		return droplets / MB;
	}

	/**
	 * A map codec for a droplet amount stored as {@code "amount"} (mB) or {@code "amount_droplets"}.
	 */
	public static MapCodec<Long> codec(boolean positive) {
		return Codec.mapEither(Codec.LONG.fieldOf("amount"), Codec.LONG.fieldOf("amount_droplets"))
			.xmap(either -> either.map(FluidAmounts::fromMillibuckets, droplets -> droplets),
				droplets -> droplets % MB == 0 ? Either.left(droplets / MB) : Either.right(droplets))
			.validate(amount -> !positive || amount > 0 ? DataResult.success(amount) : DataResult.error(() -> "Fluid amount must be positive: " + amount));
	}
}
