package vazkii.quark.mixin.witchhat;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.vanity.feature.WitchHat;

@QuarkModuleToggle(module = "vanity", feature = "Witch hat", subfeature = "Make witches ignore players with witch hats")
@Mixin(EntityAITarget.class)
public abstract class EntityAITargetMixin {

	@WrapMethod(method = "isSuitableTarget(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/entity/EntityLivingBase;ZZ)Z")
	private static boolean quark_ignoreWitchHat(EntityLiving attacker, EntityLivingBase target, boolean includeInvincibles, boolean checkSight, Operation<Boolean> original) {
		return !WitchHat.hasWitchHat(attacker, target) && original.call(attacker, target, includeInvincibles, checkSight);
	}
}
