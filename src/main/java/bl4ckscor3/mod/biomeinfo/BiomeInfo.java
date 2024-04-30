package bl4ckscor3.mod.biomeinfo;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(BiomeInfo.MODID)
public class BiomeInfo {
	public static final String MODID = "biomeinfo";

	public BiomeInfo(ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
	}
}
