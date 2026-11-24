package vazkii.quark.mixin.ironladders;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.decoration.feature.IronLadders;

@QuarkModuleToggle(module = "decoration", feature = "Iron ladders")
@Mixin(BlockDynamicLiquid.class)
public abstract class BlockDynamicLiquidMixin {

	@ModifyReturnValue(method = "isBlocked", at = @At("RETURN"))
	private boolean quark_isIronLadderBlocking(boolean blocked, World world, BlockPos pos, IBlockState state) {
		return blocked || IronLadders.isBlockNotBrokenByWater(state.getBlock());
	}
}
