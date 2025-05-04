package vazkii.quark.oddities.feature;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.client.AtlasSpriteHelper;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.util.ItemMetaHelper;
import vazkii.quark.oddities.client.render.RenderTotemOfHolding;
import vazkii.quark.oddities.entity.EntityTotemOfHolding;
import vazkii.quark.oddities.item.ItemSoulCompass;
import vazkii.quark.oddities.item.ItemTotemOfHolding;
import vazkii.quark.world.feature.Wraiths;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

public class TotemOfHolding extends Feature {
	
	private static final String TAG_LAST_TOTEM = "quark:lastTotemOfHolding";

	private static final String TAG_DEATH_X = "quark:deathX";
	private static final String TAG_DEATH_Z = "quark:deathZ";
	private static final String TAG_DEATH_DIM = "quark:deathDim";
	
	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite totemSprite;
	
	public static Item soul_compass, totem_item;
	
	public static boolean darkSoulsMode, enableOnPK, destroyItems, anyoneCollect, enableSoulCompass, shouldBlacklistBeWhitelist, enableTotemItem;
	public static float entityScale;
	public static double glowRange;
	public static int deathTime, baseDropAmount, randomBonusDrops;

	private static String[] tempBlacklist;
	public static Set<Pair<Item, Integer>> holdingBlacklist;

	private static String tempSavingItem;
	public static Pair<Item, Integer> savingItem;
	
	@Override
	public void setupConfig() {
		darkSoulsMode = loadPropBool("Dark Souls Mode", "Set this to false to remove the behaviour where totems destroy themselves if the player dies again.", true);
		enableOnPK = loadPropBool("Spawn Totem on PVP Kill", "", false);
		destroyItems = loadPropBool("Destroy Lost Items", "Set this to true to make it so that if a totem is destroyed, the items it holds are destroyed alongside it rather than dropped", false);
		anyoneCollect = loadPropBool("Allow Anyone to Collect", "Set this to false to only allow the owner of a totem to collect its items rather than any player", true);
		enableSoulCompass = loadPropBool("Enable Soul Compass", "", true);
		shouldBlacklistBeWhitelist = loadPropBool("Should Holding Blacklist Be Whitelist", "", false);
		tempBlacklist = loadPropStringList("Holding Blacklist", 
				"Items that should be prevented from being saved by the totem\n" + 
				"Format is modid:item[:meta]", 
				new String[0]);
		tempSavingItem = loadPropString("Saving Item", "An item that must be in the player inventory for the totem to work. Set to 'none' to disable", "quark:totem_of_holding");
		enableTotemItem = loadPropBool("Enable Totem of Holding Item", "", true);
		entityScale = (float) loadPropDouble("Totem of Holding Entity Scale", "Displayed scale of the totem of holding entity", 1.0D);
		deathTime = loadPropInt("Totem of Holding Vanish Time", "How long it takes for the totem to vanish once depleted, in ticks", 40);
		glowRange = loadPropDouble("Totem Glow Range", "Maximum range at which totems visibly glow. Default is 32; set to 0 to disable", 32);
		baseDropAmount = loadPropInt("Totem Drop Amount Base", "The minimum number of items the totem always drops per hit", 3);
		randomBonusDrops = loadPropInt("Totem Drop Amount Bonus", "The extra random drops per hit. Ranges from 0 to this number", 4);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		if (enableSoulCompass)
			soul_compass = new ItemSoulCompass();

		String totemName = "quark:totem_of_holding";
		EntityRegistry.registerModEntity(new ResourceLocation(totemName), EntityTotemOfHolding.class, totemName, LibEntityIDs.TOTEM_OF_HOLDING, Quark.instance, 64, 128, false);
		
		if (enableTotemItem)
			totem_item = new ItemTotemOfHolding("holding_totem");
	}
	
	@Override
	public void postPreInit() {
		if (enableSoulCompass)
			RecipeHandler.addShapelessOreDictRecipe(new ItemStack(soul_compass), 
					(Wraiths.soul_bead == null ? new ItemStack(Blocks.SOUL_SAND) : new ItemStack(Wraiths.soul_bead)), 
					new ItemStack(Items.COMPASS));
		
		if (enableTotemItem)
			RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(totem_item), 
					" P ",
					"SCS",
					" A ",
					'P', "enderpearl",
					'C', "chestWood",
					'S', "stone",
					'A', Items.ARMOR_STAND);
		
