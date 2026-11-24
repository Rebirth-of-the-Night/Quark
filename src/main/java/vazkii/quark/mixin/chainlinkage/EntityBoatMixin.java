package vazkii.quark.mixin.chainlinkage;

import net.minecraft.entity.item.EntityBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.quark.automation.feature.ChainLinkage;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;

@QuarkModuleToggle(module = "automation", feature = "Chain linkage")
@Mixin(EntityBoat.class)
public abstract class EntityBoatMixin {

	@Inject(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/item/EntityBoat;dropItemWithOffset(Lnet/minecraft/item/Item;IF)Lnet/minecraft/entity/item/EntityItem;", shift = At.Shift.AFTER))
	private void quark_dropChain(CallbackInfoReturnable<Boolean> cir) {
		ChainLinkage.drop((EntityBoat) (Object) this);
	}
}
