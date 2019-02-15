package bl4ckscor3.mod.biomeinfo;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class Configuration
{
	public static final ForgeConfigSpec CONFIG_SPEC;
	private static final Configuration CONFIG;

	public final BooleanValue enabled;
	public final BooleanValue fadeOut;
	public final IntValue displayTime;
	public final IntValue posX;
	public final IntValue posY;
	public final DoubleValue scale;
	public final BooleanValue textShadow;
	public final IntValue color;

	static
	{
		Pair<Configuration,ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Configuration::new);

		CONFIG_SPEC = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	Configuration(ForgeConfigSpec.Builder builder)
	{
		enabled = builder
				.comment("true if the biome info should be shown, false otherwise")
				.define("enabled", true);
		fadeOut = builder
				.comment("true if the biome info should only show shortly when the biome is switched")
				.define("fadeOut", true);
		displayTime = builder
				.comment("How long in ticks (20 ticks = 1 second) to display the biome info, if fadeOut = true")
				.defineInRange("displayTime", 30, 0, Integer.MAX_VALUE);
		posX = builder
				.comment("The X position to display the biome info at")
				.defineInRange("posX", 3, 0, Integer.MAX_VALUE);
		posY = builder
				.comment("The Y position to display the biome info at")
				.defineInRange("posY", 3, 0, Integer.MAX_VALUE);
		scale = builder
				.comment("The size of the biome info (multiplier)")
				.defineInRange("scale", 1.0D, 0.0D, Double.MAX_VALUE);
		textShadow = builder
				.comment("true if the biome info should be rendered with a shadow, false otherwise")
				.define("textShadow", true);
		color = builder
				.comment("The color to display the biome info in (Format: 0xRRGGBB)")
				.defineInRange("color", 0xffffff, 0x000000, 0xffffff);
	}

	public static boolean enabled()
	{
		if(CONFIG_SPEC == null)
			return true;

		return CONFIG.enabled.get();
	}

	public static boolean fadeOut()
	{
		if(CONFIG_SPEC == null)
			return false;

		return CONFIG.fadeOut.get();
	}

	public static int displayTime()
	{
		if(CONFIG_SPEC == null)
			return 30;

		return CONFIG.displayTime.get();
	}

	public static int posX()
	{
		if(CONFIG_SPEC == null)
			return 3;

		return CONFIG.posX.get();
	}

	public static int posY()
	{
		if(CONFIG_SPEC == null)
			return 3;

		return CONFIG.posY.get();
	}

	public static double scale ()
	{
		if(CONFIG_SPEC == null)
			return 1.0D;

		return CONFIG.scale.get();
	}

	public static boolean textShadow()
	{
		if(CONFIG_SPEC == null)
			return true;

		return CONFIG.textShadow.get();
	}

	public static int color()
	{
		if(CONFIG_SPEC == null)
			return 0xffffff;

		return CONFIG.color.get();
	}
}
