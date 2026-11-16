package vazkii.quark.management.capability;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import vazkii.arl.util.AbstractDropIn;
import vazkii.arl.util.ItemNBTHelper;

public class ShulkerBoxDropIn extends AbstractDropIn implements ICapabilityProvider {

	@Override
	public boolean canDropItemIn(EntityPlayer player, ItemStack stack, ItemStack incoming) {
		return tryAddToShulkerBox(player, stack, incoming, true);
	}

	@Override
	public ItemStack dropItemIn(EntityPlayer player, ItemStack stack, ItemStack incoming) {
		tryAddToShulkerBox(player, stack, incoming, false);
		return stack;
	}

	private boolean tryAddToShulkerBox(EntityPlayer player, ItemStack shulkerBox, ItemStack stack, boolean simulate) {
		if (stack.getItem() instanceof ItemShulkerBox || shulkerBox.getCount() > 1)
			return false;

		TileEntity tile = createTile(player.world, shulkerBox);
		if (tile == null) return false;

		NBTTagCompound stackTag = shulkerBox.getTagCompound();
		NBTTagCompound blockEntityTag = (stackTag != null && stackTag.hasKey("BlockEntityTag"))
			? stackTag.getCompoundTag("BlockEntityTag")
			: new NBTTagCompound();

		tile.readFromNBT(blockEntityTag);

		IItemHandler handler = getItemHandler(tile);
		if (handler == null) return false;

		ItemStack result = ItemHandlerHelper.insertItem(handler, stack, simulate);
		boolean did = result.isEmpty();

		if (!simulate && did) {
			tile.writeToNBT(blockEntityTag);
			ItemNBTHelper.setCompound(shulkerBox, "BlockEntityTag", blockEntityTag);
		}

		return did;
	}

	@SuppressWarnings("deprecation")
	private static TileEntity createTile(World world, ItemStack stack) {
		Block block = Block.getBlockFromItem(stack.getItem());
		IBlockState state = block.getStateFromMeta(stack.getMetadata());
		if (!block.hasTileEntity(state))
			return null;

		TileEntity tile = block.createTileEntity(world, state);
		if (tile != null)
			tile.setWorld(world);
		return tile;
	}

	private static IItemHandler getItemHandler(TileEntity tile) {
		if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if (handler != null)
				return handler;
		}
		return tile instanceof IInventory ? new InvWrapper((IInventory) tile) : null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == AbstractDropIn.DROP_IN_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == AbstractDropIn.DROP_IN_CAPABILITY ? (T) this : null;
	}
}
