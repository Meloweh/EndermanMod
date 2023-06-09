package treecree.enderscience.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.api.abilities.IAbility;
import treecree.enderscience.api.abilities.IAction;
import treecree.enderscience.api.abilities.IAttackAbility;
import treecree.enderscience.api.abilities.ISound;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.api.morphs.EntityMorph;
import treecree.enderscience.client.gui.editor.GuiAbstractMorph;
import treecree.enderscience.factory.RegisterHandler;
import treecree.enderscience.references.ResourceReference;

/**
 * Morph manager class
 * 
 * This manager is responsible for managing available morphings.
 */
public class MorphManager {
	/**
	 * Default <s>football</s> morph manager
	 */
	public static final MorphManager INSTANCE = new MorphManager();

	/**
	 * Registered abilities
	 */
	public Map<String, IAbility> abilities = new HashMap<String, IAbility>();

	/**
	 * Registered actions
	 */
	public Map<String, IAction> actions = new HashMap<String, IAction>();
	
	/**
	 * Registered sounds
	 */
	public Map<String, ISound> sounds = new HashMap<String, ISound>();

	/**
	 * Registered morph factories
	 */
	public List<IMorphFactory> factories = new ArrayList<IMorphFactory>();

	/**
	 * Active morph settings
	 */
	public Map<String, MorphSettings> activeSettings = new HashMap<String, MorphSettings>();

	public void setActiveSettings(Map<String, MorphSettings> settings) {
		
        Map<String, MorphSettings> newSettings = new HashMap<String, MorphSettings>();

        for (Map.Entry<String, MorphSettings> entry : settings.entrySet())
        {
            String key = entry.getKey();
            MorphSettings setting = this.activeSettings.get(key);
            
            if (setting == null)
            {
                setting = entry.getValue();
            }
            else
            {
                setting.merge(entry.getValue());
            }

            newSettings.put(key, setting);
        }

        this.activeSettings.clear();
        this.activeSettings.putAll(newSettings);
	}

	/**
	 * That's a singleton, boy!
	 */
	private MorphManager() {
	}

	/**
	 * Register all morph factories
	 */
	public void register() {
		for (int i = this.factories.size() - 1; i >= 0; i--) {
			this.factories.get(i).register(this);
		}
	}

	/**
	 * Register all morph factories on the client side
	 */
	@SideOnly(Side.CLIENT)
	public void registerClient() {
		for (int i = this.factories.size() - 1; i >= 0; i--) {
			this.factories.get(i).registerClient(this);
		}
	}

	/**
	 * Register morph editors
	 */
	@SideOnly(Side.CLIENT)
	public void registerMorphEditors(List<GuiAbstractMorph> editors) {
		for (int i = this.factories.size() - 1; i >= 0; i--) {
			this.factories.get(i).registerMorphEditors(editors);
		}
	}

	/**
	 * Checks if manager has given morph by ID and NBT tag compound
	 * 
	 * This meethod iterates over all {@link IMorphFactory}s and if any of them
	 * returns true, then there's a morph, otherwise false.
	 */
	public boolean hasMorph(ResourceLocation resource) {
		for (int i = this.factories.size() - 1; i >= 0; i--) {
			if (this.factories.get(i).hasMorph(resource)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Get an abstract morph from NBT
	 * 
	 * This method iterates over all {@link IMorphFactory}s, returns a morph from
	 * the first morph factory that does have a morph.
	 */
	public AbstractMorph morphFromNBT(NBTTagCompound tag) {
		ResourceLocation resource = new ResourceLocation(tag.getString(ResourceReference.RESOURCE_DOMAIN),
				tag.getString(ResourceReference.RESOURCE_PATH));// tag.getString("Name");

		for (int i = this.factories.size() - 1; i >= 0; i--) {
			if (this.factories.get(i).hasMorph(resource)) {
				AbstractMorph morph = this.factories.get(i).getMorphFromNBT(tag);

				this.applySettings(morph);

				return morph;
			}
		}

		return null;
	}

	/**
	 * Apply morph settings on a given morph
	 */
	public void applySettings(AbstractMorph morph) {
		if (this.activeSettings.containsKey(ResourceReference.getResourceString(morph.resource))) {
			morph.settings = this.activeSettings.get(ResourceReference.getResourceString(morph.resource));
		}
	}

	/**
	 * Get all morphs that factories provide. Take in account that this code don't
	 * apply morph settings.
	 */
	public MorphList getMorphs(World world) {
		MorphList morphs = new MorphList();

		for (int i = this.factories.size() - 1; i >= 0; i--) {
			this.factories.get(i).getMorphs(morphs, world);
		}

		return morphs;
	}

	/**
	 * Get morph from the entity
	 * 
	 * Here I should add some kind of mechanism that allows people to substitute the
	 * name of the morph based on the given entity (in the future with introduction
	 * of the public API).
	 */
	public ResourceLocation morphResourceFromEntity(Entity entity) {
		return EntityList.getKey(entity);// EntityList.getEntityString(entity);
	}

	/**
	 * Get display name for morph (only client)
	 */
	@SideOnly(Side.CLIENT)
	public ResourceLocation morphDisplayResourceFromMorph(AbstractMorph morph) {
		for (int i = this.factories.size() - 1; i >= 0; i--) {
			ResourceLocation resource = this.factories.get(i).displayResourceForMorph(morph);

			if (resource != null) {
				return resource;
			}
		}

		/* Falling back to default method */
		ResourceLocation resource = morph.resource;

		try {
			if (morph instanceof EntityMorph) {
				resource = EntityList.getKey(((EntityMorph) morph).getEntity(Minecraft.getMinecraft().world));// EntityList.getEntityString(((EntityMorph)
																												// morph).getEntity(Minecraft.getMinecraft().world));
			}
		} catch (Exception e) {
		}

		String key = "entity." + ResourceReference.getResourceString(resource) + ".name";
		String result = I18n.format(key);

		return key.equals(result) ? resource : new ResourceLocation(resource.getResourceDomain(), result);
	}
}