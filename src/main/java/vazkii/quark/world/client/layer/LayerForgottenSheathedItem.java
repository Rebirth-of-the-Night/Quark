package vazkii.quark.world.client.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.ItemStack;
import vazkii.quark.world.client.render.RenderForgotten;
import vazkii.quark.world.entity.EntityForgotten;

public class LayerForgottenSheathedItem implements LayerRenderer<EntitySkeleton> {
    private final RenderSkeleton renderer;

    public LayerForgottenSheathedItem(RenderForgotten renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntitySkeleton entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!(entity instanceof EntityForgotten)) {
            return;
        }

        EntityForgotten forgotten = (EntityForgotten) entity;
        ItemStack sheathedItem = forgotten.getDataManager().get(EntityForgotten.SHEATHED_ITEM);

        if (!sheathedItem.isEmpty()) {
            GlStateManager.pushMatrix();

            GlStateManager.translate(0.1F, 0.2F, 0.15F);
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);

            Minecraft.getMinecraft().getItemRenderer().renderItem(forgotten, sheathedItem, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}