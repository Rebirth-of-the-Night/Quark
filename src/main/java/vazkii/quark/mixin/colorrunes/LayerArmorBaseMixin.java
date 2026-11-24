package vazkii.quark.mixin.colorrunes;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.misc.feature.ColorRunes;

@QuarkModuleToggle(module = "misc", feature = "Color runes")
@Mixin(LayerArmorBase.class)
public abstract class LayerArmorBaseMixin {

	@Inject(method = "renderArmorLayer", at = @At("HEAD"))
	private void quark_setRuneTargetStack(CallbackInfo ci, @Local(argsOnly = true) EntityLivingBase entity, @Local(argsOnly = true) EntityEquipmentSlot slot) {
		ColorRunes.setTargetStack(entity, slot);
	}
}
