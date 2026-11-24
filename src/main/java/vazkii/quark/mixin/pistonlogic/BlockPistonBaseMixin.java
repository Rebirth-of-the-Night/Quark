package vazkii.quark.mixin.pistonlogic;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.base.handler.QuarkPistonStructureHelper;

@QuarkModuleToggle(module = QuarkModuleToggle.GLOBAL, feature = "Use Piston Logic Replacement")
@Mixin(BlockPistonBase.class)
public abstract class BlockPistonBaseMixin {

	@WrapOperation(method = {"doMove", "checkForMove"}, at = @At(value = "NEW", target = "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Z)Lnet/minecraft/block/state/BlockPistonStructureHelper;"))
	private BlockPistonStructureHelper quark_replaceStructureHelper(World world, BlockPos pos, EnumFacing facing, boolean extending, Operation<BlockPistonStructureHelper> original) {
		return new QuarkPistonStructureHelper(original.call(world, pos, facing, extending), world, pos, facing, extending);
	}
}
