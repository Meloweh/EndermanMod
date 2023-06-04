package treecree.enderscience.factory.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import treecree.enderscience.api.abilities.Ability;
import treecree.enderscience.util.TeleportEntity;

public class WaterAllgergy extends Ability {
	@Override
	public void update(EntityLivingBase target) {
		if (target.isWet()) {
			target.attackEntityFrom(DamageSource.DROWN, 1.0F);
		}
	}
}
