package bl4ckscor3.mod.biomeinfo;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid=BiomeInfo.MODID)
public class Configuration
{
	@Config.Name("Enable")
	@Config.Comment("true if the biome info should be shown, false otherwise")
	public static boolean enabled = true;
	@Config.Name("Position X")
	@Config.Comment("The X position to display the biome info at")
	public static int posX = 3;
	@Config.Name("Position Y")
	@Config.Comment("The Y position to display the biome info at")
	public static int posY = 3;
	@Config.Name("Color")
	@Config.Comment("The color to display the biome info in (hexadecimal)")
	public static String sColor = "ffffff";
	@Config.Ignore
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
