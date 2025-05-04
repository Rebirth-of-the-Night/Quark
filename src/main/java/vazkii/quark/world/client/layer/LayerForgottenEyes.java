package vazkii.quark.world.client.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;
import vazkii.quark.world.client.render.RenderForgotten;
import vazkii.quark.world.entity.EntityForgotten;

public class LayerForgottenEyes implements LayerRenderer<EntitySkeleton> {
    private static final ResourceLocation EYES_TEXTURE = new ResourceLocation("quark", "textures/entity/forgotten_eye.png");
    private final RenderSkeleton renderer;

    public LayerForgottenEyes(RenderForgotten renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntitySkeleton entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!(entity instanceof EntityForgotten)) {
            return;
        }

        this.renderer.bindTexture(EYES_TEXTURE);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();

        GlStateManager.depthMask(false);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}