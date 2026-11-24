package vazkii.quark.mixin.glowingitems;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.client.feature.GlowingItems;

@QuarkModuleToggle(module = "client", feature = "Glowing items", enabledByDefault = false)
@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin {

	@WrapMethod(method = "isOutlineActive")
	private boolean quark_isOutlineActive(Entity entity, Entity viewer, ICamera camera, Operation<Boolean> original) {
		return GlowingItems.isOutlineActive(entity, viewer) || original.call(entity, viewer, camera);
	}
}
