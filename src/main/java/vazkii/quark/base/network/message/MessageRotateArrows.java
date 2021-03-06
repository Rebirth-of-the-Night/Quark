/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [31/03/2016, 02:55:00 (GMT)]
 */
package vazkii.quark.base.network.message;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBow;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.arl.network.NetworkMessage;
import vazkii.quark.management.feature.RotateArrowTypes;

public class MessageRotateArrows extends NetworkMessage<MessageRotateArrows> {

    private static final long serialVersionUID = -5186835494497578682L;
    public int direction;

	public MessageRotateArrows() { }

	public MessageRotateArrows(int direction) {
		this.direction = direction;
	}

	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayerMP player = context.getServerHandler().player;
		player.getServerWorld().addScheduledTask(() -> {
			ItemBow bowItem = RotateArrowTypes.getHeldBow(player);
			if (bowItem != null)
				RotateArrowTypes.rotateArrows(bowItem, player, direction);
		});
		return null;
	}

}
