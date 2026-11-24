package vazkii.quark.mixin.betternausea;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.experimental.features.BetterNausea;

@QuarkModuleToggle(module = "experimental", feature = "Better nausea", enabledByDefault = false)
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

	@Inject(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;setupCameraTransform(FI)V", shift = At.Shift.AFTER))
	private void quark_renderNausea(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
		BetterNausea.renderNausea((EntityRenderer) (Object) this, partialTicks);
	}
}
