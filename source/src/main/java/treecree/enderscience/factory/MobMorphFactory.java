package treecree.enderscience.factory;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.api.EntityUtils;
import treecree.enderscience.api.IMorphFactory;
import treecree.enderscience.api.MorphList;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.api.morphs.EntityMorph;
import treecree.enderscience.client.gui.editor.GuiAbstractMorph;
import treecree.enderscience.references.NameReference;
import treecree.enderscience.references.ResourceReference;

/**
 * Mob morph factory
 * 
 * This is underlying morph factory. It's responsible for generating 
 * {@link EntityMorph} out of 
 */
public class MobMorphFactory implements IMorphFactory
{
    /**
     * Nothing to register here, since all of the morphs are generated on 
     * runtime 
     */
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
        editors.add(new GuiAbstractMorph(Minecraft.getMinecraft()));
    }

    /**
     * Get all available variation of vanilla mobs and default types of custom 
     * mobs
     */
    @Override
    public void getMorphs(MorphList morphs, World world)
    {
        for (ResourceLocation resource : EntityList.getEntityNameList())
        {
            if (this.hasMorph(resource) && !morphs.hasMorph(resource))
            {
                this.addMorph(morphs, world, resource, null);
            }
        }
    }

    /**
     * Add an entity morph to the morph list
     */
    private void addMorph(MorphList morphs, World world, ResourceLocation name, String json)
    {
        this.addMorph(morphs, world, name, "", json);
    }

    /**
     * Add an entity morph to the morph list
     */
    private void addMorph(MorphList morphs, World world, ResourceLocation resource, String variant, String json)
    {
        try
        {
            EntityMorph morph = this.getNewEntityMorph();
            EntityLivingBase entity = (EntityLivingBase) EntityList.createEntityByIDFromName(resource, world);//(EntityLivingBase) EntityList.createEntityByIDFromName(name, world);//EntityList.createEntityByName(name, world);

            if (entity == null)
            {
                System.out.println("Couldn't add morph " + ResourceReference.getResourceString(resource) + ", because it's null!");
                return;
            }

            NBTTagCompound data = entity.serializeNBT();

            morph.resource = resource;
            //morph.name = name.getResourcePath();

            if (json != null)
            {
                try
                {
                    data.merge(JsonToNBT.getTagFromJson(json));
                }
                catch (NBTException e)
                {
                    System.out.println("Failed to merge provided JSON data for '" + ResourceReference.getResourceString(resource) + "' morph!");
                    e.printStackTrace();
                }
            }

            /* Setting up a category */
            int index = ResourceReference.getResourceString(resource).indexOf(".");
            String category = "";

            /* Category for third-party mod mobs */
            if (index >= 0)
            {
                category = ResourceReference.getResourceString(resource).substring(0, index);
            }
            else if (entity instanceof EntityDragon || entity instanceof EntityWither || entity instanceof EntityGiantZombie)
            {
                category = "boss";
            }
            else if (entity instanceof EntityAnimal || resource.getResourcePath().equals("bat") || resource.getResourcePath().equals("squid"))
            {
                category = "animal";
            }
            else if (entity instanceof EntityMob || resource.getResourcePath().equals("ghast") || resource.getResourcePath().equals("magma_cube") || resource.getResourcePath().equals("slime") || resource.getResourcePath().equals("shulker"))
            {
                category = "hostile";
            }

            EntityUtils.stripEntityNBT(data);
            morph.setEntityData(data);
            morphs.addMorphVariant(resource, category, variant, morph);
        }
        catch (Exception e)
        {
            System.out.println("An error occured during insertion of " + ResourceReference.getResourceString(resource) + " morph!");
            e.printStackTrace();
        }
    }

    /**
     * Checks if the {@link EntityList} has an entity with given name does 
     * exist and the entity is a living base.
     */
    @Override
    public boolean hasMorph(ResourceLocation resource)
    {
    	
/*
        if (name.equals("metamorph.Block"))
        {
            return true;
        }

        
        Class<? extends Entity> clazz = EntityList.getClassFromName(name);

        if (clazz != null)
        {
            return EntityLivingBase.class.isAssignableFrom(clazz);
        }

        return false;*/
    	
    	return EntityLivingBase.class.isAssignableFrom(EntityLiving.class);
    }

    /**
     * Create an {@link EntityMorph} from NBT
     */
    @Override
    public AbstractMorph getMorphFromNBT(NBTTagCompound tag)
    {
    	ResourceLocation resource = new ResourceLocation(tag.getString(ResourceReference.RESOURCE_DOMAIN), tag.getString(ResourceReference.RESOURCE_PATH));

        if (this.hasMorph(resource))
        {
            EntityMorph morph = getNewEntityMorph();

            morph.fromNBT(tag);

            return morph;
        }

        return null;
    }

    /**
     * Get a morph from a name 
     */
    public EntityMorph getNewEntityMorph()
    {
        return new EntityMorph();
    }

    @Override
    @SideOnly(Side.CLIENT)
	public ResourceLocation displayResourceForMorph(AbstractMorph morph) {
		// TODO Auto-generated method stub
		return null;
	}
}