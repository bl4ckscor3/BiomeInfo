package bl4ckscor3.mod.biomeinfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@EventBusSubscriber(modid=BiomeInfo.MODID, value=Dist.CLIENT)
public class BiomeInfoRenderer
{
	public static final IIngameOverlay OVERLAY = OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HUD_TEXT_ELEMENT, BiomeInfo.MODID + ":overlay", BiomeInfoRenderer::renderBiomeInfo);
	public static Biome previousBiome;
	public static int displayTime = 0;
	public static int alpha = 0;
	public static boolean complete = false;
	public static boolean fadingIn = false;

	static {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(BiomeInfoRenderer::onLoadComplete);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(BiomeInfoRenderer::onModConfigReloading);
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event)
	{
		if(complete)
		{
			if(!fadingIn)
			{
				if(!Configuration.fadeOut() && alpha != 255)
					alpha = 255;
				else if(Configuration.fadeOut())
				{
					if(displayTime > 0)
						displayTime--;
					else if(alpha > 0)
						alpha -= 10;
				}
			}
			else //when fading in
			{
				alpha += 10;

				if(alpha >= 255)
				{
					fadingIn = false;
					displayTime = Configuration.displayTime();
					alpha = 255;
				}
			}
		}
	}

	public static void renderBiomeInfo(ForgeIngameGui gui, PoseStack matrix, float partialTicks, int width, int height)
	{
		if(complete && Configuration.enabled() && (!Configuration.hideOnDebugScreen() || !Minecraft.getInstance().options.renderDebug))
		{
			Minecraft mc = Minecraft.getInstance();
			BlockPos pos = mc.getCameraEntity().blockPosition();

			if(mc.level != null && mc.level.isLoaded(pos))
			{
				Biome biome = mc.level.getBiome(pos);

				if(previousBiome != biome)
				{
					previousBiome = biome;

					if(Configuration.fadeIn())
					{
						displayTime = 0;
						alpha = 0;
						fadingIn = true;
					}
					else
					{
						displayTime = Configuration.displayTime();
						alpha = 255;
					}
				}

				if(alpha > 0)
				{
					float scale = (float)Configuration.scale();
					TranslatableComponent biomeName = new TranslatableComponent(Util.makeDescriptionId("biome", mc.level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome)));

					matrix.pushPose();
					matrix.scale(scale, scale, scale);

					if(!Configuration.textShadow())
						mc.font.draw(matrix, biomeName, Configuration.posX(), Configuration.posY(), Configuration.color() | (alpha << 24));
					else
						mc.font.drawShadow(matrix, biomeName, Configuration.posX(), Configuration.posY(), Configuration.color() | (alpha << 24));

					matrix.popPose();
				}
			}
		}
	}

	public static void onLoadComplete(FMLLoadCompleteEvent event)
	{
		complete = true;
	}

	public static void onModConfigReloading(ModConfigEvent.Reloading event)
	{
		if(event.getConfig().getSpec() == Configuration.CONFIG_SPEC)
			OverlayRegistry.enableOverlay(OVERLAY, Configuration.enabled());
	}
}
