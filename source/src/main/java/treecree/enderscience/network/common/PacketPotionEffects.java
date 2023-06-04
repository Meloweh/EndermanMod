package treecree.enderscience.network.common;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.capabilities.player.IEnderScienceCap;
import treecree.enderscience.capabilities.player.EnderScienceCap;
import treecree.enderscience.potions.init.PotionsInit;
import treecree.enderscience.references.PlayerDefaultsReferences;

public class PacketPotionEffects implements IMessage {
	public PotionEffect effect;

	public PacketPotionEffects() {
	}

	public PacketPotionEffects(final PotionEffect effect) {
		this.effect = effect;

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		if (buf.readBoolean()) {
			final NBTTagCompound tag = ByteBufUtils.readTag(buf);
			final byte id = tag.getByte("Id");
			final int amplifier = tag.getInteger("Amplifier");
			final int duration = tag.getInteger("Duration");
			final boolean isAmbient = tag.getBoolean("Ambient");
			final boolean showParticles = tag.getBoolean("ShowParticles");

			this.effect = new PotionEffect(Potion.getPotionById(id), duration, amplifier, isAmbient, showParticles);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.effect != null);

		if (this.effect != null) {
			NBTTagCompound tag = new NBTTagCompound();

			tag.setByte("Id", (byte) Potion.getIdFromPotion(this.effect.getPotion()));
			tag.setInteger("Amplifier", this.effect.getAmplifier());
			tag.setInteger("Duration", this.effect.getDuration());
			tag.setBoolean("Ambient", this.effect.getIsAmbient());
			tag.setBoolean("ShowParticles", this.effect.doesShowParticles());

			ByteBufUtils.writeTag(buf, tag);
		}
	}
}
