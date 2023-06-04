package treecree.enderscience.util;

import treecree.enderscience.EnderScience;
import treecree.enderscience.capabilities.player.CapabilityFlags;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.effects.Effects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TeleportEntity {
	
	private final static int UNUSED_CONTAINER_FLAG = 0;

	public static final boolean teleportRandomly(EntityLivingBase entity) {
		
		if(entity == null) {
			return false;
		}

        for (int i = 0; i < 16; ++i) {
            double d3 = entity.posX + (entity.getRNG().nextDouble() - 0.5D) * 16.0D;
            double d4 = MathHelper.clamp(entity.posY + (double)(entity.getRNG().nextInt(16) - 8), 0.0D, (double)(entity.world.getActualHeight() - 1));
            double d5 = entity.posZ + (entity.getRNG().nextDouble() - 0.5D) * 16.0D;

            if (entity.isRiding()) {
                entity.dismountRidingEntity();
            }

            if (entity.attemptTeleport(d3, d4, d5)) {
            	
            	Effects.spawnParticlesAndTeleportSoundFromServer(entity.dimension, entity, 20, 2, 0);
            	
            	return true;
            }
        }
        
        for (int i = 0; i < 16; ++i) {
            double d3 = entity.posX + (entity.getRNG().nextDouble() - 0.5D) * 64.0D;
            double d4 = MathHelper.clamp(entity.posY + (double)(entity.getRNG().nextInt(64) - 32), 0.0D, (double)(entity.world.getActualHeight() - 1));
            double d5 = entity.posZ + (entity.getRNG().nextDouble() - 0.5D) * 64.0D;

            if (entity.isRiding()) {
                entity.dismountRidingEntity();
            }

            if (entity.attemptTeleport(d3, d4, d5)) {
            	
            	Effects.spawnParticlesAndTeleportSoundFromServer(entity.dimension, entity, 20, 2, 0);
            	
                return true;
            }
        }
        return false;
	}
	
	public static final boolean teleportRandomly(final EntityLivingBase sourceEntity, final EntityLivingBase targetEntity) {
		
		if(sourceEntity == null || targetEntity == null) {
			return false;
		}

        for (int i = 0; i < 16; ++i) {
            double d3 = targetEntity.posX + (targetEntity.getRNG().nextDouble() - 0.5D) * 16.0D;
            double d4 = MathHelper.clamp(targetEntity.posY + (double)(targetEntity.getRNG().nextInt(16) - 8), 0.0D, (double)(targetEntity.world.getActualHeight() - 1));
            double d5 = targetEntity.posZ + (targetEntity.getRNG().nextDouble() - 0.5D) * 16.0D;

            if (sourceEntity.isRiding()) {
            	sourceEntity.dismountRidingEntity();
            }

            if (sourceEntity.attemptTeleport(d3, d4, d5)) {
            	
            	Effects.spawnParticlesAndTeleportSoundFromServer(sourceEntity.dimension, sourceEntity, 20, 2, 0);
            	
                return true;
            }
        }
        
        for (int i = 0; i < 16; ++i) {
            double d3 = targetEntity.posX + (targetEntity.getRNG().nextDouble() - 0.5D) * 64.0D;
            double d4 = MathHelper.clamp(targetEntity.posY + (double)(targetEntity.getRNG().nextInt(64) - 32), 0.0D, (double)(targetEntity.world.getActualHeight() - 1));
            double d5 = targetEntity.posZ + (targetEntity.getRNG().nextDouble() - 0.5D) * 64.0D;

            if (sourceEntity.isRiding()) {
            	sourceEntity.dismountRidingEntity();
            }

            if (sourceEntity.attemptTeleport(d3, d4, d5)) {

            	Effects.spawnParticlesAndTeleportSoundFromServer(sourceEntity.dimension, sourceEntity, 20, 2, 0);
            	
                return true;
            }
        }
        return false;
	}
	
	public static final void teleportToBed(final EntityPlayer player) {
		
		if(player == null) {
			return;
		}
		
		final int overworld = 0;
		
		final BlockPos blockPos = player.getBedLocation(overworld);
		
		if(blockPos != null) {
			
	        if (player.isRiding()) {
	        	player.dismountRidingEntity();
	        }
	        
			if(player.dimension != overworld) {
				player.changeDimension(overworld);
			}
			
			player.setPositionAndUpdate(blockPos.getX() + 0.5f, blockPos.getY() + 1.0f, blockPos.getZ() + 0.5f);
	        Effects.spawnParticlesAndTeleportSoundFromServer(player.dimension, player, 20, 2, 0);
	        
		} else {
			final ITextComponent component = new TextComponentString("You have no bed to teleport to");
			player.sendMessage(component);
		}
	}
}
