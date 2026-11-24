package vazkii.quark.mixin.pistonspushpullitems;

import net.minecraft.tileentity.TileEntityPiston;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.automation.feature.PistonsPushPullItems;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;

@QuarkModuleToggle(module = "automation", feature = "Pistons Push and Pull Items")
@Mixin(TileEntityPiston.class)
public abstract class TileEntityPistonMixin {

	@Inject(method = "update", at = @At("HEAD"))
	private void quark_pushPullItems(CallbackInfo ci) {
		PistonsPushPullItems.onPistonUpdate((TileEntityPiston) (Object) this);
	}
}
