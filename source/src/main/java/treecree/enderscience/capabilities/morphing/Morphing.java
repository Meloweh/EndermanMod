package treecree.enderscience.capabilities.morphing;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.EnderScience;
import treecree.enderscience.api.MorphAPI;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.client.gui.elements.GuiSurvivalMorphs;

/**
 * Default implementation of {@link IMorphing} interface.
 *
 * This class is responsible for storing current morphing, setting and retrieval
 * of current morphing.
 */
public class Morphing implements IMorphing
{
    /**
     * List of acquired abstract morphs 
     */
    private List<AbstractMorph> acquiredMorphs = new ArrayList<AbstractMorph>();

    /**
     * Current used morph
     */
    private AbstractMorph morph;

    /**
     * Used for animation
     */
    private AbstractMorph previousMorph;

    /**
     * Animation timer 
     */
    private int animation;

    /**
     * The last damage source received by the player
     */
    private DamageSource lastDamageSource;

    /**
     * (health / max health) is stored here when the new max health ends up
     * very close to zero, and retrieved when the fraction is meaningful again
     */
    private float lastHealthRatio;

    /**
     * Whether or not the current player is in a morph which can drown on land
     * due to having the Swim ability
     */
    private boolean hasSquidAir = false;

    /**
     * The air value used for morphs with the Swim ability in place of regular
     * player air
     */
    private int squidAir = 300;

    /**
     * GUI menu which is responsible for choosing morphs
     */
    @SideOnly(Side.CLIENT)
    private GuiSurvivalMorphs overlay;

    /**
     * Whether {@link #overlay} is non-null
     *
     * This is a separate field because {@link #overlay} is SideOnly(CLIENT), so referring to it on the server (where
     * it would always be null) is not possible (will result in NoSuchFieldError).
     */
    private boolean hasOverlay = false;

