package vazkii.quark.mixin.parroteggs;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vazkii.quark.base.asm.mixin.QuarkModuleToggle;
import vazkii.quark.misc.feature.ParrotEggs;

@QuarkModuleToggle(module = "misc", feature = "Parrot eggs")
@Mixin(LayerEntityOnShoulder.class)
public abstract class LayerEntityOnShoulderMixin {

	@ModifyArg(method = "renderEntityOnShoulder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
	private ResourceLocation quark_replaceParrotTexture(ResourceLocation texture, @Local(argsOnly = true) NBTTagCompound parrot) {
		return ParrotEggs.getTextureForParrot(texture, parrot);
	}
}
