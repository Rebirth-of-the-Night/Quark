package vazkii.quark.mixin.chainlinkage;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.world.client.render.ChainRenderer;

@QuarkModuleToggle(module = "automation", feature = "Chain linkage")
@Mixin(RenderManager.class)
public abstract class RenderManagerMixin {

	@WrapOperation(method = "renderEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;doRender(Lnet/minecraft/entity/Entity;DDDFF)V"))
	private void quark_renderChain(Render<Entity> render, Entity entity, double x, double y, double z, float yaw, float partialTicks, Operation<Void> original) {
		original.call(render, entity, x, y, z, yaw, partialTicks);
		ChainRenderer.renderChain(render, x, y, z, entity, partialTicks);
	}
}
