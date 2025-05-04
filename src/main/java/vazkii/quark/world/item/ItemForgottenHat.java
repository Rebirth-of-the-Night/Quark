package vazkii.quark.world.item;

import com.google.common.collect.Multimap;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemModArmor;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.world.client.model.ModelArchaeologistHat;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ItemForgottenHat extends ItemModArmor implements IQuarkItem {

    public static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/entity/forgotten_hat.png");

    private static final UUID REACH_DISTANCE_UUID = UUID.fromString("024aec5f-cbcb-4413-80e0-337e5826f6fa");
    private static final UUID LUCK_UUID = UUID.fromString("1dafcddb-d541-4e92-ad4d-6591e182f426");

    @SideOnly(Side.CLIENT)
    public static ModelBiped headModel;

    public ItemForgottenHat() {
        super("forgotten_hat", ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (headModel == null)
            headModel = new ModelArchaeologistHat();

        return headModel;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (slot == this.armorType) {

            multimap.put(EntityPlayer.REACH_DISTANCE.getName(),
                    new AttributeModifier(REACH_DISTANCE_UUID, "Reach modifier", 2.0D, 0));

            multimap.put(SharedMonsterAttributes.LUCK.getName(),
                    new AttributeModifier(LUCK_UUID, "Luck modifier", 1.0D, 0));
        }

        return multimap;
    }

    @Override
    public boolean hasColor(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return TEXTURE.toString();
    }

    @Nonnull
    @Override
    public IRarity getForgeRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

}
