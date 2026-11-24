package vazkii.quark.mixin.boatsails;

import net.minecraft.entity.item.EntityBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.vanity.feature.BoatSails;

@QuarkModuleToggle(module = "vanity", feature = "Boat sails")
@Mixin(EntityBoat.class)
public abstract class EntityBoatMixin {

	@Inject(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/item/EntityBoat;dropItemWithOffset(Lnet/minecraft/item/Item;IF)Lnet/minecraft/entity/item/EntityItem;", shift = At.Shift.AFTER))
	private void quark_dropBanner(CallbackInfoReturnable<Boolean> cir) {
		BoatSails.dropBoatBanner((EntityBoat) (Object) this);
	}
}
