package treecree.enderscience.network.client;

import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.network.common.PacketPotionEffects;
import treecree.enderscience.potions.init.PotionsInit;

public class ClientHandlerPotionEffects extends ClientMessageHandler<PacketPotionEffects>{

	@Override
	@SideOnly(Side.CLIENT)
	public void run(EntityPlayerSP player, PacketPotionEffects message) {
		
		if(player.isPotionActive(message.effect.getPotion())) {
			player.removeActivePotionEffect(message.effect.getPotion());
		}
		
		final PotionEffect effect = new PotionEffect(message.effect.getPotion(), message.effect.getDuration(), message.effect.getAmplifier(), message.effect.getIsAmbient(), message.effect.doesShowParticles());
		player.addPotionEffect(effect);
	}
}
