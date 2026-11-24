package vazkii.quark.mixin.colorrunes;

import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.misc.feature.ColorRunes;

@QuarkModuleToggle(module = "misc", feature = "Color runes", compatibleWithOptifine = false)
@Mixin(LayerArmorBase.class)
public abstract class LayerArmorBaseGlintMixin {

	@Inject(method = "renderEnchantedGlint", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V", shift = At.Shift.AFTER))
	private static void quark_applyRuneColor(CallbackInfo ci) {
		ColorRunes.applyColor();
	}
}
