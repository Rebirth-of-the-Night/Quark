package vazkii.quark.mixin.betterfireeffect;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.client.feature.BetterFireEffect;

@QuarkModuleToggle(module = "client", feature = "Better fire effect", subfeature = "Enable Different Render")
@Mixin(Render.class)
public abstract class RenderMixin {

	@WrapMethod(method = "renderEntityOnFire")
	private void quark_renderFire(Entity entity, double x, double y, double z, float partialTicks, Operation<Void> original) {
		if (!BetterFireEffect.renderFire(entity, x, y, z))
			original.call(entity, x, y, z, partialTicks);
	}
}
