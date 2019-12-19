package bl4ckscor3.mod.biomeinfo.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;render(F)V"
					),
			method = "render")
	public void render(float partialTicks, long nanoTime, boolean draw, CallbackInfo info)
	{
		if(!MinecraftClient.getInstance().options.debugEnabled)
		{
			MinecraftClient mc = MinecraftClient.getInstance();

			if(mc.world != null)
			{
				BlockPos pos = new BlockPos(mc.getCameraEntity());

				if(pos.getY() >= 0 && pos.getY() < 256)
				{
					Biome biome = mc.world.getBiome(pos);

					if(biome != null)
					{
						RenderSystem.pushMatrix();
						RenderSystem.scaled(1.0D, 1.0D, 1.0D);
						mc.textRenderer.drawWithShadow(biome.getName().asFormattedString(), 5, 5, 0xffffff);
						RenderSystem.popMatrix();
					}
				}
			}
		}
	}
}
