package treecree.enderscience.capabilities.morphing;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.client.gui.elements.GuiSurvivalMorphs;

/**
 * Morphing interface
 *
 * This interface is responsible for morphing. See {@link Morphing} class for
 * default implementation.
 */
public interface IMorphing
{
    public static final float REASONABLE_HEALTH_VALUE = Float.MIN_VALUE * 100;

    /**
     * Whether this morph is in the process of animation 
     */
    public boolean isAnimating();

    /**
     * Get animation tick
     */
    @SideOnly(Side.CLIENT)
    public int getAnimation();

    /**
     * Get previous animation morph 
     */
    @SideOnly(Side.CLIENT)
    public AbstractMorph getPreviousMorph();

    /**
     * Render player as a morph
     */
    @SideOnly(Side.CLIENT)
    public boolean renderPlayer(EntityPlayer player, double x, double y, double z, float yaw, float partialTick);
    
    /**
     * Check the last damage source received by the
     * player. (This value is volatile and not stored)
     */
    public DamageSource getLastDamageSource();
    
    /**
     * Record the last damage source received by the
     * player. (This value is volatile and not stored)
     */
    public void setLastDamageSource(DamageSource damageSource);

    /**
     * Add a morph
     */
    public boolean acquireMorph(AbstractMorph morph);

    /**
     * Check if this capability has acquired a morph
     */
    public boolean acquiredMorph(AbstractMorph morph);

    /**
     * Get all acquired morph
     */
    public List<AbstractMorph> getAcquiredMorphs();

    /**
     * Set acquired morph
     */
    public void setAcquiredMorphs(List<AbstractMorph> morphs);

    /**
     * Get current morph 
     */
    public AbstractMorph getCurrentMorph();

    /**
     * Set current morph
     */
    public boolean setCurrentMorph(AbstractMorph morph, EntityPlayer player, boolean force);

    /**
     * Demorph this capability 
     */
    public void demorph(EntityPlayer player);

    /**
     * Is this capability is morphed at all 
     */
    public boolean isMorphed();

    /**
     * Remove a morph at given index
     */
    public boolean remove(int index);
    
    public boolean remove(AbstractMorph morph);

    /**
     * Copy data from other morph 
     */
    public void copy(IMorphing morphing, EntityPlayer player);

    /**
     * Get the last recorded finite health fraction of the player
     */
    public float getLastHealthRatio();

    /**
     * Determines what the player's new health will be if the player morphs out of a morph with very low health
     */
    public void setLastHealthRatio(float lastHealthRatio);
    
    /**
     * Gets whether the player is in a morph which drowns on hand due to the Swim ability
     */
    public boolean getHasSquidAir();
    
    /**
     * Sets whether the player is in a morph which drowns on hand due to the Swim ability
     */
    public void setHasSquidAir(boolean hasSquidAir);
    
    /**
     * Gets the air value used when in a morph with the Swim ability
     */
    public int getSquidAir();
    
    /**
     * Sets the air value of a morph in the Swim ability
     */
    public void setSquidAir(int squidAir);

    /**
     * Update the player 
     */
    public void update(EntityPlayer player);

    /**
     * Gets the GUI menu which is responsible for choosing morphs
     */
    @SideOnly(Side.CLIENT)
    public GuiSurvivalMorphs getOverlay();
}