    public static IMorphing get(EntityPlayer player)
    {
        if (player == null)
        {
            return null;
        }
        return player.getCapability(MorphingProvider.MORPHING_CAP, null);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isAnimating()
    {
    	return false;
    	/*
        if (Metamorph.proxy.config.disable_morph_animation)
        {
            return false;
        }

        return this.animation != -1;*/
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getAnimation()
    {
        return this.animation;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AbstractMorph getPreviousMorph()
    {
        return this.previousMorph;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderPlayer(EntityPlayer player, double x, double y, double z, float yaw, float partialTick)
    {
        if (this.morph == null && !this.isAnimating())
        {
            return false;
        }

        if (this.morph == null && this.animation <= 10 || this.previousMorph == null && this.animation > 10)
        {
            return false;
        }

        if (!this.isAnimating())
        {
            this.morph.render(player, x, y, z, yaw, partialTick);

            return true;
        }

        GlStateManager.pushMatrix();

        /* Render morph in after transition */
        if (this.animation <= 10)
        {
            float anim = (this.animation - partialTick) / 10.0F;
            float offset = 0;

            if (anim >= 0)
            {
                offset = -anim * anim * 2F;
            }

            GlStateManager.translate(x, y + offset, z);

            if (anim >= 0)
            {
                GlStateManager.rotate(anim * -90.0F, 1, 0, 0);
                GlStateManager.scale(1 - anim, 1 - anim, 1 - anim);
            }

            this.morph.render(player, 0, 0, 0, yaw, partialTick);
        }
        else if (this.previousMorph != null)
        {
            /* Render morph before the transition */
            float anim = (this.animation - 10 - partialTick) / 10.0F;
            float offset = 0;

            if (anim >= 0)
            {
                offset = (1 - anim);
            }

            GlStateManager.translate(x, y + offset, z);

            if (anim >= 0)
            {
                GlStateManager.rotate((1 - anim) * 90.0F, 1, 0, 0);
                GlStateManager.scale(anim, anim, anim);
            }

            this.previousMorph.render(player, 0, 0, 0, yaw, partialTick);
        }

        GlStateManager.popMatrix();

        return true;
    }

    @Override
    public DamageSource getLastDamageSource()
    {
        return lastDamageSource;
    }

    @Override
    public void setLastDamageSource(DamageSource damageSource)
    {
        this.lastDamageSource = damageSource;
    }

    @Override
    public boolean acquireMorph(AbstractMorph morph)
    {
        if (morph == null || this.acquiredMorph(morph))
        {
            return false;
        }

        this.acquiredMorphs.add(morph);

        if (this.hasOverlay)
        {
            this.overlay.setupMorphs(this);
        }

        return true;
    }

    @Override
    public boolean acquiredMorph(AbstractMorph morph)
    {
        for (AbstractMorph acquired : this.acquiredMorphs)
        {
            if (acquired.equals(morph))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<AbstractMorph> getAcquiredMorphs()
    {
        return acquiredMorphs;
    }

    @Override
    public void setAcquiredMorphs(List<AbstractMorph> morphs)
    {
        this.acquiredMorphs.clear();
        this.acquiredMorphs.addAll(morphs);

        if (this.hasOverlay)
        {
            this.overlay.setupMorphs(this);
        }
    }

    @Override
    public AbstractMorph getCurrentMorph()
    {
        return this.morph;
    }

    @Override
    public boolean setCurrentMorph(AbstractMorph morph, EntityPlayer player, boolean force)
    {
        if (morph == null)
        {
            this.demorph(player);

            return true;
        }

        boolean creative = player != null ? player.isCreative() : false;

        if (force || creative || this.acquiredMorph(morph))
        {
            if (player != null && this.morph != null)
            {
                this.morph.demorph(player);
            }

            this.setMorph(morph, player == null ? false : player.world.isRemote);

            if (player != null)
            {
                this.morph.morph(player);
            }

            return true;
        }

        return false;
    }

    @Override
    public void demorph(EntityPlayer player)
    {
        if (player != null && this.morph != null)
        {
            this.morph.demorph(player);
        }

        this.setMorph(null, player == null ? false : player.world.isRemote);
    }

    /**
     * Set current morph, as well as update animation information  
     */
    protected void setMorph(AbstractMorph morph, boolean isRemote)
    {
        if (this.morph == null || (this.morph != null && !this.morph.canMerge(morph, isRemote)))
        {
        	/*
        	if(morph.resource.getResourceDomain().isEmpty() || morph.resource.getResourcePath().isEmpty()) {
        		return;
        	}*/
        	
            if (!EnderScience.proxy.config.disable_morph_animation)
            {
                this.animation = 20;
            }

            this.previousMorph = this.morph;
            this.morph = morph;
        }
    }

    @Override
    public boolean isMorphed()
    {
        return this.morph != null;
    }
    
    @Override
    public boolean remove(AbstractMorph morph) {
    	if(this.acquiredMorphs.contains(morph)) {
    		
    		int index = this.acquiredMorphs.indexOf(morph);
    		return remove(index);
    		
    	}
    	return false;
    }

    @Override
    public boolean remove(int index)
    {
        if (!this.acquiredMorphs.isEmpty() && index >= 0 && index < this.acquiredMorphs.size())
        {
            this.acquiredMorphs.remove(index);

            if (this.hasOverlay)
            {
                this.overlay.remove(index);
            }

            return true;
        }

        return false;
    }

    @Override
    public void copy(IMorphing morphing, EntityPlayer player)
    {
        this.acquiredMorphs.addAll(morphing.getAcquiredMorphs());
        if (morphing.getCurrentMorph() != null)
        {
            NBTTagCompound morphNBT = new NBTTagCompound();
            morphing.getCurrentMorph().toNBT(morphNBT);
            this.setCurrentMorph(MorphManager.INSTANCE.morphFromNBT(morphNBT), player, true);
        }
        else
        {
            this.setCurrentMorph(null, player, true);
        }
    }

    @Override
    public float getLastHealthRatio()
    {
        return lastHealthRatio;
    }

    @Override
    public void setLastHealthRatio(float lastHealthRatio)
    {
        this.lastHealthRatio = lastHealthRatio;
    }

    @Override
    public boolean getHasSquidAir()
    {
        return hasSquidAir;
    }

    @Override
    public void setHasSquidAir(boolean hasSquidAir)
    {
        this.hasSquidAir = hasSquidAir;
    }

    @Override
    public int getSquidAir()
    {
        return squidAir;
    }

    @Override
    public void setSquidAir(int squidAir)
    {
        this.squidAir = squidAir;
    }
    
    private long delta = System.currentTimeMillis();
    
    @Override
    public void update(EntityPlayer player)
    {
        if (this.animation >= 0)
        {
            this.animation--;
        }

        if (this.animation == 16 && !player.world.isRemote && !EnderScience.proxy.config.disable_morph_animation)
        {
            /* Pop! */
            ((WorldServer) player.world).spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, false, player.posX, player.posY + 0.5, player.posZ, 25, 0.5, 0.5, 0.5, 0.05);

            player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
        }

        if (this.morph != null)
        {
            this.morph.update(player, this);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiSurvivalMorphs getOverlay() {
        if (this.overlay == null)
        {
            this.overlay = new GuiSurvivalMorphs();
            this.overlay.setupMorphs(this);
            this.hasOverlay = true;
        }
        return this.overlay;
    }
}