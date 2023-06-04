package treecree.enderscience.factory;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.api.IMorphFactory;
import treecree.enderscience.api.MorphList;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.client.gui.editor.GuiAbstractMorph;
import treecree.enderscience.factory.editors.GuiPlayerMorph;
import treecree.enderscience.factory.morphs.PlayerMorph;
import treecree.enderscience.references.NameReference;
import treecree.enderscience.references.ResourceReference;

/**
 * Player morph factory
 * 
 * This morph factory allows making a morph using player's username. 
 * Basically player's disguising.
 */
public class PlayerMorphFactory implements IMorphFactory
{
    /**
     * Notch, the Minecraft's creator 
     */
    private PlayerMorph player;

    @Override
    public void register(MorphManager manager)
    {}

    @Override
    @SideOnly(Side.CLIENT)
    public void registerClient(MorphManager manager)
    {}

    @Override
    @SideOnly(Side.CLIENT)
    public void registerMorphEditors(List<GuiAbstractMorph> editors)
    {
        editors.add(new GuiPlayerMorph(Minecraft.getMinecraft()));
    }

    /**
     * Return game profile's username as for player's name 
     */
    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation displayResourceForMorph(AbstractMorph morph)
    {
        if (morph instanceof PlayerMorph)
        {
            return new ResourceLocation(NameReference.DOMAIN_MINECRAFT, ((PlayerMorph) morph).profile.getName()); // is null?
        }

        return null;
    }

    @Override
    public void getMorphs(MorphList morphs, World world)
    {
        if (this.player == null)
        {
            this.player = new PlayerMorph();

            NBTTagCompound tag = new NBTTagCompound();

            //tag.setString("Name", "Player");
            tag.setString(ResourceReference.RESOURCE_DOMAIN, NameReference.DOMAIN_MINECRAFT);
            tag.setString(ResourceReference.RESOURCE_PATH, "player");
            
            tag.setString("username", this.player.getEntity().getName());

            this.player.fromNBT(tag);
        }

        morphs.addMorph(new ResourceLocation(NameReference.DOMAIN_MINECRAFT, "player"), "players", this.player);
    }

    @Override
    public boolean hasMorph(ResourceLocation resource)
    {
    	return resource.getResourceDomain().equals(NameReference.DOMAIN_MINECRAFT) && resource.getResourcePath().equals("player");
        //return resource.getResourcePath().equals("Player");
    }

    @Override
    public AbstractMorph getMorphFromNBT(NBTTagCompound tag)
    {
    	
    	final ResourceLocation resource = new ResourceLocation(tag.getString(ResourceReference.RESOURCE_DOMAIN), tag.getString(ResourceReference.RESOURCE_PATH));
    	
        //if (tag.getString("Name").equals("Player"))
    	if(resource.getResourceDomain().equals(NameReference.DOMAIN_MINECRAFT) && resource.getResourcePath().equals("player"))
        {
            PlayerMorph player = new PlayerMorph();

            player.fromNBT(tag);

            return player;
        }

        return null;
    }
}