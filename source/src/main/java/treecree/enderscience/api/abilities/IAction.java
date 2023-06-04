package treecree.enderscience.api.abilities;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import treecree.enderscience.api.morphs.AbstractMorph;

/**
 * Action interface
 * 
 * Just like an ability, but cooler. This interface, instead of changing player's 
 * properties, it's actually does some kind of trick.
 */
public interface IAction
{
    /**
     * Execute an action. Depends on action's description, it can teleport 
     * player, emit explosion, or something else.  
     */
    public void execute(EntityLivingBase target, @Nullable AbstractMorph morph);
    
    public String getActionAsString();
}