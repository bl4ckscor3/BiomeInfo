package bl4ckscor3.mod.biomeinfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.biome.Biome;

public class BiomeInfo implements ClientModInitializer, IdentifiableResourceReloadListener {
	public static final Map<ResourceKey<Biome>, Component> NAME_CACHE = new HashMap<>();

	@Override
	public void onInitializeClient() {
		AutoConfig.register(Configuration.class, JanksonConfigSerializer::new);
		Configuration.bootstrap();
		ClientTickEvents.START_CLIENT_TICK.register(BiomeInfoRenderer::onClientTick);
		HudRenderCallback.EVENT.register(BiomeInfoRenderer::renderBiomeInfo);
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(this);
	}

	@Override
	public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
		return CompletableFuture.runAsync(NAME_CACHE::clear, executor).thenCompose(preparationBarrier::wait);
	}

	@Override
	public ResourceLocation getFabricId() {
		return ResourceLocation.fromNamespaceAndPath("biomeinfo", "cache_invalidation");
	}
}