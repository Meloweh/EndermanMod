package treecree.enderscience.network.server;

import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import treecree.enderscience.api.MorphAPI;
import treecree.enderscience.network.common.PacketMorph;

public class ServerHandlerMorph extends ServerMessageHandler<PacketMorph> {
	@Override
	public void run(EntityPlayerMP player, PacketMorph message) {
		if (player.isCreative()) {
			MorphAPI.morph(player, message.morph, false);
		}
	}
}