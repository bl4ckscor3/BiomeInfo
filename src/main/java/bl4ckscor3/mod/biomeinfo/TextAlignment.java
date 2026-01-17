package bl4ckscor3.mod.biomeinfo;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public enum TextAlignment {
	LEFT((font, component, scale) -> 0),
	MIDDLE((font, component, scale) -> (int) (font.width(component) * scale / 2.0F)),
	RIGHT((font, component, scale) -> (int) (font.width(component) * scale));

	private final NegativeOffset negativeOffset;

	TextAlignment(NegativeOffset offset) {
		this.negativeOffset = offset;
	}

	public int getNegativeOffset(Font font, Component biomeName, float scale) {
		return negativeOffset.get(font, biomeName, scale);
	}

	@FunctionalInterface
	public interface NegativeOffset {
		int get(Font font, Component biomeName, float scale);
	}
}