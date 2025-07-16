package bl4ckscor3.mod.biomeinfo;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(BiomeInfo.MODID)
public class BiomeInfo {
	public static final String MODID = "biomeinfo";

	public BiomeInfo(ModContainer modContainer) {
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		modContainer.registerConfig(ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
	}
}
