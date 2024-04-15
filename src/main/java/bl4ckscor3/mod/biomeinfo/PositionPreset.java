package bl4ckscor3.mod.biomeinfo;

import static bl4ckscor3.mod.biomeinfo.BiomeInfoRenderer.MARGIN;

import java.util.function.Supplier;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.Font;

public enum PositionPreset {
	NONE(window -> Configuration.posX(), (window, font) -> Configuration.posY(), Configuration::textAlignment),
	TOP_LEFT(window -> MARGIN, (window, font) -> MARGIN, () -> TextAlignment.LEFT),
	TOP_MIDDLE(window -> window.getGuiScaledWidth() / 2, (window, font) -> MARGIN, () -> TextAlignment.MIDDLE),
	TOP_RIGHT(window -> window.getGuiScaledWidth() - MARGIN, (window, font) -> MARGIN, () -> TextAlignment.RIGHT),
	MIDDLE_LEFT(window -> MARGIN, (window, font) -> window.getGuiScaledHeight() / 2 - font.lineHeight / 2, () -> TextAlignment.LEFT),
	MIDDLE(window -> window.getGuiScaledWidth() / 2, (window, font) -> window.getGuiScaledHeight() / 2 - font.lineHeight / 2, () -> TextAlignment.MIDDLE),
	MIDDLE_RIGHT(window -> window.getGuiScaledWidth() - MARGIN, (window, font) -> window.getGuiScaledHeight() / 2 - font.lineHeight / 2, () -> TextAlignment.RIGHT),
	BOTTOM_LEFT(window -> MARGIN, (window, font) -> window.getGuiScaledHeight() - MARGIN - font.lineHeight, () -> TextAlignment.LEFT),
	BOTTOM_MIDDLE(window -> window.getGuiScaledWidth() / 2, (window, font) -> window.getGuiScaledHeight() - MARGIN - font.lineHeight, () -> TextAlignment.MIDDLE),
	BOTTOM_RIGHT(window -> window.getGuiScaledWidth() - MARGIN, (window, font) -> window.getGuiScaledHeight() - MARGIN - font.lineHeight, () -> TextAlignment.RIGHT),
	ABOVE_MIDDLE(window -> window.getGuiScaledWidth() / 2, (window, font) -> window.getGuiScaledHeight() / 4, () -> TextAlignment.MIDDLE),
	ABOVE_HOTBAR(window -> window.getGuiScaledWidth() / 2, (window, font) -> window.getGuiScaledHeight() - 68, () -> TextAlignment.MIDDLE),
	LEFT_OF_CROSSHAIR(window -> window.getGuiScaledWidth() / 2 - MARGIN - 3, (window, font) -> window.getGuiScaledHeight() / 2 - font.lineHeight / 2, () -> TextAlignment.RIGHT),
	RIGHT_OF_CROSSHAIR(window -> window.getGuiScaledWidth() / 2 + MARGIN + 3, (window, font) -> window.getGuiScaledHeight() / 2 - font.lineHeight / 2, () -> TextAlignment.LEFT),
	ABOVE_CROSSHAIR(window -> window.getGuiScaledWidth() / 2, (window, font) -> window.getGuiScaledHeight() / 2 - MARGIN - 3 - font.lineHeight, () -> TextAlignment.MIDDLE),
	UNDER_CROSSHAIR_WITH_ATTACK_INDICATOR(window -> window.getGuiScaledWidth() / 2, (window, font) -> window.getGuiScaledHeight() / 2 + MARGIN + 4 + font.lineHeight, () -> TextAlignment.MIDDLE),
	UNDER_CROSSHAIR(window -> window.getGuiScaledWidth() / 2, (window, font) -> window.getGuiScaledHeight() / 2 + MARGIN + 3, () -> TextAlignment.MIDDLE);

	private ToIntFunction<Window> xGetter;
	private ToIntBiFunction<Window, Font> yGetter;
	private Supplier<TextAlignment> textAlignmentGetter;

	PositionPreset(ToIntFunction<Window> xGetter, ToIntBiFunction<Window, Font> yGetter, Supplier<TextAlignment> textAlignmentGetter) {
		this.xGetter = xGetter;
		this.yGetter = yGetter;
		this.textAlignmentGetter = textAlignmentGetter;
	}

	public int posX(Window window) {
		return xGetter.applyAsInt(window);
	}

	public int posY(Window window, Font font) {
		return yGetter.applyAsInt(window, font);
	}

	public TextAlignment textAlignment() {
		return textAlignmentGetter.get();
	}
}
