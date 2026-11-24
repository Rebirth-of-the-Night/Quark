package vazkii.quark.mixin.ancienttomes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.misc.feature.AncientTomes;

@QuarkModuleToggle(module = "misc", feature = "Ancient tomes")
@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

	@ModifyExpressionValue(method = "getEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getEnchantmentTagList()Lnet/minecraft/nbt/NBTTagList;"))
	private static NBTTagList quark_getEnchantmentsForStack(NBTTagList enchantments, @Local(argsOnly = true) ItemStack stack) {
		return AncientTomes.getEnchantmentsForStack(enchantments, stack);
	}
}
