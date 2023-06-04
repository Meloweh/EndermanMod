package treecree.enderscience.network.common;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.api.morphs.EntityMorph;
import treecree.enderscience.capabilities.morphing.IMorphing;

public class PacketMorphState implements IMessage
{
    public int entityID = 0;
    public boolean hasSquidAir = true;
    public int squidAir = 300;
    
    public PacketMorphState()
    {}
    
    public PacketMorphState(EntityPlayer player, IMorphing morphing)
    {
        if (morphing != null)
        {
            AbstractMorph morph = morphing.getCurrentMorph();
            
            
            if (morph instanceof EntityMorph) {

            	final EntityMorph entityMorph = (EntityMorph)morph;

            	final EntityLivingBase base = entityMorph.getEntity(player.world);
            	
                entityID = base.getEntityId();
            }
            
            this.hasSquidAir = morphing.getHasSquidAir();
            this.squidAir = morphing.getSquidAir();
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityID = buf.readInt();
        this.hasSquidAir = buf.readBoolean();
        this.squidAir = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityID);
        buf.writeBoolean(this.hasSquidAir);
        buf.writeInt(this.squidAir);
    }

}
