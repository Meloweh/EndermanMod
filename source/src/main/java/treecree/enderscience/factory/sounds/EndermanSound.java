package treecree.enderscience.factory.sounds;

import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treecree.enderscience.api.abilities.ISound;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.effects.Effects;
import treecree.enderscience.entity.SoundHandler;
import treecree.enderscience.references.MorphReference;
import treecree.enderscience.references.ResourceReference;

public class EndermanSound implements ISound {

	@Override
	public void update(EntityLivingBase target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMorph(EntityLivingBase target) {
		//MinecraftForge.EVENT_BUS.unregister(this);
		//MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onDemorph(EntityLivingBase target) {
		//MinecraftForge.EVENT_BUS.unregister(this);
	}

	private static final float BIG_DAMAGE_AMOUNT = 10;
	
	@SubscribeEvent
	public void onPlayerHurt(LivingHurtEvent event) {
		
		if(!(event.getEntity() instanceof EntityPlayer) || event.getEntity().world.isRemote) {
			return;
		}

		final EntityPlayer player = (EntityPlayer) event.getEntity();
		
		IMorphing cap = Morphing.get(player);
		
		if(!cap.isMorphed() || !ResourceReference.getResourceString(cap.getCurrentMorph().resource).equals(MorphReference.ENDERMAN_RESOURCE_STRING)) {
			return;
		}
		
		final float health = player.getHealth();
		
		final float damage = event.getAmount();
		
		if(health - damage <= 0) {
			Effects.playSoundEffectServer(player.world, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_DEATH, SoundCategory.AMBIENT, 1.0f);
		} else if(damage >= BIG_DAMAGE_AMOUNT) {
			Effects.playSoundEffectServer(player.world, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_SCREAM, SoundCategory.AMBIENT, 1.0f);
		} else {
			Effects.playSoundEffectServer(player.world, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_HURT, SoundCategory.AMBIENT, 1.0f);
			//Effects.playSoundEffectServer(player.world, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.AMBIENT, 1.0f);
		}
	}
}