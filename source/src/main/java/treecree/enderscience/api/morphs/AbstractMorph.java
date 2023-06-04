package treecree.enderscience.api.morphs;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.EnderScience;
import treecree.enderscience.api.MorphAPI;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.MorphSettings;
import treecree.enderscience.api.abilities.IAbility;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.entity.SoundHandler;
import treecree.enderscience.potions.init.PotionsInit;
import treecree.enderscience.references.MorphReference;
import treecree.enderscience.references.ResourceReference;

/**
 * Base class for all different types of morphs
 * 
 * This is an abstract morph. It contains all needed properties for a basic 
 * morph such as abilities, action, attack, health, speed and hotstyle flag.
 * 
 * This class is also responsible for rendering operations. Oh boy, this class 
 * is so huge. I'll have to decompose this thing onto rendering and logic code.
 */
public abstract class AbstractMorph
{
	//public final static int MORPH_DURATION = 6 * 60;//3 * 60 * 60; //3 * 60 * millies
	
    /* Abilities */

    /**
     * Morph settings 
     */
    public MorphSettings settings = MorphSettings.DEFAULT;

    /* Meta information */

    /**
     * Morph's name
     */
    public ResourceLocation resource;

    /**
     * Is this morph is favorite 
     */
    public boolean favorite = false;

    /**
     * Health when the player morphed into this morph 
     */
    protected float lastHealth;

    /* Rendering */

    /**
     * Client morph renderer. It's for {@link EntityPlayer} only, don't try 
     * using it with other types of entities.
     */
    @SideOnly(Side.CLIENT)
    public Render<? extends Entity> renderer;

    /* Render methods */

    /**
     * Render this morph on 2D screen (used in GUIs)
     */
    @SideOnly(Side.CLIENT)
    public abstract void renderOnScreen(EntityPlayer player, int x, int y, float scale, float alpha);

    /**
     * Render the entity (in the world) 
     */
    @SideOnly(Side.CLIENT)
    public abstract void render(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks);

    /**
     * Render the arm for given hand 
     */
    @SideOnly(Side.CLIENT)
    public boolean renderHand(EntityPlayer player, EnumHand hand)
    {
        return false;
    }

    /* Update loop */
    /**
     * Update the player based on its morph abilities and properties. This 
     * method also responsible for updating AABB size. 
     */
    public void update(EntityLivingBase target, IMorphing cap)
    {
        if (!EnderScience.proxy.config.disable_health)
        {
            this.setMaxHealth(target, this.settings.health);
        }

        if (this.settings.speed != 0.1F)
        {
            target.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.settings.speed);
        }

        for (IAbility ability : this.settings.abilities)
        {
            ability.update(target);
        }
        
        if(this.settings.sound != null) {
        	this.settings.sound.update(target);
        }
        
