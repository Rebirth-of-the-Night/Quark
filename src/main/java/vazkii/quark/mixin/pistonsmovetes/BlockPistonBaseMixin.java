package vazkii.quark.mixin.pistonsmovetes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.automation.feature.PistonsMoveTEs;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;

@QuarkModuleToggle(module = "automation", feature = "Pistons move t es")
@Mixin(BlockPistonBase.class)
public abstract class BlockPistonBaseMixin {

	@ModifyExpressionValue(method = "canPush", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;hasTileEntity(Lnet/minecraft/block/state/IBlockState;)Z", remap = false))
	private static boolean quark_canPushTileEntity(boolean hasTileEntity, @Local(argsOnly = true) IBlockState state) {
		return PistonsMoveTEs.shouldMoveTE(hasTileEntity, state);
	}

	@WrapOperation(method = "doMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/BlockPistonStructureHelper;getBlocksToMove()Ljava/util/List;", ordinal = 0))
	private List<BlockPos> quark_detachTileEntities(BlockPistonStructureHelper helper, Operation<List<BlockPos>> original, @Local(argsOnly = true) World world, @Local(argsOnly = true) EnumFacing direction, @Local(argsOnly = true) boolean extending) {
		PistonsMoveTEs.detachTileEntities(world, helper, direction, extending);
		return original.call(helper);
	}
}
