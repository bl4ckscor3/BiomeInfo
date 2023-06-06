package bl4ckscor3.mod.biomeinfo;

import com.mojang.blaze3d.vertex.PoseStack;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;

public class BiomeInfo implements ClientModInitializer {
	private BiomeInfoConfig config;
	public static Biome previousBiome;
	public static int displayTime = 0;
	public static int alpha = 0;
	public static boolean fadingIn = false;

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
			if (config.enabled && (!config.hideOnDebugScreen || !Minecraft.getInstance().options.renderDebug)) {
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
							float scale = (float) config.scale;
							Component biomeName = Component.translatable(Util.makeDescriptionId("biome", key.location()));
							int length = config.textAlignment.getNegativeOffset(mc.font, biomeName);
							PoseStack pose = graphics.pose();

							pose.pushPose();
							pose.scale(scale, scale, scale);

							if (!config.textShadow)
								graphics.drawString(mc.font, biomeName, config.posX - length, config.posY, config.color | (alpha << 24), false);
							else
								graphics.drawString(mc.font, biomeName, config.posX - length, config.posY, config.color | (alpha << 24), true);

							pose.popPose();
						});
					}
				}
			}
		});
	}
}