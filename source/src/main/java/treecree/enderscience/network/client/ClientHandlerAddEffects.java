package treecree.enderscience.network.client;

import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.EnderScience;
import treecree.enderscience.effects.Effects;
import treecree.enderscience.network.common.PacketAddEffects;

public class ClientHandlerAddEffects extends ClientMessageHandler<PacketAddEffects>{

	@Override
    @SideOnly(Side.CLIENT)
	public void run(EntityPlayerSP player, PacketAddEffects message) {
		
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (mc == null) {
			final String errorString = "Minecraft was null in ClientHandlerAddEffects";
			EnderScience.logger.error(errorString);
			return;
		}
		
		processMessage(message, player, player.world, mc.getSoundHandler());
		
		
	}

	protected void processMessage(final PacketAddEffects message, EntityPlayer player, World world, SoundHandler soundHandler) {
		if (message.effectType == PacketAddEffects.EFFECT_TELEPORT) {
			if ((message.flags & PacketAddEffects.SOUND) == PacketAddEffects.SOUND) {
				float pitch = 0.9f + world.rand.nextFloat() * 0.125f + world.rand.nextFloat() * 0.125f;
				Effects.playSoundClient(world, message.x, message.y, message.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.HOSTILE, 0.8f, pitch);
			}
			if ((message.flags & PacketAddEffects.PARTICLES) == PacketAddEffects.PARTICLES) {
				Effects.spawnParticles(world, EnumParticleTypes.PORTAL, message.x, message.y, message.z, message.particleCount, message.offset, message.velocity);
			}
		} else if (message.effectType == PacketAddEffects.EFFECT_PARTICLES) {
			Effects.spawnParticlesAround(world, EnumParticleTypes.getParticleFromId(message.flags), message.x, message.y, message.z, message.particleCount);
		} else if (message.effectType == PacketAddEffects.EFFECT_SOUND_EVENT) {
			EnderScience.proxy.playSound(message.soundEventId, message.pitch, message.volume, message.repeat, message.flags != 0, message.x, message.y, message.z);
		}
	}
}
