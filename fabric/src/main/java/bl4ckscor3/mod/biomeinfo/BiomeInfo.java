package bl4ckscor3.mod.biomeinfo;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class BiomeInfo implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.START_CLIENT_TICK.register(_ -> BiomeInfoRenderer.onClientTick());
		HudElementRegistry.attachElementAfter(VanillaHudElements.TITLE_AND_SUBTITLE, CommonBiomeInfo.OVERLAY_ID, BiomeInfoRenderer::renderBiomeInfo);
		CommonBiomeInfo.registerReloadListener(ResourceLoader.get(PackType.CLIENT_RESOURCES)::registerReloadListener);
		ModConfigEvents.loading(CommonBiomeInfo.MODID).register(config -> CommonBiomeInfo.onConfigChange(config.getSpec()));
		ModConfigEvents.reloading(CommonBiomeInfo.MODID).register(config -> CommonBiomeInfo.onConfigChange(config.getSpec()));
		ModConfigEvents.unloading(CommonBiomeInfo.MODID).register(config -> CommonBiomeInfo.onConfigChange(config.getSpec()));
		ConfigScreenFactoryRegistry.INSTANCE.register(CommonBiomeInfo.MODID, ConfigurationScreen::new);
		ConfigRegistry.INSTANCE.register(CommonBiomeInfo.MODID, ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
	}
}