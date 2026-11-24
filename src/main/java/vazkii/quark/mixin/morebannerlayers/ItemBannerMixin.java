package vazkii.quark.mixin.morebannerlayers;

import net.minecraft.item.ItemBanner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.decoration.feature.MoreBannerLayers;

@QuarkModuleToggle(module = "decoration", feature = "More banner layers")
@Mixin(ItemBanner.class)
public abstract class ItemBannerMixin {

	@ModifyConstant(method = "appendHoverTextFromTileEntityTag", constant = @Constant(intValue = 6, ordinal = 0))
	private static int quark_showMoreLayers(int layers) {
		return MoreBannerLayers.getLayerCount();
	}
}
