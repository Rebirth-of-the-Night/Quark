package vazkii.quark.mixin.morebannerlayers;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.item.crafting.RecipesBanners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.decoration.feature.MoreBannerLayers;

@QuarkModuleToggle(module = "decoration", feature = "More banner layers")
@Mixin(RecipesBanners.RecipeAddPattern.class)
public abstract class RecipeAddPatternMixin {

	@ModifyExpressionValue(method = "matches", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntityBanner;getPatterns(Lnet/minecraft/item/ItemStack;)I", ordinal = 0))
	private int quark_shiftLayerCount(int patterns) {
		return patterns + 6 - MoreBannerLayers.getLayerCount();
	}
}
