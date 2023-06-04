package treecree.enderscience.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import treecree.enderscience.EnderScience;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.MorphSettings;
import treecree.enderscience.api.abilities.IAbility;
import treecree.enderscience.api.abilities.IAction;
import treecree.enderscience.api.abilities.IAttackAbility;
import treecree.enderscience.api.abilities.ISound;
import treecree.enderscience.api.events.RegisterSettingsEvent;
import treecree.enderscience.api.json.MorphSettingsAdapter;
import treecree.enderscience.factory.abilities.ExplosiveDodge;
import treecree.enderscience.factory.abilities.LavaDodge;
import treecree.enderscience.factory.abilities.MagicDamageDodge;
import treecree.enderscience.factory.abilities.ProjectileDodge;
import treecree.enderscience.factory.abilities.WaterAllgergy;
import treecree.enderscience.factory.actions.Teleport;
import treecree.enderscience.factory.sounds.EndermanSound;
import treecree.enderscience.references.NameReference;
import treecree.enderscience.references.References;
import treecree.enderscience.references.ResourceReference;

/**
 * Register handler
 * 
 * This event handler is responsible for
 */
public class RegisterHandler {
	/**
	 * GSON instance that is responsible for deserializing morph settings
	 */
	private final static Gson GSON = new GsonBuilder()
			.registerTypeAdapter(MorphSettings.class, new MorphSettingsAdapter()).create();

	/**
	 * Register Metamorph's supplied abilities, actions and attacks.
	 */
	public static void registerAbilities(MorphManager manager) {
		/* Define shortcuts */
		Map<String, IAbility> abilities = manager.abilities;
		Map<String, IAction> actions = manager.actions;
		Map<String, ISound> sounds = manager.sounds;

		/* Register default abilities */
		abilities.put("water_allergy", new WaterAllgergy());
		abilities.put("projectile_dodge", new ProjectileDodge());
		abilities.put("explosives_dodge", new ExplosiveDodge());
		abilities.put("magicdamage_dodge", new MagicDamageDodge());
		abilities.put("lava_dodge", new LavaDodge());


		/* Register default actions */
		actions.put("teleport", new Teleport());

		/* Register sounds */
		sounds.put("enderman", new EndermanSound());
	}

	/**
	 * Register morph settings from default morphs configuration that comes with
	 * Metamorph and user configuration file
	 */
	@SubscribeEvent
	public void onSettingsReload(RegisterSettingsEvent event) {
		final Map<String, MorphSettings> stringSettings = new HashMap<>();

		final String resource = "assets/" + References.MOD_ID + "/models.json";
		
		this.loadMorphSettings(event.settings, this.getClass().getClassLoader().getResourceAsStream(resource));

		this.loadMorphSettings(event.settings, EnderScience.proxy.morphs);
	}

	/**
	 * Load morph settings into {@link MorphManager} with given {@link File} and
	 * with a try-catch which logs out an error in case of failure.
	 */
	private void loadMorphSettings(Map<String, MorphSettings> settings, File config) {
		try {
			this.loadMorphSettings(settings, new FileInputStream(config));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load morph settings from {@link InputStream}
	 */
	private void loadMorphSettings(Map<String, MorphSettings> settings, InputStream input) {
		Scanner scanner = new Scanner(input, "UTF-8");

		@SuppressWarnings("serial")
		Type type = new TypeToken<Map<String, MorphSettings>>() {
		}.getType();

		Map<String, MorphSettings> data = GSON.fromJson(scanner.useDelimiter("\\A").next(), type);

		scanner.close();

		for (Map.Entry<String, MorphSettings> entry : data.entrySet()) {
			String key = entry.getKey();
			MorphSettings morphSettings = entry.getValue();

			if (settings.containsKey(key)) {
				settings.get(key).merge(morphSettings);
			} else {
				settings.put(key, morphSettings);
			}
		}
	}
}