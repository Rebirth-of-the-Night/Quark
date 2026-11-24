package vazkii.quark.mixin.bettercraftshifting;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.management.feature.BetterCraftShifting;

@QuarkModuleToggle(module = "management", feature = "Better craft shifting", subfeature = "Enable Villager")
@Mixin(ContainerMerchant.class)
public abstract class ContainerMerchantMixin {

	@WrapOperation(method = "transferStackInSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/ContainerMerchant;mergeItemStack(Lnet/minecraft/item/ItemStack;IIZ)Z", ordinal = 1))
	private boolean quark_shiftBoundaries(ContainerMerchant container, ItemStack stack, int startIndex, int endIndex, boolean reverseDirection, Operation<Boolean> original) {
		int min = BetterCraftShifting.getMinInventoryBoundaryVillager(startIndex, endIndex);
		int max = BetterCraftShifting.getMaxInventoryBoundaryVillager(startIndex, endIndex);
		return original.call(container, stack, min, max, reverseDirection);
	}
}
