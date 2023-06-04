package treecree.enderscience.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treecree.enderscience.references.References;

/**
 * Metamorph config class
 * 
 * Intance of this class is responsible for storing configuration for Metamorph 
 * mod.
 */
public class MetamorphConfig
{
    /* Config options */

    /**
     * Retain morphs when player died 
     */
    //public boolean keep_morphs;

    /**
     * Hide username in the survival morphing menu. Added just because, for 
     * no reason, if you're asking 
     */
    public boolean hide_username;

    /**
     * Show demorph as an option in survival morph menu 
     */
    public boolean show_demorph;

    /**
     * Disable modifying Point-of-View. Requested because of MorePlayerModels
     */
    //public boolean disable_pov;

    /**
     * Disable modifying health. Requested because of Tough as Nails 
     */
    public boolean disable_health;

    /**
     * Disables morphing animation 
     */
    public boolean disable_morph_animation;

    /**
     * Disable the ability of morphs labeled as "hostile" to avoid being
     * attacked by hostile mobs.
     */
    public boolean disable_morph_disguise;

    /* End of config options */

    /**
     * Forge-provided configuration object class instance stuff...
     */
    private Configuration config;

    public MetamorphConfig(Configuration config)
    {
        this.config = config;
        this.reload();
    }

    /**
     * Reload config values
     */
    public void reload()
    {
        String cat = Configuration.CATEGORY_GENERAL;
        String lang = References.MOD_ID + ".config.";

        //this.keep_morphs = this.config.getBoolean("keep_morphs", cat, true, "Retain morphs when player dies?", lang + "keep_morphs");
        this.hide_username = this.config.getBoolean("hide_username", cat, false, "Hide username in survival morphing menu", lang + "hide_username");
        this.show_demorph = this.config.getBoolean("show_demorph", cat, true, "Show demorph as an option in survival morph menu", lang + "show_demorph");
        //this.disable_pov = this.config.getBoolean("disable_pov", cat, false, "Disable modifying Point-of-View. Requested to fix MorePlayerModels vertical jittering", lang + "disable_pov");
        this.disable_health = this.config.getBoolean("disable_health", cat, false, "Disable modifying health", lang + "disable_health");
        this.disable_morph_animation = this.config.getBoolean("disable_morph_animation", cat, false, "Disables morphing animation", lang + "disable_morph_animation");
        this.disable_morph_disguise = this.config.getBoolean("disable_morph_disguise", cat, false, "Disables the ability of morphs labeled as 'hostile' to avoid being attacked by hostile mobs.", lang + "disable_morph_disguise");
        
        this.config.getCategory(cat).setComment("General configuration of Enderman Potion Mod");

        if (this.config.hasChanged())
        {
            this.config.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(References.MOD_ID) && this.config.hasChanged())
        {
            this.reload();
        }
    }
}