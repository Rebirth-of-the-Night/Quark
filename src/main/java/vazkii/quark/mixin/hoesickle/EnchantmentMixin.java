package vazkii.quark.mixin.hoesickle;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.tweaks.feature.HoeSickle;

@QuarkModuleToggle(module = "tweaks", feature = "Hoes break a 5x5 of plants", subfeature = "Hoes Can Have Fortune")
@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

	@ModifyReturnValue(method = "canApply", at = @At("RETURN"))
	private boolean quark_canApplyFortuneToHoe(boolean canApply, ItemStack stack) {
		return canApply || HoeSickle.canFortuneApply((Enchantment) (Object) this, stack);
	}
}
