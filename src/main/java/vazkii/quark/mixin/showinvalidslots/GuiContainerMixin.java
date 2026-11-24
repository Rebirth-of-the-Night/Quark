package vazkii.quark.mixin.showinvalidslots;

import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.client.feature.ShowInvalidSlots;

@QuarkModuleToggle(module = "client", feature = "Show invalid slots")
@Mixin(GuiContainer.class)
public abstract class GuiContainerMixin {

	@Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGuiContainerForegroundLayer(II)V"))
	private void quark_drawInvalidSlots(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		ShowInvalidSlots.renderElements((GuiContainer) (Object) this);
	}
}
