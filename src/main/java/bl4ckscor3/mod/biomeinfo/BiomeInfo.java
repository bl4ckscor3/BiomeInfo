package bl4ckscor3.mod.biomeinfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.apache.commons.lang3.StringUtils;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.biome.Biome;

public class BiomeInfo implements ClientModInitializer, IdentifiableResourceReloadListener {
	public static final int MARGIN = 3;
	static BiomeInfoConfig config;
	private Biome previousBiome;
	private int displayTime = 0;
	private int alpha = 0;
	private boolean fadingIn = false;
	public static final Map<ResourceKey<Biome>, Component> NAME_CACHE = new HashMap<>();

	@Override
	public void onInitializeClient() {
		AutoConfig.register(BiomeInfoConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(BiomeInfoConfig.class).getConfig();
		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			if (!fadingIn) {
				if (!config.fadeOut && alpha != 255)
					alpha = 255;
				else if (config.fadeOut) {
					if (displayTime > 0)
						displayTime--;
					else if (alpha > 0)
						alpha -= 10;
				}
			}
			else { //when fading in
				alpha += 10;

				if (alpha >= 255) {
					fadingIn = false;
					displayTime = Math.max(0, config.displayTime);
					alpha = 255;
				}
			}
		});
		HudRenderCallback.EVENT.register((graphics, delta) -> {
			if (config.enabled && (!config.hideOnDebugScreen || !Minecraft.getInstance().getDebugOverlay().showDebugScreen())) {
				Minecraft mc = Minecraft.getInstance();
				BlockPos pos = mc.getCameraEntity().blockPosition();

				if (mc.level != null && mc.level.isLoaded(pos)) {
					Holder<Biome> biomeHolder = mc.level.getBiome(pos);

					if (!biomeHolder.isBound())
						return;

					Biome biome = biomeHolder.value();

					if (previousBiome != biome) {
						previousBiome = biome;

						if (config.fadeIn) {
							displayTime = 0;
							alpha = 0;
							fadingIn = true;
						}
						else {
							displayTime = Math.max(0, config.displayTime);
							alpha = 255;
						}
					}

					if (alpha > 0) {
						biomeHolder.unwrapKey().ifPresent(key -> {
							Component biomeName = getBiomeName(key);
							float scale = (float) config.scale;
							PositionPreset positionPreset = config.positionPreset;
							int textOffset = positionPreset.textAlignment().getNegativeOffset(mc.font, biomeName);
							PoseStack pose = graphics.pose();
							Window window = mc.getWindow();

							pose.pushPose();
							pose.scale(scale, scale, scale);
							graphics.drawString(mc.font, biomeName, positionPreset.posX(window) - textOffset, positionPreset.posY(window, mc.font), config.color | (alpha << 24), config.textShadow);
							pose.popPose();
						});
					}
				}
			}
		});
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(this);
	}

	private static Component getBiomeName(ResourceKey<Biome> key) {
		return NAME_CACHE.computeIfAbsent(key, k -> {
			String translationKey = Util.makeDescriptionId("biome", key.location());
			Component biomeName = Component.translatable(translationKey);
			String displayedText = biomeName.getString();

			if (displayedText.equals(translationKey)) {
				String biomePath = key.location().getPath(); //e.g. "birch_forest"
				String formattedBiomeName = formatBiomeName(biomePath);

				return Component.literal(formattedBiomeName);
			}

			return biomeName;
		});
	}

	private static String formatBiomeName(String biomePath) {
		String[] words = biomePath.split("_");
		StringBuilder formatted = new StringBuilder();

		for (String word : words) {
			formatted.append(StringUtils.capitalize(word)).append(" ");
		}

		return formatted.toString().trim();
	}

	@Override
	public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
		return CompletableFuture.runAsync(NAME_CACHE::clear, executor).thenCompose(preparationBarrier::wait);
	}

	@Override
	public ResourceLocation getFabricId() {
		return ResourceLocation.fromNamespaceAndPath("biomeinfo", "cache_invalidation");
	}
}