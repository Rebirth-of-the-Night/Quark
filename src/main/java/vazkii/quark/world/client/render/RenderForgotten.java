package vazkii.quark.world.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import vazkii.quark.world.client.layer.LayerForgottenClothes;
import vazkii.quark.world.client.layer.LayerForgottenEyes;
import vazkii.quark.world.client.layer.LayerForgottenSheathedItem;

import java.util.Iterator;

public class RenderForgotten extends RenderSkeleton {

    private static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/entity/forgotten.png");

    public static final IRenderFactory<EntitySkeleton> FACTORY = RenderForgotten::new;

    public RenderForgotten(RenderManager renderManagerIn) {
        super(renderManagerIn);

//        Iterator<LayerRenderer<AbstractSkeleton>> it = layerRenderers.iterator();
//        while (it.hasNext()) {
//            LayerRenderer<? extends EntityLivingBase> layer = it.next();
//            if (layer instanceof LayerHeldItem)
//                it.remove();
//        }

        addLayer(new LayerForgottenSheathedItem(this));
        addLayer(new LayerForgottenEyes(this));
        addLayer(new LayerForgottenClothes(this));

    }

    @Override
    protected ResourceLocation getEntityTexture(AbstractSkeleton entity) {
        return TEXTURE;
    }

    @Override
    protected void preRenderCallback(AbstractSkeleton entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(1.2F, 1.2F, 1.2F);
    }
}
