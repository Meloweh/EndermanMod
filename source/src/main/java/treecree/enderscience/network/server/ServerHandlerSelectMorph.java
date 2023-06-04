package treecree.enderscience.network.server;

import java.util.List;

import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.GameType;
import treecree.enderscience.api.MorphAPI;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.network.common.PacketSelectMorph;

public class ServerHandlerSelectMorph extends ServerMessageHandler<PacketSelectMorph>
{
    @Override
    public void run(EntityPlayerMP player, PacketSelectMorph message)
    {
        if (player.interactionManager.getGameType() == GameType.ADVENTURE)
        {
            return;
        }

        int index = message.index;

        IMorphing capability = Morphing.get(player);
        List<AbstractMorph> morphs = capability.getAcquiredMorphs();
        AbstractMorph morph = null;

        if (!morphs.isEmpty() && index >= 0 && index < morphs.size())
        {
            morph = morphs.get(index);
        }

        MorphAPI.morph(player, morph == null ? morph : morph.clone(false), false);
    }
}