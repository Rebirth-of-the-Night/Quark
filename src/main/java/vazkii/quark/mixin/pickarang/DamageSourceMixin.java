package vazkii.quark.mixin.pickarang;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.misc.feature.Pickarang;

@QuarkModuleToggle(module = "misc", feature = "Pickarang")
@Mixin(DamageSource.class)
public abstract class DamageSourceMixin {

	@WrapMethod(method = "causePlayerDamage")
	private static DamageSource quark_createPickarangDamage(EntityPlayer player, Operation<DamageSource> original) {
		DamageSource source = Pickarang.createDamageSource(player);
		return source != null ? source : original.call(player);
	}
}
