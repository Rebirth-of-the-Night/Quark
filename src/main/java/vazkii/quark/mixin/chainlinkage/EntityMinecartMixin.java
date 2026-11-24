package vazkii.quark.mixin.chainlinkage;

import net.minecraft.entity.item.EntityMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.automation.feature.ChainLinkage;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;

@QuarkModuleToggle(module = "automation", feature = "Chain linkage")
@Mixin(EntityMinecart.class)
public abstract class EntityMinecartMixin {

	@Inject(method = "killMinecart", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/item/EntityMinecart;entityDropItem(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/item/EntityItem;", shift = At.Shift.AFTER))
	private void quark_dropChain(CallbackInfo ci) {
		ChainLinkage.drop((EntityMinecart) (Object) this);
	}
}
