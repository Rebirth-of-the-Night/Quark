package vazkii.quark.mixin.colorrunes;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.misc.feature.ColorRunes;

@QuarkModuleToggle(module = "misc", feature = "Color runes")
@Mixin(RenderItem.class)
public abstract class RenderItemMixin {

	@Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", at = @At("HEAD"))
	private void quark_setRuneTargetStack(ItemStack stack, IBakedModel model, CallbackInfo ci) {
		ColorRunes.setTargetStack(stack);
	}

	@ModifyConstant(method = "renderEffect", constant = @Constant(intValue = -8372020)) // =#8040CC
	private int quark_applyRuneColor(int color) {
		return ColorRunes.getColor(color);
	}
}
