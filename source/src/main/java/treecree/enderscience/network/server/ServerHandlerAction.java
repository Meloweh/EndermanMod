package treecree.enderscience.network.server;

import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import treecree.enderscience.api.events.MorphActionEvent;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.network.common.PacketAction;

public class ServerHandlerAction extends ServerMessageHandler<PacketAction>
{
    @Override
    public void run(EntityPlayerMP player, PacketAction message)
    {
        IMorphing capability = Morphing.get(player);

        if (capability != null && capability.isMorphed())
        {
            AbstractMorph morph = capability.getCurrentMorph();

            morph.action(player);
            MinecraftForge.EVENT_BUS.post(new MorphActionEvent(player, morph.settings.action, morph));
        }
    }
}