package bl4ckscor3.mod.biomeinfo;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.EnumValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

public class Configuration {
	public static final ModConfigSpec CONFIG_SPEC;
	private static final Configuration CONFIG;
	public final BooleanValue enabled;
	public final BooleanValue fadeOut;
	public final BooleanValue fadeIn;
	public final IntValue displayTime;
	public final IntValue posX;
	public final IntValue posY;
	public final DoubleValue scale;
	public final BooleanValue textShadow;
	public final IntValue color;
	public final BooleanValue hideOnDebugScreen;
	public final EnumValue<TextAlignment> textAlignment;

	static {
		Pair<Configuration, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Configuration::new);

		CONFIG_SPEC = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	Configuration(ModConfigSpec.Builder builder) {
		//@formatter:off
		enabled = builder
				.comment("true if the biome info should be shown, false otherwise")
				.define("enabled", true);
		fadeOut = builder
				.comment("true if the biome info should fade out shortly after a different biome has been entered. If this is set to false, the biome info will stay visible")
				.define("fadeOut", true);
		fadeIn = builder
				.comment("true if the biome info should fade in when a different biome has been entered")
				.define("fadeIn", true);
		displayTime = builder
				.comment("How long in ticks (20 ticks = 1 second) to display the biome info, if fadeOut = true. If fadeIn = true, the time will be counted from the moment the biome info has finished fading in.")
				.defineInRange("displayTime", 30, 0, Integer.MAX_VALUE);
		posX = builder
				.comment("The X position to display the biome info at")
				.defineInRange("posX", 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
		posY = builder
				.comment("The Y position to display the biome info at")
				.defineInRange("posY", 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
		scale = builder
				.comment("The size of the biome info (multiplier)")
				.defineInRange("scale", 1.0D, 0.0D, Double.MAX_VALUE);
		textShadow = builder
				.comment("true if the biome info should be rendered with a shadow, false otherwise")
				.define("textShadow", true);
		color = builder
				.comment("The color to display the biome info in (Format: 0xRRGGBB)")
				.defineInRange("color", 0xffffff, 0x000000, 0xffffff);
		hideOnDebugScreen = builder
				.comment("If true, hides the mod's info text when the debug screen (F3) is open.")
				.define("hideOnDebugScreen", true);
		textAlignment = builder
				.comment("The text alignment of the biome info.")
				.defineEnum("textAlignment", TextAlignment.LEFT);
		//@formatter:on
	}

	public static boolean enabled() {
		return CONFIG.enabled.get();
	}

	public static boolean fadeOut() {
		return CONFIG.fadeOut.get();
	}

	public static boolean fadeIn() {
		return CONFIG.fadeIn.get();
	}

	public static int displayTime() {
		return CONFIG.displayTime.get();
	}

	public static int posX() {
		return CONFIG.posX.get();
	}

	public static int posY() {
		return CONFIG.posY.get();
	}

	public static double scale() {
		return CONFIG.scale.get();
	}

	public static boolean textShadow() {
		return CONFIG.textShadow.get();
	}

	public static int color() {
		return CONFIG.color.get();
	}

	public static boolean hideOnDebugScreen() {
		return CONFIG.hideOnDebugScreen.get();
	}

	public static TextAlignment textAlignment() {
		return CONFIG.textAlignment.get();
	}
}
