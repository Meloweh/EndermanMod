package treecree.enderscience.capabilities.player;

import net.minecraft.nbt.NBTTagCompound;
import treecree.enderscience.references.PlayerDefaultsReferences;

public interface IEnderScienceCap {
	int getFlags();
	
	void setFlags(final int flags);
	
	int getPotionDuration();
	
	void setPotionDuration(int duration);
}
