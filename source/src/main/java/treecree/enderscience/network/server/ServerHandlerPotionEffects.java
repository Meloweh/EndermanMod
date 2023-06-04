package treecree.enderscience.network.server;

import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import treecree.enderscience.network.common.PacketPotionEffects;

public class ServerHandlerPotionEffects  extends ServerMessageHandler<PacketPotionEffects>{

	@Override
	public void run(EntityPlayerMP player, PacketPotionEffects message) {
		
		if(player.isPotionActive(message.effect.getPotion())) {
			player.removeActivePotionEffect(message.effect.getPotion());
		}
		
		final PotionEffect effect = new PotionEffect(message.effect.getPotion(), message.effect.getDuration(), message.effect.getAmplifier(), message.effect.getIsAmbient(), message.effect.doesShowParticles());
		player.addPotionEffect(effect);
	}

}
