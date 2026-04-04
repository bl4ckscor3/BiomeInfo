package bl4ckscor3.mod.biomeinfo;

import java.util.Optional;

import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.moddiscovery.ModInfo;

public class NeoForgePlatformHelper implements PlatformHelper {
	@Override
	public Optional<String> getModName(String modid) {
		for (ModInfo info : FMLLoader.getCurrent().getLoadingModList().getMods()) {
			if (info.getModId().equals(modid))
				return Optional.of(info.getDisplayName());
		}

		return Optional.empty();
	}
}
