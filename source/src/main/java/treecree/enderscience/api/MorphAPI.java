package treecree.enderscience.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import treecree.enderscience.api.events.AcquireMorphEvent;
import treecree.enderscience.api.events.MorphEvent;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.network.Dispatcher;
import treecree.enderscience.network.common.PacketAcquireMorph;
import treecree.enderscience.network.common.PacketMorph;
import treecree.enderscience.network.common.PacketMorphPlayer;
import treecree.enderscience.network.common.PacketMorphState;

/**
 * Morph API class
 * 
 * This class provides public API for morphing the player. Let me know which 
 * methods I may add to simplify your life :D
 * 
 * Acquired morphs and favorites are sent only to the owner players. So you 
 * can't access this information from the client side. However, you can hit me 
 * up, and prove me why I should send that information to the other players :D
 * 
 * Use this API on the server side, please. Thanks!
 */
public class MorphAPI
{
    /**
     * Demorph given player 
     */
    public static boolean demorph(EntityPlayer player)
    {
        return morph(player, null, false);
    }

    /**
     * Morph a player into given morph with given force flag. 
     * 
     * @return true, if player was morphed successfully
     */
    public static boolean morph(EntityPlayer player, AbstractMorph morph, boolean force)
    {
        IMorphing morphing = Morphing.get(player);

        if (morphing == null)
        {
            return false;
        }

        MorphEvent.Pre event = new MorphEvent.Pre(player, morph, force);

        if (MinecraftForge.EVENT_BUS.post(event))
        {
            return false;
        }

        boolean morphed = morphing.setCurrentMorph(event.morph, player, event.force);

        if (!player.world.isRemote && morphed)
        {
            Dispatcher.sendTo(new PacketMorph(morph), (EntityPlayerMP) player);
            Dispatcher.sendToTracked(player, new PacketMorphPlayer(player.getEntityId(), morph));
            Dispatcher.sendTo(new PacketMorphState(player, morphing), (EntityPlayerMP) player);
        }

        if (morphed)
        {
            MinecraftForge.EVENT_BUS.post(new MorphEvent.Post(player, event.morph, force));
        }

        return morphed;
    }

    /**
     * Make given player acquire a given morph. Usable on both sides, but it's 
     * better to use it on the server.
     * 
     * @return true, if player has acquired a morph
     */
    public static boolean acquire(EntityPlayer player, AbstractMorph morph)
    {
        if (morph == null)
        {
            return false;
        }

        AcquireMorphEvent.Pre event = new AcquireMorphEvent.Pre(player, morph);

        if (MinecraftForge.EVENT_BUS.post(event))
        {
            return false;
        }

        boolean acquired = Morphing.get(player).acquireMorph(event.morph);

        if (!player.world.isRemote && acquired)
        {
            Dispatcher.sendTo(new PacketAcquireMorph(event.morph), (EntityPlayerMP) player);
        }

        if (acquired)
        {
            MinecraftForge.EVENT_BUS.post(new AcquireMorphEvent.Post(player, event.morph));
        }

        return acquired;
    }
}