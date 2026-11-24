package vazkii.quark.mixin.springyslime;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.tweaks.feature.SpringySlime;

@QuarkModuleToggle(module = "tweaks", feature = "Slime blocks bounce off sides")
@Mixin(Entity.class)
public abstract class EntityMixin {

	@Inject(method = "move", at = @At("HEAD"))
	private void quark_recordMotion(MoverType type, double x, double y, double z, CallbackInfo ci) {
		SpringySlime.recordMotion((Entity) (Object) this);
	}

	/** The first expand is the collision box query, its args are the attempted movement before collisions */
	@WrapOperation(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/AxisAlignedBB;expand(DDD)Lnet/minecraft/util/math/AxisAlignedBB;", ordinal = 0))
	private AxisAlignedBB quark_recordAttemptedMovement(
			AxisAlignedBB box, double x, double y, double z, Operation<AxisAlignedBB> original,
			@Share("attemptedX") LocalDoubleRef attemptedX,
			@Share("attemptedY") LocalDoubleRef attemptedY,
			@Share("attemptedZ") LocalDoubleRef attemptedZ
	) {
		attemptedX.set(x);
		attemptedY.set(y);
		attemptedZ.set(z);
		return original.call(box, x, y, z);
	}

	@Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;doBlockCollisions()V", shift = At.Shift.AFTER))
	private void quark_applyCollisionLogic(
			MoverType type, double x, double y, double z, CallbackInfo ci,
			@Share("attemptedX") LocalDoubleRef attemptedX,
			@Share("attemptedY") LocalDoubleRef attemptedY,
			@Share("attemptedZ") LocalDoubleRef attemptedZ
	) {
		SpringySlime.onEntityCollision((Entity) (Object) this, attemptedX.get(), attemptedY.get(), attemptedZ.get(), x, y, z);
	}
}
