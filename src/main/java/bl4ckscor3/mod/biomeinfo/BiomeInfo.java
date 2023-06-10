package bl4ckscor3.mod.biomeinfo;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(BiomeInfo.MODID)
public class BiomeInfo {
	public static final String MODID = "biomeinfo";

	public BiomeInfo() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
	}
}
