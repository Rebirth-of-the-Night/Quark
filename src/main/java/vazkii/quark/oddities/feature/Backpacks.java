package vazkii.quark.oddities.feature;

import java.util.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.InventoryIIH;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.network.message.MessageHandleBackpack;
import vazkii.quark.oddities.RecipesBackpackDyes;
import vazkii.quark.oddities.client.gui.GuiBackpackInventory;
import vazkii.quark.oddities.item.ItemBackpack;

public class Backpacks extends Feature {

	public static ItemBackpack backpack;
	
	public static boolean superOpMode, enableTrades, enableCrafting, enablePickUp;

	public static  int leatherCount, minEmeralds, maxEmeralds;
	
	@SideOnly(Side.CLIENT)
	public static boolean backpackRequested;

	private final Map<UUID, PickUpTask> pickUpQueue = new HashMap<>();
	private boolean processingPickUpQueue = false;
	
	@Override
	public void setupConfig() {
		enableTrades = loadPropBool("Enable Trade", "Set this to false if you want to disable the villager trade so you can add an alternate acquisition method", true);
		enableCrafting = loadPropBool("Enable Crafting", "Set this to true to enable a crafting recipe", false);
		enablePickUp = loadPropBool("Enable Backpack Pick-Up", "Set this to true to allow items to be picked up into backpacks when the main inventory is full", false);
		superOpMode = loadPropBool("Unbalanced Mode", "Set this to true to allow the backpacks to be unequipped even with items in them", false);
		leatherCount = loadPropInt("Required Leather", "", 12);
		minEmeralds = loadPropInt("Min Required Emeralds", "", 12);
		maxEmeralds = loadPropInt("Max Required Emeralds", "", 18);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		backpack = new ItemBackpack();
		
		if (enableCrafting)
			RecipeHandler.addOreDictRecipe(new ItemStack(backpack),
					"LLL", 
					"LCL", 
					"LLL",
					'L', Items.LEATHER,
					'C', "chestWood");
		
		new RecipesBackpackDyes();
	}
	
