package vazkii.quark.mixin.totemofholding;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.oddities.feature.TotemOfHolding;

@QuarkModuleToggle(module = "oddities", feature = "Totem of holding")
@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin {

	@WrapMethod(method = "isOutlineActive")
	private boolean quark_isOutlineActive(Entity entity, Entity viewer, ICamera camera, Operation<Boolean> original) {
		return TotemOfHolding.isOutlineActive(entity, viewer) || original.call(entity, viewer, camera);
	}
}
