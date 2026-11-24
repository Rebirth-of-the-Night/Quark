package vazkii.quark.mixin.boatsails;

import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.entity.item.EntityBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.vanity.client.render.BoatBannerRenderer;

@QuarkModuleToggle(module = "vanity", feature = "Boat sails")
@Mixin(RenderBoat.class)
public abstract class RenderBoatMixin {

	@Inject(method = "doRender(Lnet/minecraft/entity/item/EntityBoat;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", shift = At.Shift.AFTER))
	private void quark_renderBanner(EntityBoat entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
		BoatBannerRenderer.renderBanner(entity, partialTicks);
	}
}
