package bl4ckscor3.mod.biomeinfo;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid=BiomeInfo.MODID)
public class Configuration
{
	@Name("Enable")
	@Comment("true if the biome info should be shown, false otherwise")
	public static boolean enabled = true;
	@Name("Fade Out")
	@Comment("true if the biome info should only show shortly when the biome is switched")
	public static boolean fadeOut = true;
	@Name("Display Time")
	@Comment("How long in ticks (20 ticks = 1 second) to display the biome info, if fadeOut = true")
	@RangeInt(min = 0, max = Integer.MAX_VALUE)
	public static int displayTime = 30;
	@Name("Position X")
	@Comment("The X position to display the biome info at")
	public static int posX = 3;
	@Name("Position Y")
	@Comment("The Y position to display the biome info at")
	public static int posY = 3;
	@Name("Scale")
	@Comment("The size of the biome info (multiplier)")
	public static double scale = 1.0D;
	@Name("Shadow")
	@Comment("true if the biome info should be rendered with a shadow, false otherwise")
	public static boolean textShadow = true;
	@Name("Color")
	@Comment("The color to display the biome info in (hexadecimal)")
	public static String sColor = "ffffff";
	@Ignore
	public static int iColor = Integer.parseInt(sColor, 16);

	@EventBusSubscriber
	public static class EventHandler
	{
		@SubscribeEvent
		public static void onOnConfigChangedEvent(final OnConfigChangedEvent event)
		{
			if(event.getModID().equals(BiomeInfo.MODID))
			{
				ConfigManager.sync(BiomeInfo.MODID, Config.Type.INSTANCE);
				iColor = Integer.parseInt(sColor, 16);
			}
		}
	}
}
