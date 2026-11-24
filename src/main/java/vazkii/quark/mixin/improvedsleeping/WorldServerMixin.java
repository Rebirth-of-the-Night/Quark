package vazkii.quark.mixin.improvedsleeping;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.tweaks.feature.ImprovedSleeping;

@QuarkModuleToggle(module = "tweaks", feature = "Improved sleeping")
@Mixin(WorldServer.class)
public abstract class WorldServerMixin {

	/** {@link ImprovedSleeping#isEveryoneAsleep} returns 0 for vanilla behaviour, otherwise result + 1 */
	@WrapMethod(method = "areAllPlayersAsleep")
	private boolean quark_areAllPlayersAsleep(Operation<Boolean> original) {
		int asleep = ImprovedSleeping.isEveryoneAsleep((WorldServer) (Object) this);
		return asleep == 0 ? original.call() : asleep == 2;
	}

	@Inject(method = "wakeAllPlayers", at = @At("HEAD"))
	private void quark_whenPlayersWake(CallbackInfo ci) {
		ImprovedSleeping.whenNightPasses((WorldServer) (Object) this);
	}
}
