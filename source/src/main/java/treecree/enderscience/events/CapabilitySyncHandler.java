package treecree.enderscience.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import treecree.enderscience.EnderScience;
import treecree.enderscience.api.MorphAPI;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.capabilities.player.CapabilityFlags;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.capabilities.player.IEnderScienceCap;
import treecree.enderscience.effects.Effects;
import treecree.enderscience.network.Dispatcher;
import treecree.enderscience.network.common.PacketPlayerDefaults;
import treecree.enderscience.potions.init.PotionsInit;
import treecree.enderscience.util.TeleportEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.server.FMLServerHandler;

public class CapabilitySyncHandler {
	
	private final void handleButtons(final EntityPlayerMP player, final List<EntityPlayerMP> playerList, final IEnderScienceCap cap) {
		
		if(CapabilityFlags.isFlagBtnPressing(cap.getFlags())) {
			
			//set no action flag for client, because we don't want to repeat anything on 
			//server/client until buttons are released and actions done
			cap.setFlags(CapabilityFlags.setFlagBtnNoAction(cap.getFlags()));
			
			if (CapabilityFlags.getFlagDefaultRandomTeleportBoolean(cap.getFlags())) {

				TeleportEntity.teleportRandomly(player);
				
				cap.setFlags(CapabilityFlags.setFlagDefaultRandomTeleportFalse(cap.getFlags()));
				
			} else if (CapabilityFlags.getFlagToPlayerTeleportBoolean(cap.getFlags())) {
				
				int randomRange = 0;

				do {
					randomRange = (new Random()).nextInt(playerList.size());
				} while (playerList.get(randomRange) == player);

				TeleportEntity.teleportRandomly(player, playerList.get(randomRange));

				cap.setFlags(CapabilityFlags.setFlagToPlayerTeleportFalse(cap.getFlags()));
				
			} else if(CapabilityFlags.getFlagToBedTeleportBoolean(cap.getFlags())) {

				TeleportEntity.teleportToBed(player);
				
				cap.setFlags(CapabilityFlags.setFlagToBedTeleportFalse(cap.getFlags()));
			}

			Dispatcher.sendTo(new PacketPlayerDefaults(cap), player);
		}  else if (CapabilityFlags.isFlagBtnReleased(cap.getFlags())) {

			cap.setFlags(CapabilityFlags.setFlagBtnNone(cap.getFlags()));
			
			Dispatcher.sendTo(new PacketPlayerDefaults(cap), player);
		}
	}

//	private final void spawnQueuedEffects(final EntityPlayerMP player, final ITeleportCapability cap) {
//		/*if (!CapabilityFlags.getFlagParticleCooldownBoolean(cap.getFlags())) {
//
//			Effects.spawnParticlesAroundFromServer(player.dimension, player, EnumParticleTypes.PORTAL, 2);
//
//			cap.setFlags(CapabilityFlags.setFlagParticleCooldownTrue(cap.getFlags()));
//		}*/
//
//		if (CapabilityFlags.getFlagTeleportEffectBoolean(cap.getFlags())) {
//			
//			Effects.spawnParticlesAndTeleportSoundFromServer(player.dimension, player, 20, 2, 0);
//
//			cap.setFlags(CapabilityFlags.setFlagTeleportEffectFalse(cap.getFlags()));
//		}
//	}
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {

		final List<EntityPlayerMP> playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();

		for (final EntityPlayerMP player : playerList) {

			if (player.hasCapability(EnderScienceCapabilities.CAPABILITY_TELEPORT, null)) {
				
				final IEnderScienceCap cap = player.getCapability(EnderScienceCapabilities.CAPABILITY_TELEPORT, null);

				handleButtons(player, playerList, cap);

				//spawnQueuedEffects(player, cap);
			}
		}
	}
}