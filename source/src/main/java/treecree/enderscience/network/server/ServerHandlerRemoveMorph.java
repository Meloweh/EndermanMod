package treecree.enderscience.network.server;

import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.network.Dispatcher;
import treecree.enderscience.network.common.PacketRemoveMorph;

public class ServerHandlerRemoveMorph extends ServerMessageHandler<PacketRemoveMorph>
{
    @Override
    public void run(EntityPlayerMP player, PacketRemoveMorph message)
    {
        if (Morphing.get(player).remove(message.index))
        {
            Dispatcher.sendTo(message, player);
        }
    }
}