package treecree.enderscience.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import treecree.enderscience.EnderScience;
import treecree.enderscience.api.EntityUtils;
import treecree.enderscience.api.MorphAPI;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.capabilities.morphing.MorphingProvider;
import treecree.enderscience.network.Dispatcher;
import treecree.enderscience.network.common.PacketAcquiredMorphs;
import treecree.enderscience.network.common.PacketMorph;
import treecree.enderscience.network.common.PacketMorphPlayer;
import treecree.enderscience.network.common.PacketMorphState;
import treecree.enderscience.network.common.PacketSettings;
import treecree.enderscience.references.References;

/**
 * Capability handler class
 *
 * This class is responsible for managing capabilities, i.e. attaching
 * capabilities and syncing values on the client.
 */
public class CapabilityHandler
{
    public static final ResourceLocation MORPHING_CAP = new ResourceLocation(References.MOD_ID, References.MOD_ID + "_capability");

    /**
     * Attach capabilities (well, only one, right now)
     */
    @SubscribeEvent
    @SuppressWarnings(value = {"deprecation"})
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (!(event.getObject() instanceof EntityPlayer)) return;

        event.addCapability(MORPHING_CAP, new MorphingProvider());
    }

    /**
     * When player logs in, sent him his server counter partner's values.
     */
    @SubscribeEvent
    public void playerLogsIn(PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;
        IMorphing cap = Morphing.get(player);

        if (cap != null)
        {
            this.sendAcquiredMorphs(cap, player);

            /* Ensure that player was morphed */
            if (cap.isMorphed())
            {
                cap.getCurrentMorph().morph(player);
            }

            /* Send data */
            Dispatcher.sendTo(new PacketSettings(MorphManager.INSTANCE.activeSettings), (EntityPlayerMP) player);
            Dispatcher.sendTo(new PacketMorphState(player, cap), (EntityPlayerMP) player);
        }
    }

    /**
     * When player starts tracking another player, server has to send its
     * morphing values.
     */
    @SubscribeEvent
    public void playerStartsTracking(StartTracking event)
    {
        if (event.getTarget() instanceof EntityPlayer)
        {
            Entity target = event.getTarget();
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
            IMorphing cap = target.getCapability(MorphingProvider.MORPHING_CAP, null);

            Dispatcher.sendTo(new PacketMorphPlayer(target.getEntityId(), cap.getCurrentMorph()), player);
        }
    }

    /**
     * On player's spawn in the world (when player travels in other dimension 
     * and spawns there or when player dies and then respawns).
     * 
     * This method is responsible for sending morphing data on the client.
     */
    @SubscribeEvent
    public void onPlayerSpawn(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            if (!player.world.isRemote)
            {
                IMorphing morphing = Morphing.get(player);

                final AbstractMorph morph = morphing.getCurrentMorph();

                if(morph != null) {
	                if(morph.resource.getResourceDomain().isEmpty() || morph.resource.getResourcePath().isEmpty()) {
	                	morphing.demorph(player);
	                	morphing.remove(morph);
	                }
                }
                this.sendAcquiredMorphs(morphing, player);
                Dispatcher.sendTo(new PacketMorphState(player, morphing), (EntityPlayerMP) player);
            }
        }
    }

    /**
     * Copy data from dead player (or player returning from end) to the new player
     */
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event)
    {
        EntityPlayer player = event.getEntityPlayer();
        IMorphing morphing = Morphing.get(player);
        IMorphing oldMorphing = Morphing.get(event.getOriginal());

        if (/*EnderMod.proxy.config.keep_morphs || */!event.isWasDeath())
        {
            morphing.copy(oldMorphing, player);
            MorphAPI.demorph(player);
        }
    }

    /**
     * Send acquired morphs (and currently morphed morph) to the given player. 
     */
    private void sendAcquiredMorphs(IMorphing cap, EntityPlayer player)
    {
        EntityPlayerMP mp = (EntityPlayerMP) player;

        Dispatcher.sendTo(new PacketMorph(cap.getCurrentMorph()), mp);
        Dispatcher.sendTo(new PacketAcquiredMorphs(cap.getAcquiredMorphs()), mp);
    }
}