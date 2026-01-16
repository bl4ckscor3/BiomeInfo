package bl4ckscor3.mod.biomeinfo;

import java.util.function.Supplier;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.Font;

import static bl4ckscor3.mod.biomeinfo.BiomeInfoRenderer.MARGIN;

public enum PositionPreset {
	NONE(
		window -> Configuration.posX(),
		(window, lineHeight) -> Configuration.posY(),
		Configuration::textAlignment
	),
	TOP_LEFT(
		window -> MARGIN,
		(window, lineHeight) -> MARGIN,
		() -> TextAlignment.LEFT
	),
	TOP_MIDDLE(
		window -> window.getGuiScaledWidth() / 2,
		(window, lineHeight) -> MARGIN,
		() -> TextAlignment.MIDDLE
	),
	TOP_RIGHT(
		window -> window.getGuiScaledWidth() - MARGIN,
		(window, lineHeight) -> MARGIN,
		() -> TextAlignment.RIGHT
	),
	MIDDLE_LEFT(
		window -> MARGIN,
		(window, lineHeight) -> (int) (window.getGuiScaledHeight() / 2.0F - lineHeight / 2),
		() -> TextAlignment.LEFT
	),
	MIDDLE(
		window -> window.getGuiScaledWidth() / 2,
		(window, lineHeight) -> (int) (window.getGuiScaledHeight() / 2.0F - lineHeight / 2),
		() -> TextAlignment.MIDDLE
	),
	MIDDLE_RIGHT(
		window -> window.getGuiScaledWidth() - MARGIN,
		(window, lineHeight) -> (int) (window.getGuiScaledHeight() / 2.0F - lineHeight / 2),
		() -> TextAlignment.RIGHT
	),
	BOTTOM_LEFT(
		window -> MARGIN,
		(window, lineHeight) -> (int) (window.getGuiScaledHeight() - MARGIN - lineHeight),
		() -> TextAlignment.LEFT
	),
	BOTTOM_MIDDLE(
		window -> window.getGuiScaledWidth() / 2,
		(window, lineHeight) -> (int) (window.getGuiScaledHeight() - MARGIN - lineHeight),
		() -> TextAlignment.MIDDLE
	),
	BOTTOM_RIGHT(
		window -> {return window.getGuiScaledWidth() - MARGIN;},
		(window, lineHeight) -> (int) (window.getGuiScaledHeight() - MARGIN - lineHeight),
		() -> TextAlignment.RIGHT
	),
	ABOVE_MIDDLE(
		window -> window.getGuiScaledWidth() / 2,
		(window, lineHeight) -> window.getGuiScaledHeight() / 4,
		() -> TextAlignment.MIDDLE
	),
	ABOVE_HOTBAR(
		window -> window.getGuiScaledWidth() / 2,
		(window, lineHeight) -> window.getGuiScaledHeight() - 68,
		() -> TextAlignment.MIDDLE
	),
	LEFT_OF_CROSSHAIR(
		window -> window.getGuiScaledWidth() / 2 - MARGIN - 3,
		(window, lineHeight) -> (int) (window.getGuiScaledHeight() / 2.0F - lineHeight / 2),
		() -> TextAlignment.RIGHT
	),
	RIGHT_OF_CROSSHAIR(
		window -> window.getGuiScaledWidth() / 2 + MARGIN + 3,
		(window, lineHeight) -> (int) (window.getGuiScaledHeight() / 2.0F - lineHeight / 2),
		() -> TextAlignment.LEFT
	),
	ABOVE_CROSSHAIR(
		window -> window.getGuiScaledWidth() / 2,
		(window, lineHeight) -> (int) (window.getGuiScaledHeight() / 2.0F - MARGIN - 3 - lineHeight),
		() -> TextAlignment.MIDDLE
	),
	UNDER_CROSSHAIR_WITH_ATTACK_INDICATOR(
		window -> window.getGuiScaledWidth() / 2,
		(window, lineHeight) -> (int) (window.getGuiScaledHeight() / 2.0F + MARGIN + 4 + lineHeight),
		() -> TextAlignment.MIDDLE
	),
	UNDER_CROSSHAIR(
		window -> window.getGuiScaledWidth() / 2,
		(window, lineHeight) -> window.getGuiScaledHeight() / 2 + MARGIN + 3,
		() -> TextAlignment.MIDDLE
	);

	private final XPosition xPosition;
	private final YPosition yPosition;
	private final Supplier<TextAlignment> textAlignmentGetter;

	PositionPreset(XPosition xPosition, YPosition yPosition, Supplier<TextAlignment> textAlignmentGetter) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.textAlignmentGetter = textAlignmentGetter;
	}

	public int posX(Window window) {
		return xPosition.get(window);
	}

	public int posY(Window window, Font font, float scale) {
		return yPosition.get(window, font.lineHeight * scale);
	}

	public TextAlignment textAlignment() {
		return textAlignmentGetter.get();
	}

	@FunctionalInterface
	public interface XPosition {
		int get(Window window);
	}

	@FunctionalInterface
	public interface YPosition {
		int get(Window window, float lineHeight);
	}
}
