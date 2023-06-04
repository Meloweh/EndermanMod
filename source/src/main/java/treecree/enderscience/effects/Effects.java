package treecree.enderscience.effects;

import treecree.enderscience.EnderScience;
import treecree.enderscience.network.Dispatcher;
import treecree.enderscience.network.common.PacketAddEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class Effects {
	private static final Double TARGET_RADIUS = 24.0d;
	
	public static void spawnParticles(World world, EnumParticleTypes type, double x, double y, double z, int count,
			double offset, double velocity) {
		
		for (int i = 0; i < count; i++) {
			double offX = (world.rand.nextFloat() - 0.5d) * offset;
			double offY = (world.rand.nextFloat() - 0.5d) * offset;
			double offZ = (world.rand.nextFloat() - 0.5d) * offset;

			double velX = (world.rand.nextFloat() - 0.5d) * velocity;
			double velY = (world.rand.nextFloat() - 0.5d) * velocity;
			double velZ = (world.rand.nextFloat() - 0.5d) * velocity;
			world.spawnParticle(type, x + offX, y + offY, z + offZ, -velX, -velY, -velZ);
		}
	}

    public static void spawnParticlesAround(World world, EnumParticleTypes type, double x, double y, double z, int count) {	
        for (int i = 0; i < count; ++i) {
            world.spawnParticle(
            		type, 
            		x + (world.rand.nextDouble() - 0.5D) * 0.5D, 
            		y + world.rand.nextDouble() * 2.0D - 0.25D, 
            		z + (world.rand.nextDouble() - 0.5D) * 0.5D, 
            		(world.rand.nextDouble() - 0.5D) * 2.0D, 
            		-world.rand.nextDouble(), 
            		(world.rand.nextDouble() - 0.5D) * 2.0D);
        }
    }
	
    public static void playSoundEffectServer(World world, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume) {
        playSoundEffectServer(world, x, y, z, soundIn, category, volume, 1.0f + (world.rand.nextFloat() * 0.5f - world.rand.nextFloat() * 0.5f) * 0.5f);
    }

    public static void playSoundEffectServer(World world, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
        world.playSound(null, x, y, z, soundIn, category, volume, pitch);
    }
    
	public static void playSoundClient(World world, double x, double y, double z, SoundEvent soundIn, SoundCategory category, 
			float volume, float pitch) {
		world.playSound(x, y, z, soundIn, category, volume, pitch, false);
	}
	
	public static void spawnParticlesAroundFromServer(int dimension, EntityPlayer player, EnumParticleTypes particle, int count/*, float offset, float velocity*/) {
		final double x = player.posX;
		final double y = player.posY;
		final double z = player.posZ;
		
		//EnderScience.simple_channel.sendToAllAround(new MessageAddEffects(MessageAddEffects.EFFECT_PARTICLES, particle.getParticleID(), x, y, z),  
		//		new NetworkRegistry.TargetPoint(dimension, x, y, z, TARGET_RADIUS));
		
		Dispatcher.sendToAllAround(new PacketAddEffects(PacketAddEffects.EFFECT_PARTICLES, particle.getParticleID(), x, y, z), new NetworkRegistry.TargetPoint(dimension, x, y, z, TARGET_RADIUS));
	}
	
	public static void spawnParticlesAndTeleportSoundFromServer(int dimension, EntityLivingBase entity, int count, float offset, float velocity) {
		final double x = entity.posX;
		final double y = entity.posY + entity.height - 1.0d;
		final double z = entity.posZ;

//		EnderScience.simple_channel.sendToAllAround(new MessageAddEffects(MessageAddEffects.EFFECT_TELEPORT, MessageAddEffects.PARTICLES | MessageAddEffects.SOUND, x, y, z), 
//				new NetworkRegistry.TargetPoint(dimension, x, y, z, TARGET_RADIUS));
		
		Dispatcher.sendToAllAround(new PacketAddEffects(PacketAddEffects.EFFECT_TELEPORT, PacketAddEffects.PARTICLES | PacketAddEffects.SOUND, x, y, z), 
				new NetworkRegistry.TargetPoint(dimension, x, y, z, TARGET_RADIUS));
	}
	/*
	public static void spawnParticlesAndTeleportSoundFromServer(int dimension, double x, double y, double z, int count, float offset, float velocity) {
		
//		EnderScience.simple_channel.sendToAllAround(new MessageAddEffects(MessageAddEffects.EFFECT_TELEPORT, MessageAddEffects.PARTICLES | MessageAddEffects.SOUND, x, y, z), 
//				new NetworkRegistry.TargetPoint(dimension, x, y, z, TARGET_RADIUS));
		Dispatcher.sendToAllAround(new PacketAddEffects(PacketAddEffects.EFFECT_TELEPORT, MessageAddEffects.PARTICLES | MessageAddEffects.SOUND, x, y, z), 
				new NetworkRegistry.TargetPoint(dimension, x, y, z, TARGET_RADIUS));
	}*/
}
