package bl4ckscor3.mod.biomeinfo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
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
							System.out.println(biome.getName().asString());
							matrixStack.push();
							matrixStack.scale(1,1,1);
							mc.textRenderer.drawWithShadow(matrixStack, biome.getName().asString(), 50, 50, 0xFFFFFF| (255 << 24));
							matrixStack.pop();
						}
					}
				}
			}
		});
	}
}
