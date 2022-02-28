package bl4ckscor3.mod.biomeinfo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.biome.Biome;

public class BiomeInfo implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		HudRenderCallback.EVENT.register((matrixStack, delta) -> {
			if(!Minecraft.getInstance().options.renderDebug)
			{
				Minecraft mc = Minecraft.getInstance();

				if(mc.level != null)
				{
					BlockPos pos = mc.getCameraEntity().blockPosition();

					if(mc.level.isInWorldBounds(pos))
					{
						Holder<Biome> biome = mc.level.getBiome(pos);

						if(biome.isBound())
						{
							biome.unwrapKey().ifPresent(key -> {
								TranslatableComponent biomeName = new TranslatableComponent(Util.makeDescriptionId("biome", key.location()));

								matrixStack.pushPose();
								matrixStack.scale(1,1,1);
								mc.font.drawShadow(matrixStack, biomeName, 3, 3, 0xFFFFFFFF);
								matrixStack.popPose();
							});
						}
					}
				}
			}
		});
	}
}
