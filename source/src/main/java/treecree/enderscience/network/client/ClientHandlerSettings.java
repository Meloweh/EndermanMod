package treecree.enderscience.network.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.MorphSettings;
import treecree.enderscience.network.common.PacketSettings;
import treecree.enderscience.references.ResourceReference;

public class ClientHandlerSettings extends ClientMessageHandler<PacketSettings>
{
    @Override
    public void run(EntityPlayerSP player, PacketSettings message)
    {
    	/*
    	Map<ResourceLocation, MorphSettings> resourceList = new HashMap<>();
    	
    	message.settings.forEach((k, v) -> resourceList.put(ResourceReference.getResourceLocationFromString(k), v));
    	
        MorphManager.INSTANCE.setActiveSettings(resourceList);*/
    	
    	MorphManager.INSTANCE.setActiveSettings(message.settings);
    }
}