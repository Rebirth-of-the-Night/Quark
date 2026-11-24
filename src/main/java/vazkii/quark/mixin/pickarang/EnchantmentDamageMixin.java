package vazkii.quark.mixin.pickarang;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.misc.feature.Pickarang;

@QuarkModuleToggle(module = "misc", feature = "Pickarang")
@Mixin(EnchantmentDamage.class)
public abstract class EnchantmentDamageMixin {

	@ModifyReturnValue(method = "canApply", at = @At("RETURN"))
	private boolean quark_canApplyToPickarang(boolean canApply, ItemStack stack) {
		return canApply || Pickarang.canSharpnessApply(stack);
	}
}
