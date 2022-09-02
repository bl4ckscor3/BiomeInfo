package bl4ckscor3.mod.biomeinfo;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.ColorPicker;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "biomeinfo")
public class BiomeInfoConfig implements ConfigData
{
	@Comment("true if the biome info should be shown, false otherwise")
	boolean enabled = true;

	@Comment("true if the biome info should fade out shortly after a different biome has been entered. If this is set to false, the biome info will stay visible")
	boolean fadeOut = true;

	@Comment("true if the biome info should fade in when a different biome has been entered")
	boolean fadeIn = true;

	@Comment("How long in ticks (20 ticks = 1 second) to display the biome info, if fadeOut = true. If fadeIn = true, the time will be counted from the moment the biome info has finished fading in.")
	int displayTime = 30;

	@Comment("The X position to display the biome info at")
	int posX = 3;

	@Comment("The Y position to display the biome info at")
	int posY = 3;

	@Comment("The size of the biome info (multiplier)")
	double scale = 1.0D;

	@Comment("true if the biome info should be rendered with a shadow, false otherwise")
	boolean textShadow = true;

	@Comment("The color to display the biome info in (Format: 0xRRGGBB)")
	@ColorPicker
	int color = 0xFFFFFF;

	@Comment("If true, hides the mod's info text when the debug screen (F3) is open.")
	boolean hideOnDebugScreen = true;

	@Comment("The text alignment of the biome info.")
	TextAlignment textAlignment = TextAlignment.LEFT;
}