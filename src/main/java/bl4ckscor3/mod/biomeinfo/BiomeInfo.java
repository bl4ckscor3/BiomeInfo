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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(BiomeInfo.MODID)
public class BiomeInfo
{
	public static final String MODID = "biomeinfo";
	public static final IIngameOverlay OVERLAY = OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HUD_TEXT_ELEMENT, MODID + ":overlay", BiomeInfo::renderBiomeInfo);
	private static Biome previousBiome;
	private static int displayTime = 0;
	private static int alpha = 0;
	private static boolean complete = false;
	private static boolean fadingIn = false;

	public BiomeInfo()
	{
		if(FMLLoader.getDist() == Dist.CLIENT)
		{
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModConfigReloading);
			MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
		}
	}

	public void onLoadComplete(FMLLoadCompleteEvent event)
	{
		complete = true;
	}

	public void onClientTick(ClientTickEvent event)
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
		if(complete && Configuration.enabled() && !Minecraft.getInstance().options.renderDebug)
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

	public void onModConfigReloading(ModConfigEvent.Reloading event)
	{
		if(event.getConfig().getSpec() == Configuration.CONFIG_SPEC)
			OverlayRegistry.enableOverlay(OVERLAY, Configuration.enabled());
	}
}
