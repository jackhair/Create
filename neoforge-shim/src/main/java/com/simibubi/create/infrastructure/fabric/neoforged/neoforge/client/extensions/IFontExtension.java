package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

import net.minecraft.client.gui.Font;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

/**
 * The parts of NeoForge's {@code IFontExtension} Create uses, injected into {@link Font}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IFontExtension {
	/** Trims text to the width, adding an ellipsis if it was cut. */
	default FormattedCharSequence ellipsize(FormattedText text, int maxWidth) {
		Font self = (Font) this;
		FormattedText ellipsis = CommonComponents.ELLIPSIS;
		if (self.width(text) <= maxWidth)
			return Language.getInstance().getVisualOrder(text);
		FormattedText trimmed = self.substrByWidth(text, maxWidth - self.width(ellipsis));
		return Language.getInstance().getVisualOrder(FormattedText.composite(trimmed, ellipsis));
	}
}
