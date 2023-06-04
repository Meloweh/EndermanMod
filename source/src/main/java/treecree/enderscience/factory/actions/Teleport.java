package treecree.enderscience.factory.actions;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import treecree.enderscience.EnderScience;
import treecree.enderscience.api.abilities.IAction;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.capabilities.player.CapabilityFlags;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.capabilities.player.IEnderScienceCap;
import treecree.enderscience.util.TeleportEntity;

/**
 * Teleport action
 * 
 * This action will teleport given player there where he's looking.
 * 
 * If the point where player is about to teleport have a block above, it will
 * teleport player beside the block. Optionally, you can sneak to teleport
 * beside the block.
 * 
 * Teleport action also has cooldown and limited distance to teleport in radius
 * of 32 blocks.
 */
public class Teleport implements IAction {
	@Override
	public void execute(EntityLivingBase target, @Nullable AbstractMorph morph) {
		if (!target.hasCapability(EnderScienceCapabilities.CAPABILITY_TELEPORT, null)) {
			return;
		}

		final IEnderScienceCap cap = target.getCapability(EnderScienceCapabilities.CAPABILITY_TELEPORT, null);
		
		cap.setFlags(CapabilityFlags.setFlagDefaultRandomTeleportTrue(cap.getFlags()));

	}

	@Override
	public String getActionAsString() {
		return "teleport action";
	}
}