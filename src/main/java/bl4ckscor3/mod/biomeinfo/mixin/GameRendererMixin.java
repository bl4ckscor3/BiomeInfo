package bl4ckscor3.mod.biomeinfo.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/platform/GlStateManager;alphaFunc(IF)V"
					),
			method = "method_3192")
	public void method_3192(float partialTicks, long nanoTime, boolean draw, CallbackInfo info)
	{
		if(!MinecraftClient.getInstance().options.debugEnabled)
		{
			MinecraftClient mc = MinecraftClient.getInstance();

			if(mc.world != null)
			{
				BlockPos pos = new BlockPos(mc.getCameraEntity().x, mc.getCameraEntity().getBoundingBox().minY, mc.getCameraEntity().z);

				if(mc.world.isBlockLoaded(pos) && pos.getY() >= 0 && pos.getY() < 256)
				{
					Chunk chunk = mc.world.getChunk(pos);

					if(chunk instanceof WorldChunk && !((WorldChunk)chunk).isEmpty())
					{
						GlStateManager.pushMatrix();
						GlStateManager.scaled(2.0D, 2.0D, 2.0D);
						mc.fontRenderer.drawWithShadow(chunk.getBiome(pos).getTextComponent().getFormattedText(), 5, 5, 0xffffff);
						GlStateManager.popMatrix();
					}
				}
			}
		}
	}
}
