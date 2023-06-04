package treecree.enderscience.factory.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treecree.enderscience.api.abilities.IAbility;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.util.TeleportEntity;

public class LavaDodge implements IAbility {

	@Override
	public void update(EntityLivingBase target) {
	}

	@Override
	public void onMorph(EntityLivingBase target) {
		// MinecraftForge.EVENT_BUS.unregister(this);
		// MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onDemorph(EntityLivingBase target) {
		// MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onLivingAttackEvent(LivingAttackEvent event) {

		if (event.getEntity().world.isRemote) {
			return;
		}

		final Entity entity = event.getEntity();

		if (!(entity instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) entity;
		IMorphing morphing = Morphing.get(player);
		if (morphing == null) {
			return;
		}
		AbstractMorph morph = morphing.getCurrentMorph();
		if (morph == null) {
			return;
		}

		final DamageSource source = event.getSource();

		if (source == DamageSource.LAVA) {

			if (TeleportEntity.teleportRandomly(player)) {

				event.setCanceled(true);
			}
		}
	}
}
