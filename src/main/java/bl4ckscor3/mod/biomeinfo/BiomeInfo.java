package bl4ckscor3.mod.biomeinfo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BiomeInfo implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		HudRenderCallback.EVENT.register((matrixStack, delta) -> {
			if(!MinecraftClient.getInstance().options.debugEnabled)
			{
				MinecraftClient mc = MinecraftClient.getInstance();

				if(mc.world != null)
				{
					BlockPos pos = mc.getCameraEntity().getBlockPos();

					if(pos.getY() >= 0 && pos.getY() < 256)
					{
						Biome biome = mc.world.getBiome(pos);

						if(biome != null)
						{
							TranslatableText biomeName = new TranslatableText(Util.createTranslationKey("biome", mc.world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome)));

							matrixStack.push();
							matrixStack.scale(1,1,1);
							mc.textRenderer.drawWithShadow(matrixStack, biomeName, 3, 3, 0xFFFFFFFF);
							matrixStack.pop();
						}
					}
				}
			}
		});
	}
}
