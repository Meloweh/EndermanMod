package treecree.enderscience.api.events;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import treecree.enderscience.api.MorphSettings;

/**
 * Register settings event
 * 
 * This event should be fired when Metamorph reloads morph settings 
 * (this happens once the server is starting up or when admin executes 
 * "/metamorph reload settings" command).
 * 
 * Use this event to add your own custom morph settings.  
 */
public class RegisterSettingsEvent extends Event
{
    public Map<String, MorphSettings> settings = new HashMap<String, MorphSettings>();
}