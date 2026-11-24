package vazkii.quark.mixin.glowingitems;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import meldexun.renderlib.renderer.entity.EntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.client.feature.GlowingItems;

@QuarkModuleToggle(module = "client", feature = "Glowing items", enabledByDefault = false, requiresRenderLib = true)
@Mixin(EntityRenderer.class)
public abstract class RenderLibEntityRendererMixin {

	@WrapMethod(method = "shouldRenderOutlines", remap = false)
	private boolean quark_shouldRenderOutlines(Entity entity, Operation<Boolean> original) {
		return GlowingItems.isOutlineActive(entity, Minecraft.getMinecraft().player) || original.call(entity);
	}
}
