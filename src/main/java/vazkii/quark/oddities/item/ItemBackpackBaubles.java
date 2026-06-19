package vazkii.quark.oddities.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.render.IRenderBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.oddities.client.model.ModelBackpack;
import vazkii.quark.oddities.feature.Backpacks;

import static vazkii.quark.oddities.feature.Backpacks.backpack;

@Optional.InterfaceList(value = {
        @Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles", striprefs = true),
        @Optional.Interface(iface = "baubles.api.render.IRenderBauble", modid = "baubles", striprefs = true)
})
public class ItemBackpackBaubles extends ItemBackpack implements IBauble, IRenderBauble {

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
            for (int i = 0; i < baubles.getSlots(); i++) {
                if ((baubles.getStackInSlot(i) == null || baubles.getStackInSlot(i).isEmpty())
                        && baubles.isItemValidForSlot(i, stack, player)) {
                    baubles.setStackInSlot(i, stack.copy());
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                    }
                    onEquipped(stack, player);

                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
                }
            }
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.BODY;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return !Backpacks.isEntityWearingBackpack(player, itemstack);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return Backpacks.superOpMode || !doesBackpackHaveItems(itemstack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Optional.Method(modid = "baubles")
    public void onPlayerBaubleRender(ItemStack itemStack, EntityPlayer player, RenderType renderType, float v) {
        if (!player.world.isRemote) return;

        if (renderType != RenderType.BODY) return;

        if (model == null) model = new ModelBackpack();

        model.setModelAttributes(new ModelPlayer(0.0F, false));

        Minecraft.getMinecraft().renderEngine.bindTexture(WORN_TEXTURE_RL);

        int i = backpack.getColor(itemStack);
        float red = (float) (i >> 16 & 255) / 255.0F;
        float green = (float) (i >> 8 & 255) / 255.0F;
        float blue = (float) (i & 255) / 255.0F;

        GlStateManager.color(red, green, blue, 1);


        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        float f = this.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
        float f1 = this.interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, partialTicks);
        float f2 = f1 - f;
//		float f4 = renderer.prepareScale(player, Minecraft.getMinecraft().getRenderPartialTicks());
        float f4 = 0.0625F;
        float f8 = (float) player.ticksExisted + partialTicks;
        float f5 = player.prevLimbSwingAmount + (player.limbSwingAmount - player.prevLimbSwingAmount) * partialTicks;
        float f6 = player.limbSwing - player.limbSwingAmount * (1.0F - partialTicks);
        float f7 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;

        model.setVisible(false);
        model.bipedBody.showModel = true;

        model.render(player, f6, f5, f8, f2, f7, f4);

        Minecraft.getMinecraft().renderEngine.bindTexture(WORN_OVERLAY_TEXTURE_RL);

        GlStateManager.color(1, 1, 1, 1);

        model.render(player, 0, 0, 1000, 0, 0, 0.0625F);
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot() {
        return null;
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        if (stack.getItem() instanceof ItemBackpack && entity instanceof EntityPlayer) {
            return false;
        }
        return super.isValidArmor(stack, armorType, entity);
    }
}
