package vazkii.quark.mixin.coloredlights;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.experimental.features.ColoredLights;

@QuarkModuleToggle(module = "experimental", feature = "Colored lights", enabledByDefault = false, compatibleWithOptifine = false)
@Mixin(BlockModelRenderer.class)
public abstract class BlockModelRendererMixin {

	@Inject(method = "renderQuadsFlat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;putPosition(DDD)V", ordinal = 0))
	private void quark_putColorsFlat(CallbackInfo ci, @Local(argsOnly = true) IBlockAccess world, @Local(argsOnly = true) IBlockState state, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) BufferBuilder buffer, @Local BakedQuad quad, @Local(argsOnly = true) int brightness) {
		ColoredLights.putColorsFlat(world, state, pos, buffer, quad, brightness);
	}
}
