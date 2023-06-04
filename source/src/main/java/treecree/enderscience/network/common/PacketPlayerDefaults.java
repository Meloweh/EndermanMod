package treecree.enderscience.network.common;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.capabilities.player.IEnderScienceCap;
import treecree.enderscience.capabilities.player.EnderScienceCap;
import treecree.enderscience.references.PlayerDefaultsReferences;

public class PacketPlayerDefaults implements IMessage {
	public IEnderScienceCap cap;

	public PacketPlayerDefaults() {
	}

	public PacketPlayerDefaults(final IEnderScienceCap cap) {
		this.cap = cap;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		if (buf.readBoolean()) {
			NBTTagCompound tag = ByteBufUtils.readTag(buf);
			
			final IEnderScienceCap cap = new EnderScienceCap();

			cap.setFlags(tag.getInteger(PlayerDefaultsReferences.multiVariableHolder));
			cap.setPotionDuration(tag.getInteger(PlayerDefaultsReferences.potionDuration));
			
			this.cap = cap;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.cap != null);

		if (this.cap != null) {
			NBTTagCompound tag = new NBTTagCompound();
			
			final int flags = this.cap.getFlags();

			tag.setInteger(PlayerDefaultsReferences.multiVariableHolder, this.cap.getFlags());
			tag.setInteger(PlayerDefaultsReferences.potionDuration, this.cap.getPotionDuration());
			
			ByteBufUtils.writeTag(buf, tag);
		}
	}
}
