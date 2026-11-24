package vazkii.quark.mixin.emotes;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.vanity.client.emotes.EmoteHandler;

@QuarkModuleToggle(module = "vanity", feature = "Emote system")
@Mixin(ModelBiped.class)
public abstract class ModelBipedMixin {

	@Inject(method = "setRotationAngles", at = @At("RETURN"))
	private void quark_updateEmotes(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity, CallbackInfo ci) {
		EmoteHandler.updateEmotes(entity);
	}
}
