package vazkii.quark.mixin.renderitemsinchat;

import net.minecraftforge.client.model.pipeline.LightUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.client.feature.RenderItemsInChat;

@QuarkModuleToggle(module = "client", feature = "Render items in chat")
@Mixin(LightUtil.class)
public abstract class LightUtilMixin {

	@ModifyVariable(method = "renderQuadColor", at = @At("HEAD"), argsOnly = true, remap = false)
	private static int quark_transformQuadRenderColor(int color) {
		return RenderItemsInChat.transformColor(color);
	}
}
