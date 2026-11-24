package vazkii.quark.mixin.pistonsmovetes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.automation.feature.PistonsMoveTEs;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.base.module.ModuleLoader;

@QuarkModuleToggle(module = "automation", feature = "Pistons move t es")
@Mixin(TileEntityPiston.class)
public abstract class TileEntityPistonMixin {

	@WrapOperation(method = {"update", "clearPistonTileEntity"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z", ordinal = 0))
	private boolean quark_setPistonBlock(World world, BlockPos pos, IBlockState state, int flags, Operation<Boolean> original) {
		if (!ModuleLoader.isFeatureEnabled(PistonsMoveTEs.class))
			return original.call(world, pos, state, flags);
		return PistonsMoveTEs.setPistonBlock(world, pos, state, flags);
	}
}
