package bl4ckscor3.mod.biomeinfo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class BiomeInfoRenderer {
	public static final int MARGIN = 3;
	public static Biome previousBiome;
	public static int displayTime = 0;
	public static int alpha = 0;
	public static boolean fadingIn = false;
	public static final Map<ResourceKey<Biome>, Component> NAME_CACHE = new HashMap<>();

	private BiomeInfoRenderer() {}

	public static void onClientTick(Minecraft mc) {
		if (!fadingIn) {
			if (!Configuration.fadeOut() && alpha != 255)
				alpha = 255;
			else if (Configuration.fadeOut()) {
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
				displayTime = Configuration.displayTime();
				alpha = 255;
			}
		}
	}

	public static void renderBiomeInfo(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		if (Configuration.enabled()) {
			Minecraft mc = Minecraft.getInstance();

			if (hideBecauseOfF1(mc) || hideBecauseOfF3(mc))
				return;

			BlockPos pos = mc.getCameraEntity().blockPosition();

			if (mc.level != null && mc.level.isLoaded(pos)) {
				Holder<Biome> biomeHolder = mc.level.getBiome(pos);

				if (!biomeHolder.isBound())
					return;

				Biome biome = biomeHolder.value();

				if (previousBiome != biome) {
					previousBiome = biome;

					if (Configuration.fadeIn()) {
						displayTime = 0;
						alpha = 0;
						fadingIn = true;
					}
					else {
						displayTime = Math.max(0, Configuration.displayTime());
						alpha = 255;
					}
				}

				if (alpha > 0) {
					biomeHolder.unwrapKey().ifPresent(key -> {
						Component biomeName = getBiomeName(key);
						float scale = (float) Configuration.scale();
						PositionPreset positionPreset = Configuration.positionPreset();
						int textOffset = positionPreset.textAlignment().getNegativeOffset(mc.font, biomeName);
						PoseStack pose = guiGraphics.pose();
						Window window = mc.getWindow();

						pose.pushPose();
						pose.scale(scale, scale, scale);
						guiGraphics.drawString(
							mc.font,
							biomeName,
							(positionPreset.posX(window) - textOffset),
							positionPreset.posY(window, mc.font),
							Configuration.color() | (alpha << 24),
							Configuration.textShadow()
						);
						pose.popPose();
					});
				}
			}
		}
	}

	private static Component getBiomeName(ResourceKey<Biome> key) {
		return NAME_CACHE.computeIfAbsent(key, k -> {
			ResourceLocation location = key.location();
			String translationKey = Util.makeDescriptionId("biome", location);
			MutableComponent biomeName = Component.translatable(translationKey);
			MutableComponent displayName = biomeName;

			if (Configuration.fallbackOnUntranslatableName()) {
				String displayedText = biomeName.getString();

				if (displayedText.equals(translationKey)) {
					String biomePath = key.location().getPath(); //e.g. "birch_forest"
					String formattedBiomeName = snakeCaseToEnglish(biomePath);

					displayName = Component.literal(formattedBiomeName);
				}
			}

			if (Configuration.appendModName()) {
				String modName = getModName(location);

				if (modName != null)
					displayName = displayName.append(Component.literal(String.format(" (%s)", modName)));
			}

			return displayName;
		});
	}

	private static boolean hideBecauseOfF1(Minecraft mc) {
		return mc.options.hideGui && Configuration.hideWithUI();
	}

	private static boolean hideBecauseOfF3(Minecraft mc) {
		return mc.getDebugOverlay().showDebugScreen() && Configuration.hideOnDebugScreen();
	}

	private static String snakeCaseToEnglish(String biomePath) {
		String[] words = biomePath.split("_");
		StringBuilder formatted = new StringBuilder();

		for (String word : words) {
			formatted.append(StringUtils.capitalize(word)).append(" ");
		}

		return formatted.toString().trim();
	}

	private static String getModName(ResourceLocation location) {
		String namespace = location.getNamespace();

		//@formatter:off
		return FabricLoader.getInstance().getAllMods()
			.stream()
			.map(ModContainer::getMetadata)
			.filter(meta -> meta.getId().equals(namespace))
			.findFirst()
			.map(ModMetadata::getName)
			.orElseGet(() -> snakeCaseToEnglish(namespace));
		//@formatter:on
	}
}
