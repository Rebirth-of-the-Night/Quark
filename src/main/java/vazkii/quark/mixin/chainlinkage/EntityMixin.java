package vazkii.quark.mixin.chainlinkage;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.automation.feature.ChainLinkage;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;

@QuarkModuleToggle(module = "automation", feature = "Chain linkage")
@Mixin(Entity.class)
public abstract class EntityMixin {

	@Inject(method = "onEntityUpdate", at = @At("HEAD"))
	private void quark_updateChain(CallbackInfo ci) {
		ChainLinkage.onEntityUpdate((Entity) (Object) this);
	}
}
