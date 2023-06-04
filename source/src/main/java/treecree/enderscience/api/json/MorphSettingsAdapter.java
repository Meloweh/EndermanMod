package treecree.enderscience.api.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.MorphSettings;
import treecree.enderscience.api.abilities.IAbility;

/**
 * Morph settings adapter
 * 
 * This class is responsible for deserializing {@link MorphSettings}.
 */
public class MorphSettingsAdapter implements JsonDeserializer<MorphSettings>
{
    @Override
    public MorphSettings deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject object = json.getAsJsonObject();

        MorphSettings morph = new MorphSettings();
        MorphManager manager = MorphManager.INSTANCE;
        List<IAbility> abilities = new ArrayList<IAbility>();

        if (object.has("health"))
        {
            morph.health = object.get("health").getAsInt();
        }

        if (object.has("speed"))
        {
            morph.speed = object.get("speed").getAsFloat();
        }

        if (object.has("hostile"))
        {
            morph.hostile = object.get("hostile").getAsBoolean();
        }

        if (object.has("hands"))
        {
            morph.hands = object.get("hands").getAsBoolean();
        }

        if (object.has("abilities"))
        {
            for (JsonElement ability : object.get("abilities").getAsJsonArray())
            {
                IAbility iability = manager.abilities.get(ability.getAsString());

                if (iability != null)
                {
                    abilities.add(iability);
                }
            }

            morph.abilities = abilities.toArray(new IAbility[abilities.size()]);
        }

        if (object.has("action"))
        {
            morph.action = manager.actions.get(object.get("action").getAsString());
        }

        if (object.has("sound"))
        {
            morph.sound = manager.sounds.get(object.get("sound").getAsString());
        }
        
        if (object.has("updates"))
        {
            morph.updates = object.get("updates").getAsBoolean();
        }

        return morph;
    }
}