		holdingBlacklist = ItemMetaHelper.getFromStringArray("totem holding blacklist item", tempBlacklist).stream()
				.filter(i -> !i.isEmpty())
				.map(s -> Pair.of(s.getItem(), s.getMetadata()))
				.collect(Collectors.toSet());
		
		ItemStack item = (tempSavingItem.equals("none") ? ItemStack.EMPTY : new ArrayList<>(ItemMetaHelper.getFromString("totem holding saving item", tempSavingItem, false)).get(0));
		savingItem = !item.isEmpty() ? Pair.of(item.getItem(), item.getMetadata()) : Pair.of(Items.AIR, 0);
	}
	
	@Override
	public void preInitClient() {
		RenderingRegistry.registerEntityRenderingHandler(EntityTotemOfHolding.class, RenderTotemOfHolding.factory());
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitch(TextureStitchEvent event) {
		totemSprite = AtlasSpriteHelper.forName(event.getMap(), new ResourceLocation(LibMisc.MOD_ID , "items/holding_totem"));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerDrops(PlayerDropsEvent event) {
		List<EntityItem> drops = event.getDrops();
		
		if (!event.isCanceled() && (enableOnPK || !(event.getSource().getTrueSource() instanceof EntityPlayer))) {
			EntityPlayer player = event.getEntityPlayer();
			NBTTagCompound data = player.getEntityData();
			NBTTagCompound persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			
			if (!drops.isEmpty()) {
				EntityTotemOfHolding totem = new EntityTotemOfHolding(player.world);
				totem.setPosition(player.posX, Math.max(3, player.posY + 1), player.posZ);
				totem.setOwner(player);
				totem.setCustomNameTag(player.getDisplayNameString());
				List<EntityItem> saving = drops.stream()
						.filter(Objects::nonNull)
						.map(e -> Pair.of(e.getItem(), e))
						.filter(p -> !p.getLeft().isEmpty() && !holdingBlacklist.contains(Pair.of(p.getLeft().getItem(), p.getLeft().getMetadata())))
						.map(p -> p.getRight())
						.collect(Collectors.toList());

				// Consume save item (if it exists) from drop list
				if (savingItem.getLeft() != Items.AIR) {
					boolean saved = false;
					for (EntityItem e : saving) {
						if (!e.getItem().isEmpty() && ItemMetaHelper.itemEqualsPair(e.getItem(), savingItem)) {
							ItemStack copy = e.getItem().copy();
							copy.shrink(1);
							e.setItem(copy);

							saved = true;
							break;
						}
					}

					if (!saved) return;

					// Prune saving list
					saving.removeIf(e -> e.getItem().isEmpty());
				}
				
				saving.stream()
						.map(EntityItem::getItem)
						.forEach(totem::addItem);
				
				if (!player.world.isRemote)
					player.world.spawnEntity(totem);
				
				persistent.setString(TAG_LAST_TOTEM, totem.getUniqueID().toString());
				
				drops.removeAll(saving);
			} else persistent.setString(TAG_LAST_TOTEM, "");
			
			BlockPos pos = player.getPosition();
			persistent.setInteger(TAG_DEATH_X, pos.getX());
			persistent.setInteger(TAG_DEATH_Z, pos.getZ());
			persistent.setInteger(TAG_DEATH_DIM, player.world.provider.getDimension());
			
			if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
				data.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistent);
		}
	}
	
	public static String getTotemUUID(EntityPlayer player) {
		NBTTagCompound cmp = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		if (cmp.hasKey(TAG_LAST_TOTEM))
			return cmp.getString(TAG_LAST_TOTEM);
		
		return "";
	}
	
	public static BlockPos getPlayerDeathPosition(Entity e) {
		if (e instanceof EntityPlayer) {
			NBTTagCompound cmp = e.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			if (cmp.hasKey(TAG_LAST_TOTEM)) {
				int x = cmp.getInteger(TAG_DEATH_X);
				int z = cmp.getInteger(TAG_DEATH_Z);
				int dim = cmp.getInteger(TAG_DEATH_DIM);
				return new BlockPos(x, dim, z);
			}
		}
		
		return new BlockPos(0, -1, 0);
	}

	@SideOnly(Side.CLIENT)
	public static boolean isOutlineActive(Entity entity, Entity viewer) {
		return glowRange > 0 && entity instanceof EntityTotemOfHolding
				&& viewer.getDistanceSq(entity) <= glowRange * glowRange;
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