        /*
        if(cap.isMorphed()) {
        	ITeleportCapability capa = target.getCapability(EnderModCapabilities.CAPABILITY_TELEPORT, null);
        	if(capa.getMorphTimer() <= 0) {
        		this.demorph(target);
        		MorphAPI.demorph((EntityPlayer)target);
        	}
        }*/
	    //this.updateMorphTimer(target);
    }

    /* Morph and demorph handlers */

    /**
     * Morph into the current morph
     * 
     * This method responsible for setting up the health of the player to 
     * morph's health and invoke ability's onMorph methods.
     */
    public void morph(EntityLivingBase target)
    {
        this.lastHealth = (float)target.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
        this.setHealth(target, this.settings.health);

        for (IAbility ability : this.settings.abilities)
        {
            ability.onMorph(target);
        }
        
        if(this.settings.sound != null) {
        	this.settings.sound.onMorph(target);
        }
        /*
        ITeleportCapability cap = target.getCapability(EnderModCapabilities.CAPABILITY_TELEPORT, null);
        
        
        if(cap.getMorphTimer() <= 0) {
        	cap.setMorphTimer(MorphReference.MORPH_DURATION_ENDERMAN);
        	
        	if(!target.world.isRemote) {
	        	EnderMod.simple_channel.sendTo(new MessageCapabilityToClient(cap, true), (EntityPlayerMP) target);
        	} else {
        		EnderMod.simple_channel.sendToServer(new MessageCapabilityToServer(cap, true));
        	}
        }*/
    }

    /**
     * Demorph from the current morph
     * 
     * This method responsible for setting up the health back to player's 
     * default health and invoke ability's onDemorph methods.
     */
    public void demorph(EntityLivingBase target)
    {
        /* 20 is default player's health */
        this.setHealth(target, this.lastHealth <= 0.0F ? 20.0F : this.lastHealth);

        for (IAbility ability : this.settings.abilities)
        {
            ability.onDemorph(target);
        }
        
        if(this.settings.sound != null) {
        	this.settings.sound.onDemorph(target);
        }
    }

    /* Adjusting size */

    /**
     * Update player's size based on given width and height.
     * 
     * This method is responsible for doing trickshots, 360 noscopes while being 
     * morped in a morph. Probably...
     */
    public void updateSize(EntityLivingBase target, float width, float height)
    {
        updateSizeDefault(target, width, height);
    }

    public static void updateSizeDefault(EntityLivingBase target, float width, float height)
    {
        /* Any lower than this, and the morph will take damage when hitting the ceiling.
         * Likewise, an eye height less than this will cause suffocation damage when standing
         * on the ground.
         * This is hard-coded in vanilla.
         */
        float minEyeToHeadDifference = 0.1F;
        height = Math.max(height, minEyeToHeadDifference * 2);
        
        if (target instanceof EntityPlayer)
        {
            float eyeHeight = height * 0.9F;
            if (eyeHeight + minEyeToHeadDifference > height)
            {
                eyeHeight = height - minEyeToHeadDifference;
            }
            ((EntityPlayer) target).eyeHeight = eyeHeight;
        }
        
        /* This is a total rip-off of EntityPlayer#setSize method */
        if (width != target.width || height != target.height)
        {
            AxisAlignedBB aabb = target.getEntityBoundingBox();

            target.width = width;
            target.height = height;
            target.setEntityBoundingBox(new AxisAlignedBB(target.posX - width / 2, aabb.minY, target.posZ - width / 2, target.posX + width / 2, aabb.minY + height, target.posZ + width / 2));
        }
    }

    /* Adjusting health */

    /**
     * Set player's health proportional to the current health with given max 
     * health.
     * 
     * @author asanetargoss
     */
    protected void setHealth(EntityLivingBase target, float health)
    {
        if (EnderScience.proxy.config.disable_health)
        {
            return;
        }

        float maxHealth = target.getMaxHealth();
        float currentHealth = target.getHealth();
        float ratio = currentHealth / maxHealth;

        // A sanity check to prevent "healing" health when morphing to and from
        // a mob
        // with essentially zero health
        if (target instanceof EntityPlayer)
        {
            IMorphing capability = Morphing.get((EntityPlayer) target);
            if (capability != null)
            {
                // Check if a health ratio makes sense for the old health value
                if (maxHealth > IMorphing.REASONABLE_HEALTH_VALUE)
                {
                    // If it makes sense, store that ratio in the capability
                    capability.setLastHealthRatio(ratio);
                }
                else if (health > IMorphing.REASONABLE_HEALTH_VALUE)
                {
                    // If it doesn't make sense, BUT the new max health makes
                    // sense, retrieve the
                    // ratio from the capability and use that instead
                    ratio = capability.getLastHealthRatio();
                }
            }
        }

        this.setMaxHealth(target, health);
        // We need to retrieve the max health of the target after modifiers are
        // applied
        // to get a sensible value
        float proportionalHealth = target.getMaxHealth() * ratio;
        target.setHealth(proportionalHealth <= 0.0F ? Float.MIN_VALUE : proportionalHealth);
    }

    /**
     * Set player's max health
     */
    protected void setMaxHealth(EntityLivingBase target, float health)
    {
        if (target.getMaxHealth() != health)
        {
            target.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
        }
    }

    /* Safe shortcuts for activating action and attack */

    /**
     * Execute action with (or on) given player 
     */
    public void action(EntityLivingBase target)
    {
        if (this.settings.action != null)
        {
            this.settings.action.execute(target, this);
        } else {
        	System.out.println("action null");
        }
    }

    /**
     * Attack a target 
     */
    public void attack(Entity target, EntityLivingBase source)
    {
        if (this.settings.attack != null)
        {
            this.settings.attack.attack(target, source);
        }
    }

    /**
     * <p>Clone a morph.</p>
     * 
     * <p>
     * <b>IMPORTANT</b>: when you subclass other morphs, don't forget to override 
     * their method with your own, because otherwise its going to create 
     * another {@link CustomMorph} instance, for example, instead of 
     * MyCustomMorph instance.
     * </p>
     */
    public abstract AbstractMorph clone(boolean isRemote);

    /* Getting size */

    /**
     * Get width of this morph 
     */
    public abstract float getWidth(EntityLivingBase target);

    /**
     * Get height of this morph 
     */
    public abstract float getHeight(EntityLivingBase target);

    /**
     * Get the eye height of this morph.
     * Not used by updateSize.
     */
    public float getEyeHeight(EntityLivingBase target)
    {
//        if (!EnderMod.proxy.config.disable_pov)
//        {
            return this.getHeight(target) * 0.9F;
//        }
//        else
//        {
//            return 1.62F;
//        }
    }

    /**
     * Get the default sound that this morph makes when it
     * is hurt
     */
    public final SoundEvent getHurtSound(EntityLivingBase target)
    {
        return getHurtSound(target, SoundHandler.GENERIC_DAMAGE);
    }

    /**
     * Get the sound that this morph makes when it
     * is hurt by the given DamageSource, or return null
     * for no change.
     */
    public SoundEvent getHurtSound(EntityLivingBase target, DamageSource damageSource)
    {
    	/*
    	if(this.settings.sound != null) {
    		return this.settings.sound.getHurtSound(target);
    	} else {
    		return SoundHandler.NO_SOUND;
    	}*/
    	return SoundHandler.NO_SOUND;
    }

    /**
     * Get the sound that this morph makes when it
     * is killed, or return null for no change.
     */
    public SoundEvent getDeathSound(EntityLivingBase target)
    {
    	/*
    	if(this.settings.sound != null) {
    		return this.settings.sound.getDeathSound();
    	} else {
    		return SoundHandler.NO_SOUND;
    	}*/
    	return SoundHandler.NO_SOUND;
    }

    /**
     * Make this return true if you override playStepSound(..)
     */
    public boolean hasCustomStepSound(EntityLivingBase target)
    {
        return false;
    }

    /**
     * Plays the sound that this morph makes when it
     * takes a step, but only if hasCustomStepSound(..) returns true
     */
    public void playStepSound(EntityLivingBase target)
    {}

    /**
     * Called when the player just changed dimensions
     */
    public void onChangeDimension(EntityPlayer player, int oldDim, int currentDim)
    {}

    /**
     * Check either if given object is the same as this morph 
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof AbstractMorph)
        {
            AbstractMorph morph = (AbstractMorph) obj;

            boolean equal = false;
            
            if(morph.resource.getResourceDomain().equals(this.resource.getResourceDomain())
            		&& morph.resource.getResourcePath().equals(this.resource.getResourcePath())) {
            	equal = true;
            }
            
            return equal;
        }

        return super.equals(obj);
    }

    /**
     * Check whether the morph can be merged (this should allow 
     * overwriting of a morph instead of completely replacing it)
     */
    public boolean canMerge(AbstractMorph morph, boolean isRemote)
    {
        return false;
    }

    /**
     * Reset data for editing 
     */
    public void reset()
    {}

    /* Reading / writing to NBT */

    /**
     * Save abstract morph's properties to NBT compound 
     */
    public void toNBT(NBTTagCompound tag)
    {
       // tag.setString("Name", this.resource);
    	tag.setString(ResourceReference.RESOURCE_DOMAIN, resource.getResourceDomain());
    	tag.setString(ResourceReference.RESOURCE_PATH, resource.getResourcePath());
    	
        tag.setFloat("LastHealth", this.lastHealth);
        
        if (this.favorite) tag.setBoolean("Favorite", this.favorite);
    }

    /**
     * Read abstract morph's properties from NBT compound 
     */
    public void fromNBT(NBTTagCompound tag)
    {
        this.reset();

        this.resource = new ResourceLocation(tag.getString(ResourceReference.RESOURCE_DOMAIN), tag.getString(ResourceReference.RESOURCE_PATH));
        //this.name = tag.getString("Name");
        this.lastHealth = tag.getFloat("LastHealth");
        
        if (tag.hasKey("Favorite")) this.favorite = tag.getBoolean("Favorite");
    }
}