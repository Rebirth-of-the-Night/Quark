package vazkii.quark.mixin.itemsflash;

import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.client.feature.ItemsFlashBeforeExpiring;

@QuarkModuleToggle(module = "client", feature = "Items flash before expiring")
@Mixin(EntityItem.class)
public abstract class EntityItemMixin {

	@Inject(method = "onUpdate", at = @At("HEAD"))
	private void quark_updateItemAge(CallbackInfo ci) {
		ItemsFlashBeforeExpiring.updateItemInfo((EntityItem) (Object) this);
	}
}
