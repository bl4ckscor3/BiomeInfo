package bl4ckscor3.mod.biomeinfo;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;

public enum TextAlignment {
	LEFT((font, component) -> 0),
	MIDDLE((font, component) -> font.width(component) / 2),
	RIGHT((font, component) -> font.width(component));

	private final BiFunction<Font,Component,Integer> negativeOffset;

	TextAlignment(BiFunction<Font,Component,Integer> offset) {
		negativeOffset = offset;
	}

	public int getNegativeOffset(Font font, Component biomeName) {
		return negativeOffset.apply(font, biomeName);
	}
}