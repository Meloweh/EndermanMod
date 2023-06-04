package treecree.enderscience.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities.CapProvider;

public class PlayerEventHandler {

	@SubscribeEvent
	public void onAttachCapabilityEntityPlayer(AttachCapabilitiesEvent<Entity> event) {

		final Entity entity = event.getObject();

		if (entity instanceof EntityPlayer) {
			
			event.addCapability(EnderScienceCapabilities.TELEPORT_CAP, new CapProvider());
		}
	}
}
