package vazkii.quark.world.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.misc.feature.ColorRunes;
import vazkii.quark.world.feature.DepthMobs;

import java.util.Iterator;

public class EntityForgotten extends EntitySkeleton {

    public static final DataParameter<ItemStack> SHEATHED_ITEM = EntityDataManager.<ItemStack>createKey(EntityForgotten.class, DataSerializers.ITEM_STACK);

    public EntityForgotten(World world) {
        super(world);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0F);
    }


    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SHEATHED_ITEM, ItemStack.EMPTY);
    }

//    @Override
//    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
//        livingdata = super.onInitialSpawn(difficulty, livingdata);
//        return livingdata;
//    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();
            boolean shouldUseBow = target == null;
            if(!shouldUseBow) {
                PotionEffect eff = target.getActivePotionEffect(MobEffects.BLINDNESS);
                shouldUseBow = eff == null || eff.getDuration() < 20;
            }

            boolean isUsingBow = this.getHeldItemMainhand().getItem() instanceof ItemBow;
            if(shouldUseBow != isUsingBow)
                swap();
        }
    }

    private void swap() {
        ItemStack curr = this.getHeldItemMainhand();
        ItemStack off = this.dataManager.get(SHEATHED_ITEM);

        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, off);
        this.dataManager.set(SHEATHED_ITEM, curr);

        Iterator<EntityAITasks.EntityAITaskEntry> it = this.tasks.taskEntries.iterator();
        while(it.hasNext()) {
            EntityAITasks.EntityAITaskEntry entry = it.next();
            if(entry.action instanceof EntityAIAttackMelee || entry.action instanceof EntityAIAttackRangedBow)
                entry.action.resetTask();
        }

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        NBTTagCompound sheathed = new NBTTagCompound();
        this.dataManager.get(SHEATHED_ITEM).writeToNBT(sheathed);
        compound.setTag("sheathed", sheathed);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        NBTTagCompound sheathed = compound.getCompoundTag("sheathed");
        this.dataManager.set(SHEATHED_ITEM, new ItemStack(sheathed));
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        prepareEquipment();
    }

    public void prepareEquipment() {
        ItemStack bow = new ItemStack(Items.BOW);
        ItemStack sheathed = new ItemStack(Items.IRON_SWORD);

        EnchantmentHelper.addRandomEnchantment(this.rand, bow, 20, false);
        EnchantmentHelper.addRandomEnchantment(this.rand, sheathed, 20, false);

        if (ModuleLoader.isFeatureEnabled(ColorRunes.class) && rand.nextBoolean()) {
            int colorIndex = rand.nextInt(16);

            if (!bow.isEmpty()) {
                ItemNBTHelper.setBoolean(bow, ColorRunes.TAG_RUNE_ATTACHED, true);
                ItemNBTHelper.setInt(bow, ColorRunes.TAG_RUNE_COLOR, colorIndex);
            }

            if (!sheathed.isEmpty()) {
                ItemNBTHelper.setBoolean(sheathed, ColorRunes.TAG_RUNE_ATTACHED, true);
                ItemNBTHelper.setInt(sheathed, ColorRunes.TAG_RUNE_COLOR, colorIndex);
            }
        }

        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, bow);
        this.dataManager.set(SHEATHED_ITEM, sheathed);

        this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(DepthMobs.forgotten_hat));
    }

    @Override
    protected EntityArrow getArrow(float distanceFactor) {
        EntityArrow arrow = super.getArrow(distanceFactor);

        if (arrow instanceof EntityTippedArrow) {
            PotionEffect effect = new PotionEffect(MobEffects.BLINDNESS, 100, 0);
            ((EntityTippedArrow)arrow).addEffect(effect);
        } else {
            EntityTippedArrow tippedArrow = new EntityTippedArrow(this.world, this);
            tippedArrow.addEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0));
            arrow = tippedArrow;
        }

        return arrow;
    }

    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && posY < DepthMobs.upperBound;
    }
}