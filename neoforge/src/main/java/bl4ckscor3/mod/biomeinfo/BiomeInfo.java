package bl4ckscor3.mod.biomeinfo;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@Mod(CommonBiomeInfo.MODID)
@EventBusSubscriber
public class BiomeInfo {
	public BiomeInfo(ModContainer modContainer) {
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		modContainer.registerConfig(ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Pre event) {
		BiomeInfoRenderer.onClientTick();
	}

	@SubscribeEvent
	public static void onAddClientReloadListeners(AddClientReloadListenersEvent event) {
		CommonBiomeInfo.registerReloadListener(event::addListener);
	}

	@SubscribeEvent
	public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.TITLE, CommonBiomeInfo.OVERLAY_ID, BiomeInfoRenderer::renderBiomeInfo);
	}

	@SubscribeEvent
	public static void onConfigChange(ModConfigEvent event) {
		CommonBiomeInfo.onConfigChange(event.getConfig().getSpec());
	}
}
