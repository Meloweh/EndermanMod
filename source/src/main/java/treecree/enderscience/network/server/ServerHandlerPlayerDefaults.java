package treecree.enderscience.network.server;

import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.capabilities.player.IEnderScienceCap;
import treecree.enderscience.network.common.PacketPlayerDefaults;

public class ServerHandlerPlayerDefaults extends ServerMessageHandler<PacketPlayerDefaults>{

	@Override
	public void run(EntityPlayerMP player, PacketPlayerDefaults message) {
		if(player.hasCapability(EnderScienceCapabilities.CAPABILITY_TELEPORT, null)) {
			
			final IEnderScienceCap cap = player.getCapability(EnderScienceCapabilities.CAPABILITY_TELEPORT, null);
			
			cap.setFlags(message.cap.getFlags());
		}
	}

}
