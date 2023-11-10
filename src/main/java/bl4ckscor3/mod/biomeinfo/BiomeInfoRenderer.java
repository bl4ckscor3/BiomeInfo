package bl4ckscor3.mod.biomeinfo;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent.ClientTickEvent;

@EventBusSubscriber(modid = BiomeInfo.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class BiomeInfoRenderer {
	public static final IGuiOverlay OVERLAY = BiomeInfoRenderer::renderBiomeInfo;
	public static Biome previousBiome;
	public static int displayTime = 0;
	public static int alpha = 0;
	public static boolean complete = false;
	public static boolean fadingIn = false;

	static {
		NeoForge.EVENT_BUS.addListener(BiomeInfoRenderer::onClientTick);
	}

	public static void onClientTick(ClientTickEvent event) {
		if (complete) {
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
	}

	public static void renderBiomeInfo(ExtendedGui gui, GuiGraphics guiGraphics, float partialTicks, int width, int height) {
		if (complete && Configuration.enabled() && (!Configuration.hideOnDebugScreen() || !Minecraft.getInstance().getDebugOverlay().showDebugScreen())) {
			Minecraft mc = Minecraft.getInstance();
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
						displayTime = Configuration.displayTime();
						alpha = 255;
					}
				}

				if (alpha > 0) {
					biomeHolder.unwrapKey().ifPresent(key -> {
						float scale = (float) Configuration.scale();
						Component biomeName = Component.translatable(Util.makeDescriptionId("biome", key.location()));
						int length = Configuration.textAlignment().getNegativeOffset(mc.font, biomeName);
						PoseStack pose = guiGraphics.pose();

						pose.pushPose();
						pose.scale(scale, scale, scale);
						guiGraphics.drawString(mc.font, biomeName, Configuration.posX() - length, Configuration.posY(), Configuration.color() | (alpha << 24), Configuration.textShadow());
						pose.popPose();
					});
				}
			}
		}
	}

	@SubscribeEvent
	public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
		event.registerAbove(VanillaGuiOverlay.TITLE_TEXT.id(), "overlay", OVERLAY);
	}

	@SubscribeEvent
	public static void onLoadComplete(FMLLoadCompleteEvent event) {
		complete = true;
	}
}
