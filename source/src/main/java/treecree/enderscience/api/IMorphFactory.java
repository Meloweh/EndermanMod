package treecree.enderscience.api;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.client.gui.editor.GuiAbstractMorph;

/**
 * Morph factory
 * 
 * This factory interface will be responsible for providing different resources 
 * related to morphs like custom models, abilities, attacks or actions.
 */
public interface IMorphFactory
{
    /**
     * Register method
     * 
     * Register here everything that doesn't require stuff
     */
    public void register(MorphManager manager);

    /**
     * Register client method
     * 
     * Register here additional stuff that are related to client side
     */
    @SideOnly(Side.CLIENT)
    public void registerClient(MorphManager manager);

    /**
     * Register morph editors which will be available in the creative 
     * morphs for editing 
     */
    @SideOnly(Side.CLIENT)
    public void registerMorphEditors(List<GuiAbstractMorph> editors);

    /**
     * Get display name for morph
     * 
     * IMPORTANT: If your factory doesn't override any of the names, please 
     * return null.
     */
    @SideOnly(Side.CLIENT)
    public ResourceLocation displayResourceForMorph(AbstractMorph morph);

    /**
     * Get all available morphs for this morphing factory
     */
    public void getMorphs(MorphList morphs, World world);

    /**
     * Does this factory has morph by given name? 
     */
    public boolean hasMorph(ResourceLocation resource);

    /**
     * Get a morph from NBT
     */
    public AbstractMorph getMorphFromNBT(NBTTagCompound tag);
}