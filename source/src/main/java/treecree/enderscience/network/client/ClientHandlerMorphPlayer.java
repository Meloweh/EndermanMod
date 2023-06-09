package treecree.enderscience.network.client;

import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.MorphingProvider;
import treecree.enderscience.network.common.PacketMorphPlayer;

public class ClientHandlerMorphPlayer extends ClientMessageHandler<PacketMorphPlayer>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketMorphPlayer message)
    {
        Entity entity = player.world.getEntityByID(message.id);
        IMorphing capability = entity.getCapability(MorphingProvider.MORPHING_CAP, null);

        if (capability != null)
        {
            capability.setCurrentMorph(message.morph, (EntityPlayer) entity, true);
        }
    }
}
