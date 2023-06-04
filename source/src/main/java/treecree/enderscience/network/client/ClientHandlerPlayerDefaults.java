package treecree.enderscience.network.client;

import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.capabilities.player.IEnderScienceCap;
import treecree.enderscience.network.common.PacketPlayerDefaults;
import treecree.enderscience.proxy.ClientProxy;

public class ClientHandlerPlayerDefaults extends ClientMessageHandler<PacketPlayerDefaults>{

	@Override
    @SideOnly(Side.CLIENT)
	public void run(EntityPlayerSP player, PacketPlayerDefaults message) {
		
		if(player.hasCapability(EnderScienceCapabilities.CAPABILITY_TELEPORT, null)) {
		
			IEnderScienceCap cap = player.getCapability(EnderScienceCapabilities.CAPABILITY_TELEPORT, null);

			cap.setFlags(message.cap.getFlags());
		}
	}
}
