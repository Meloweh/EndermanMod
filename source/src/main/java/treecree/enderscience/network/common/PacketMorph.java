package treecree.enderscience.network.common;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.morphs.AbstractMorph;

public class PacketMorph implements IMessage
{
    public AbstractMorph morph;

    public PacketMorph()
    {}

    public PacketMorph(AbstractMorph morph)
    {
        this.morph = morph;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        if (buf.readBoolean())
        {
            NBTTagCompound tag = ByteBufUtils.readTag(buf);

            this.morph = MorphManager.INSTANCE.morphFromNBT(tag);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.morph != null);

        if (this.morph != null)
        {
            NBTTagCompound tag = new NBTTagCompound();

            this.morph.toNBT(tag);
            ByteBufUtils.writeTag(buf, tag);
        }
    }
}