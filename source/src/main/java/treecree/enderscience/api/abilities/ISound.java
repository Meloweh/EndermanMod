package treecree.enderscience.api.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundEvent;

public interface ISound {
    public void update(EntityLivingBase target);
    public void onMorph(EntityLivingBase target);
    public void onDemorph(EntityLivingBase target);
}
