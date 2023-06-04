package treecree.enderscience.network.server;

import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import treecree.enderscience.api.MorphAPI;
import treecree.enderscience.network.common.PacketAcquireMorph;

/**
 * Server handler acquire morph
 * 
 * This handler is responsible for sending acquired morph for players in 
 * creative morph.
 */
public class ServerHandlerAcquireMorph extends ServerMessageHandler<PacketAcquireMorph>
{
    @Override
    public void run(EntityPlayerMP player, PacketAcquireMorph message)
    {
        if (player.isCreative() || player.isSpectator())
        {
            MorphAPI.acquire(player, message.morph);
        }
    }
}