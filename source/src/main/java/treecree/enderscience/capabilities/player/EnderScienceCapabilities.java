package treecree.enderscience.capabilities.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import treecree.enderscience.references.References;

public class EnderScienceCapabilities {
	
	public static final ResourceLocation TELEPORT_CAP = new ResourceLocation(References.MOD_ID, "enderScienceCapability");
	
	@CapabilityInject(IEnderScienceCap.class)
	public static Capability<IEnderScienceCap> CAPABILITY_TELEPORT = null;
	
	public static void register() {
		CapabilityManager.INSTANCE.register(IEnderScienceCap.class, new DefaultEnderScienceStorage<>(), () -> new EnderScienceCap());
	}
	
	public static class CapProvider implements ICapabilityProvider, INBTSerializable<NBTBase> {

		private final IEnderScienceCap cap;
		private static final DefaultEnderScienceStorage<IEnderScienceCap> STORAGE = new DefaultEnderScienceStorage<>();
		
		public CapProvider() {
			this.cap = new EnderScienceCap();
		}
		
		@Override
		public NBTBase serializeNBT() {
			return STORAGE.writeNBT(CAPABILITY_TELEPORT, cap, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt) {
			STORAGE.readNBT(CAPABILITY_TELEPORT, cap, null, nbt);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == CAPABILITY_TELEPORT;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == CAPABILITY_TELEPORT) {
				return CAPABILITY_TELEPORT.cast(cap);
			}
			
			return null;
			//throw new IllegalStateException("getCapability called without instance");
		}
		
	}
}
