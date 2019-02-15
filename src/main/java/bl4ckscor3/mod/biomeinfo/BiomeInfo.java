package bl4ckscor3.mod.biomeinfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.config.ModConfig;

@Mod(BiomeInfo.MODID)
@EventBusSubscriber
public class BiomeInfo
{
	public static final String MODID = "biomeinfo";
	public static Biome previousBiome;
	public static int displayTime = 0;
	public static int alpha = 0;

	public BiomeInfo()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event)
	{
		if(!Configuration.fadeOut() && alpha != 255)
			alpha = 255;

		if(Configuration.fadeOut())
		{
			if(displayTime > 0)
				displayTime--;
			else if(alpha > 0)
				alpha -= 10;
		}
	}

	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent event)
	{
		if(Configuration.enabled() && event.getType() == ElementType.TEXT && !Minecraft.getInstance().gameSettings.showDebugInfo)
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
