package bl4ckscor3.mod.biomeinfo;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(BiomeInfo.MODID)
public class BiomeInfo
{
	public static final String MODID = "biomeinfo";
	public static Biome previousBiome;
	public static int displayTime = 0;
	public static int alpha = 0;
	private boolean complete = false;

	public BiomeInfo()
	{
		if(FMLLoader.getDist() == Dist.CLIENT)
		{
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
			MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
			MinecraftForge.EVENT_BUS.addListener(this::onRenderGameOverlay);
		}
	}

	public void onLoadComplete(FMLLoadCompleteEvent event)
	{
		complete = true;
	}

	public void onClientTick(ClientTickEvent event)
	{
		if(!complete)
			return;
		else if(!Configuration.fadeOut() && alpha != 255)
			alpha = 255;
		else if(Configuration.fadeOut())
		{
			if(displayTime > 0)
				displayTime--;
			else if(alpha > 0)
				alpha -= 10;
		}
	}

	public void onRenderGameOverlay(RenderGameOverlayEvent event)
	{
		if(complete && Configuration.enabled() && event.getType() == ElementType.TEXT && !Minecraft.getInstance().gameSettings.showDebugInfo)
		{
			Minecraft mc = Minecraft.getInstance();
			BlockPos pos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getBoundingBox().minY, mc.getRenderViewEntity().posZ);

			if(mc.world != null)
			{
				if(mc.world.isBlockLoaded(pos) && pos.getY() >= 0 && pos.getY() < 256)
				{
					Biome biome = mc.world.getBiome(pos);

					if(previousBiome != biome)
					{
						previousBiome = biome;
						displayTime = Configuration.displayTime();
						alpha = 255;
					}

					if(alpha > 0)
					{
						double scale = Configuration.scale();

						GlStateManager.pushMatrix();
						GlStateManager.scaled(scale, scale, scale);

						if(Configuration.textShadow())
							mc.fontRenderer.drawStringWithShadow(biome.getDisplayName().getFormattedText(), Configuration.posX(), Configuration.posY(), Configuration.color() | (alpha << 24));
						else
							mc.fontRenderer.drawString(biome.getDisplayName().getFormattedText(), Configuration.posX(), Configuration.posY(), Configuration.color() | (alpha << 24));

						GlStateManager.popMatrix();
					}
				}
			}
		}
	}
}
