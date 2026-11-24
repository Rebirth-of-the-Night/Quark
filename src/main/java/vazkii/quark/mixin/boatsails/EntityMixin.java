package vazkii.quark.mixin.boatsails;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.vanity.feature.BoatSails;

@QuarkModuleToggle(module = "vanity", feature = "Boat sails")
@Mixin(Entity.class)
public abstract class EntityMixin {

	@Inject(method = "onEntityUpdate", at = @At("HEAD"))
	private void quark_updateBoat(CallbackInfo ci) {
		BoatSails.onBoatUpdate((Entity) (Object) this);
	}
}
