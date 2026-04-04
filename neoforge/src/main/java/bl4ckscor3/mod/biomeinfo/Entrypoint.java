package bl4ckscor3.mod.biomeinfo;

import java.util.Optional;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@Mod(BiomeInfo.MODID)
@EventBusSubscriber
public class Entrypoint implements Platform {
	public Entrypoint(ModContainer modContainer) {
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		modContainer.registerConfig(ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
		BiomeInfo.initialize(this);
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Pre event) {
		BiomeInfoRenderer.onClientTick();
	}

	@SubscribeEvent
	public static void onAddClientReloadListeners(AddClientReloadListenersEvent event) {
		BiomeInfo.registerReloadListener(event::addListener);
	}

	@SubscribeEvent
	public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.TITLE, BiomeInfo.OVERLAY_ID, BiomeInfoRenderer::renderBiomeInfo);
	}

	@SubscribeEvent
	public static void onConfigChange(ModConfigEvent event) {
		BiomeInfo.onConfigChange(event.getConfig().getSpec());
	}

	@Override
	public Optional<String> getModName(String modid) {
		for (ModInfo info : FMLLoader.getCurrent().getLoadingModList().getMods()) {
			if (info.getModId().equals(modid))
				return Optional.of(info.getDisplayName());
		}

		return Optional.empty();
	}
}
