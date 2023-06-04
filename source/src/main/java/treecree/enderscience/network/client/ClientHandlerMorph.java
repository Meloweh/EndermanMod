package treecree.enderscience.network.client;

import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.capabilities.CapabilityHandler;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.network.common.PacketMorph;
import treecree.enderscience.proxy.ClientProxy;

public class ClientHandlerMorph extends ClientMessageHandler<PacketMorph> {
	@Override
	@SideOnly(Side.CLIENT)
	public void run(EntityPlayerSP player, PacketMorph message) {
		IMorphing capability = Morphing.get(player);

		if (capability != null) {
			capability.setCurrentMorph(message.morph, player, true);

			// ClientProxy.morphCountdownOverlay.add(message.morph);
			if (message.morph != null) {
				ClientProxy.morphOverlay2.start(message.morph);
			}
		}
	}
}