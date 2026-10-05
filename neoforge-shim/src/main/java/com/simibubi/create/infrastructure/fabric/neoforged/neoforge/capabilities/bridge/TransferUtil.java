package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.bridge;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

/**
 * Transaction helpers for the NeoForge-to-Fabric transfer bridge. Create code (PORTING.md D7).
 */
public final class TransferUtil {
	private TransferUtil() {}

	/**
	 * Opens a transaction for a NeoForge-style call: nested in the current transaction if one is open on this
	 * thread (so an outer abort still rolls it back), otherwise an outer transaction.
	 */
	public static Transaction open() {
		TransactionContext current = Transaction.getCurrentUnsafe();
		return current != null ? current.openNested() : Transaction.openOuter();
	}
}
