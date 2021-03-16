package bl4ckscor3.mod.biomeinfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
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
	private boolean fadingIn = false;

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

	public void onRenderGameOverlay(RenderGameOverlayEvent.Text event)
	{
		if(complete && Configuration.enabled() && !Minecraft.getInstance().gameSettings.showDebugInfo)
		{
			Minecraft mc = Minecraft.getInstance();
			BlockPos pos = mc.getRenderViewEntity().getPosition();

			if(mc.world != null)
			{
				if(mc.world.isBlockPresent(pos))
				{
					Biome biome = mc.world.getBiome(pos);

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
						MatrixStack matrix = event.getMatrixStack();
						TranslationTextComponent biomeName = new TranslationTextComponent(Util.makeTranslationKey("biome", mc.world.func_241828_r().getRegistry(Registry.BIOME_KEY).getKey(biome)));

						matrix.push();
						matrix.scale(scale, scale, scale);

						if(Configuration.textShadow())
							mc.fontRenderer.drawText(matrix, biomeName, Configuration.posX(), Configuration.posY(), Configuration.color() | (alpha << 24));
						else
							mc.fontRenderer.drawTextWithShadow(matrix, biomeName, Configuration.posX(), Configuration.posY(), Configuration.color() | (alpha << 24));

						matrix.pop();
					}
				}
			}
		}
	}
}
