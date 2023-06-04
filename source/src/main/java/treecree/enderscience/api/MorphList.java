package treecree.enderscience.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.references.ResourceReference;

/**
 * Morph list
 * 
 * This class is used in {@link GuiCreativeMenu} for displaying morphs in GUIs. 
 * It's generated in {@link MorphManager}. 
 */
public class MorphList
{
    /**
     * This field is going to be responsible storing morphs. Feel free to use 
     * this field directly, but don't abuse this privilege.  
     */
    public Map<String, List<MorphCell>> morphs = new HashMap<String, List<MorphCell>>();

    /**
     * Checks if this list has a morph by given name 
     */
    public boolean hasMorph(ResourceLocation resource)
    {
        return this.morphs.containsKey(ResourceReference.getResourceString(resource));
    }

    /**
     * Add a morph to this list with name   
     */
    public void addMorph(ResourceLocation resource, AbstractMorph morph)
    {
        this.addMorph(resource, "", "", morph);
    }

    /**
     * Add a morph to this list with name and category
     */
    public void addMorph(ResourceLocation resource, String category, AbstractMorph morph)
    {
        this.addMorph(resource, category, "", morph);
    }

    /**
     * Add a morph to this list with name, category and variant name
     */
    public void addMorph(ResourceLocation resource, String category, String variant, AbstractMorph morph)
    {
        this.addMorph(resource, category, "", variant, morph);
    }

    /**
     * Add a morph to this list with name, category, category variant and variant name
     * 
     * This method is responsible for adding a morph variant
     */
    public void addMorph(ResourceLocation resource, String category, String categoryVariant, String variant, AbstractMorph morph)
    {
        if (this.hasMorph(resource))
        {
            return;
        }

        List<MorphCell> list = new ArrayList<MorphCell>();

        list.add(new MorphCell(morph, category, categoryVariant, variant));
        this.morphs.put(ResourceReference.getResourceString(resource), list);
    }

    /**
     * Add a morph variant to this morph list
     */
    public void addMorphVariant(ResourceLocation resource, String category, String variant, AbstractMorph morph)
    {
        this.addMorphVariant(resource, category, "", variant, morph);
    }

    /**
     * Add a morph variant to this morph list.
     * 
     * This is like {@link #addMorph(String, AbstractMorph)}, but it appends 
     * another morph. Basically, it adds a morph variant.
     */
    public void addMorphVariant(ResourceLocation resource, String category, String categoryVariant, String variant, AbstractMorph morph)
    {
        if (this.hasMorph(resource))
        {
            this.morphs.get(ResourceReference.getResourceString(resource)).add(new MorphCell(morph, category, categoryVariant, variant));
        }
        else
        {
            this.addMorph(resource, category, categoryVariant, variant, morph);
        }
    }

    /**
     * Remove a morph variant from the morph list 
     */
    public void removeVariant(ResourceLocation resource, int index)
    {
        if (this.hasMorph(resource))
        {
            List<MorphCell> list = this.morphs.get(ResourceReference.getResourceString(resource));

            /* Safe removing technique, avoiding exception basically */
            if (!list.isEmpty() && index >= 0 && index < list.size())
            {
                list.remove(index);
            }
        }
    }

    /**
     * Morph cell
     * 
     * This class is responsible for containing additional information about 
     * morphs such as its category and variant.
     */
    public static class MorphCell
    {
        public AbstractMorph morph;
        public String category;
        public String categoryVariant;
        public String variant;

        public MorphCell(AbstractMorph morph, String category, String categoryVariant, String variant)
        {
            this.morph = morph;
            this.category = category;
            this.categoryVariant = categoryVariant;
            this.variant = variant;
        }
    }
}