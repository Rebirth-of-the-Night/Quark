package vazkii.quark.mixin.renderitemsinchat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.client.feature.RenderItemsInChat;

@QuarkModuleToggle(module = "client", feature = "Render items in chat")
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@ModifyReturnValue(method = "getTextComponent", at = @At("RETURN"))
	private ITextComponent quark_createStackComponent(ITextComponent component) {
		return RenderItemsInChat.createStackComponent(component);
	}
}
