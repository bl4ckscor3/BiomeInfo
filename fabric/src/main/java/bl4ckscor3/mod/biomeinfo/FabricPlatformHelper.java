package bl4ckscor3.mod.biomeinfo;

import java.util.Optional;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class FabricPlatformHelper implements PlatformHelper {
	@Override
	public Optional<String> getModName(String modid) {
		return FabricLoader.getInstance().getAllMods()
			.stream()
			.map(ModContainer::getMetadata)
			.filter(meta -> meta.getId().equals(modid))
			.findFirst()
			.map(ModMetadata::getName);
	}
}
