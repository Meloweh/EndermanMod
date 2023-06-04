package treecree.enderscience.bodypart;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.capabilities.morphing.IMorphing;

/**
 * Interface for body-part system part
 */
public interface IBodyPart
{
    @SideOnly(Side.CLIENT)
    public void init();

    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, float partialTicks);

    @SideOnly(Side.CLIENT)
    public void update(EntityLivingBase entity, IMorphing cap);

    public boolean canMerge(IBodyPart part, boolean isRemote);

    public void toNBT(NBTTagCompound tag);

    public void fromNBT(NBTTagCompound tag);
}