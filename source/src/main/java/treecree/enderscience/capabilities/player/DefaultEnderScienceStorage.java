package treecree.enderscience.capabilities.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import treecree.enderscience.references.PlayerDefaultsReferences;

public class DefaultEnderScienceStorage <T extends IEnderScienceCap> implements Capability.IStorage<T>{

	@Override
	public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
		if((instance instanceof IEnderScienceCap) == false) {
			throw new RuntimeException(instance.getClass().getName() + "does not implement ITeleportCapability");
		}
		
		NBTTagCompound nbt = new NBTTagCompound();
		IEnderScienceCap cap = (IEnderScienceCap) instance;
		nbt.setInteger(PlayerDefaultsReferences.multiVariableHolder, cap.getFlags());
		nbt.setInteger(PlayerDefaultsReferences.potionDuration, cap.getPotionDuration());
		
		return nbt;
	}

	@Override
	public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
		if((instance instanceof IEnderScienceCap) == false) {
			throw new RuntimeException(instance.getClass().getName() + "does not implement ITeleportCapability");
		}
		
		IEnderScienceCap cap = capability.cast(instance);
		cap.setFlags(((NBTTagCompound) nbt).getInteger(PlayerDefaultsReferences.multiVariableHolder));
		cap.setPotionDuration(((NBTTagCompound) nbt).getInteger(PlayerDefaultsReferences.potionDuration));
	}
}
