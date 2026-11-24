package vazkii.quark.mixin.extrapotions;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiBeacon;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.misc.feature.ExtraPotions;

@QuarkModuleToggle(module = "misc", feature = "Extra potions")
@Mixin(GuiBeacon.Button.class)
public abstract class GuiBeaconButtonMixin {

	@WrapMethod(method = "drawButton")
	private void quark_renderPotion(Minecraft mc, int mouseX, int mouseY, float partialTicks, Operation<Void> original) {
		GuiBeacon.Button self = (GuiBeacon.Button) (Object) this;
		if (!(self instanceof GuiBeacon.PowerButton) || !(mc.currentScreen instanceof GuiBeacon)
				|| !ExtraPotions.renderPotion(self, (GuiBeacon) mc.currentScreen, mc, mouseX, mouseY, partialTicks))
			original.call(mc, mouseX, mouseY, partialTicks);
	}
}
