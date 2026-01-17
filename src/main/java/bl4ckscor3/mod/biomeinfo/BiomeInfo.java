package bl4ckscor3.mod.biomeinfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.biome.Biome;

public class BiomeInfo implements ClientModInitializer {
	public static final Map<ResourceKey<Biome>, Component> NAME_CACHE = new HashMap<>();

	@Override
	public void onInitializeClient() {
		AutoConfig.register(Configuration.class, JanksonConfigSerializer::new);
		Configuration.bootstrap();
		ClientTickEvents.START_CLIENT_TICK.register(BiomeInfoRenderer::onClientTick);
		HudElementRegistry.attachElementAfter(VanillaHudElements.TITLE_AND_SUBTITLE, ResourceLocation.fromNamespaceAndPath("biomeinfo", "overlay"), BiomeInfoRenderer::renderBiomeInfo);
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(ResourceLocation.fromNamespaceAndPath("biomeinfo", "cache_invalidation"), (state, backgroundExecutor, barrier, gameExecutor) -> CompletableFuture.runAsync(NAME_CACHE::clear, backgroundExecutor).thenCompose(barrier::wait));
	}
}