	@SubscribeEvent
	public void onRegisterVillagers(RegistryEvent.Register<VillagerProfession> event) {
		if (!enableTrades)
			return;
		
		VillagerProfession butcher = event.getRegistry().getValue(new ResourceLocation("minecraft:butcher"));
		if (butcher != null) {
			VillagerCareer leatherworker = butcher.getCareer(1);

			leatherworker.addTrade(1, new BackpackTrade());
		}
 	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onOpenGUI(GuiOpenEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player != null && isInventoryGUI(event.getGui()) && !player.isCreative() && isEntityWearingBackpack(player)) {
			requestBackpack();
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void clientTick(ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (isInventoryGUI(mc.currentScreen) && !backpackRequested && isEntityWearingBackpack(mc.player)) {
			requestBackpack();
			backpackRequested = true;
		} else if (mc.currentScreen instanceof GuiBackpackInventory)
			backpackRequested = false;
	}
	
	private void requestBackpack() {
		NetworkHandler.INSTANCE.sendToServer(new MessageHandleBackpack(true));
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void removeCurseTooltip(ItemTooltipEvent event) {
		if (!superOpMode && event.getItemStack().getItem() instanceof ItemBackpack)
			for (String s : event.getToolTip())
				if (s.equals(Enchantments.BINDING_CURSE.getTranslatedName(1))) {
					event.getToolTip().remove(s);
					return;
				}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void preItemPickUp(EntityItemPickupEvent event) {
		if (!enablePickUp || processingPickUpQueue) return;

		EntityItem item = event.getItem();
		if (item.isDead || item.getItem().isEmpty()) return;

		EntityPlayer player = event.getEntityPlayer();
		if (!isEntityWearingBackpack(player)) return;

		pickUpQueue.computeIfAbsent(item.getUniqueID(), k -> new PickUpTask(item)).players.add(player);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void postItemPickUp(PlayerEvent.ItemPickupEvent event) {
		if (!enablePickUp || processingPickUpQueue) return;

		EntityItem item = event.getOriginalEntity();
		if (item.isDead || item.getItem().isEmpty())
			pickUpQueue.remove(item.getUniqueID());
	}

	@SubscribeEvent
	public void onServerTickEnd(TickEvent.ServerTickEvent event) {
		if (!enablePickUp || event.phase != TickEvent.Phase.END) return;

		processingPickUpQueue = true;
		try {
			for (PickUpTask task : pickUpQueue.values()) {
				for (EntityPlayer player : task.players) {
					if (task.item.isDead) break;
					ItemStack stack = task.item.getItem();
					if (stack.isEmpty()) break;
					IItemHandlerModifiable inv = getBackpackInventory(player);
					if (inv == null) continue;

					ItemStack original = stack.copy();
					ItemStack remainder = ItemHandlerHelper.insertItemStacked(inv, stack, false);
					if (remainder.getCount() >= original.getCount()) continue; // nothing was transferred

					task.item.setItem(remainder);
					ItemStack transferred = ItemHandlerHelper.copyStackWithSize(original, original.getCount() - remainder.getCount());
					FMLCommonHandler.instance().firePlayerItemPickupEvent(player, task.item, transferred);

					player.addStat(StatList.getObjectsPickedUpStats(original.getItem()), transferred.getCount());
					if (task.item.getItem().isEmpty()) {
						player.onItemPickup(task.item, transferred.getCount());
						task.item.setDead();
						// vanilla code resets the entity's stack to its original count here; it's unclear why this would be useful
						break;
					}
				}
			}
			pickUpQueue.clear();
		} finally {
			processingPickUpQueue = false;
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static boolean isInventoryGUI(GuiScreen gui) {
		return gui != null && gui.getClass() == GuiInventory.class;
	}
	
	public static boolean isEntityWearingBackpack(Entity e) {
		if (Loader.isModLoaded("baubles")) {
			if (e instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) e;
				return BaublesApi.isBaubleEquipped(player, backpack) != -1;
			}
		} else {
			if (e instanceof EntityLivingBase) {
				EntityLivingBase living = (EntityLivingBase) e;
				ItemStack chestArmor = living.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
				return chestArmor.getItem() instanceof ItemBackpack;
			}
		}
		
		return false;
	}
	
	public static boolean isEntityWearingBackpack(Entity e, ItemStack stack) {
		if (Loader.isModLoaded("baubles")) {
			if (e instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) e;
				return BaublesApi.isBaubleEquipped(player, stack.getItem()) != -1;
			}
		} else {
			if (e instanceof EntityLivingBase) {
				EntityLivingBase living = (EntityLivingBase) e;
				ItemStack chestArmor = living.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
				return chestArmor == stack;
			}
		}
		
		return false;
	}

	@Nullable
	public static IItemHandlerModifiable getBackpackInventory(Entity e) {
		ItemStack stack = null;
		if (Loader.isModLoaded("baubles")) {
			if (e instanceof EntityPlayer)
				stack = BaublesApi.getBaublesHandler((EntityPlayer) e).getStackInSlot(BaublesApi.isBaubleEquipped((EntityPlayer) e, backpack));
		} else {
			if (e instanceof EntityLivingBase)
				stack = ((EntityLivingBase) e).getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		}
		return stack != null && stack.getItem() == backpack ? new InventoryIIH(stack) : null;
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
	public static class BackpackTrade implements EntityVillager.ITradeList {

		@Override
		public void addMerchantRecipe(@Nonnull IMerchant merchant, @Nonnull MerchantRecipeList recipeList, @Nonnull Random random) {
			int emeraldCount = random.nextInt(maxEmeralds - minEmeralds) + minEmeralds;
			recipeList.add(new MerchantRecipe(new ItemStack(Items.LEATHER, leatherCount), new ItemStack(Items.EMERALD, emeraldCount), new ItemStack(backpack)));
		}
	}

	private static class PickUpTask {

		final EntityItem item;
		final List<EntityPlayer> players = new ArrayList<>();

        PickUpTask(EntityItem item) {
            this.item = item;
        }

    }

}
