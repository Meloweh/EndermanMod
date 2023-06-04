package treecree.enderscience.network.client;

import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.api.morphs.EntityMorph;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.network.common.PacketMorphState;

public class ClientHandlerMorphState extends ClientMessageHandler<PacketMorphState>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketMorphState message)
    {
        IMorphing capability = Morphing.get(player);

        AbstractMorph morph = capability.getCurrentMorph();
        if (morph instanceof EntityMorph)
        {
            Entity entity = ((EntityMorph) morph).getEntity(player.world);
            entity.setEntityId(message.entityID);
        }

        capability.setHasSquidAir(message.hasSquidAir);
        capability.setSquidAir(message.squidAir);
    }
}
