package vazkii.quark.world.client.layer;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class LayerForgottenClothes implements LayerRenderer<EntitySkeleton> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/entity/forgotten_overlay.png");
    private final RenderLivingBase<?> render;
    private final ModelSkeleton model;

    public LayerForgottenClothes(RenderLivingBase<?> render) {
        this.render = render;
        model = new ModelSkeleton(0.25F, true);
    }

    @Override
    public void doRenderLayer(@Nonnull EntitySkeleton entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        model.setModelAttributes(render.getMainModel());
        model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        render.bindTexture(TEXTURE);
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}