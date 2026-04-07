package bl4ckscor3.mod.biomeinfo;

import java.util.Optional;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.server.packs.PackType;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class FabricEntrypoint implements ClientModInitializer, Platform {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.START_CLIENT_TICK.register(_ -> BiomeInfoRenderer.onClientTick());
		HudElementRegistry.attachElementAfter(VanillaHudElements.TITLE_AND_SUBTITLE, BiomeInfo.OVERLAY_ID, BiomeInfoRenderer::renderBiomeInfo);
		BiomeInfo.registerReloadListener(ResourceLoader.get(PackType.CLIENT_RESOURCES)::registerReloadListener);
		ModConfigEvents.loading(BiomeInfo.MODID).register(config -> BiomeInfo.onConfigChange(config.getSpec()));
		ModConfigEvents.reloading(BiomeInfo.MODID).register(config -> BiomeInfo.onConfigChange(config.getSpec()));
		ModConfigEvents.unloading(BiomeInfo.MODID).register(config -> BiomeInfo.onConfigChange(config.getSpec()));
		ConfigScreenFactoryRegistry.INSTANCE.register(BiomeInfo.MODID, ConfigurationScreen::new);
		ConfigRegistry.INSTANCE.register(BiomeInfo.MODID, ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
		BiomeInfo.initialize(this);
	}

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