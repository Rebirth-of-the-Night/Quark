package vazkii.quark.mixin.pistonsmovetes;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.tileentity.TileEntityPistonRenderer;
import net.minecraft.tileentity.TileEntityPiston;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.quark.automation.client.render.PistonTileEntityRenderer;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;

@QuarkModuleToggle(module = "automation", feature = "Pistons move t es")
@Mixin(TileEntityPistonRenderer.class)
public abstract class TileEntityPistonRendererMixin {

	@WrapMethod(method = "render(Lnet/minecraft/tileentity/TileEntityPiston;DDDFIF)V")
	private void quark_renderPistonBlock(TileEntityPiston te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, Operation<Void> original) {
		if (!PistonTileEntityRenderer.renderPistonBlock(te, x, y, z, partialTicks))
			original.call(te, x, y, z, partialTicks, destroyStage, alpha);
	}
}
