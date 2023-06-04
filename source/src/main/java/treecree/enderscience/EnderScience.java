package treecree.enderscience;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.MorphUtils;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.proxy.CommonProxy;
import treecree.enderscience.references.References;

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, version = References.MOD_VERSION, guiFactory = References.GUI_FACTORY, dependencies = "required-after:mclib@[%MCLIB%,)")
public class EnderScience {

	public static final Logger logger = LogManager.getLogger(References.MOD_ID);

	@Mod.Instance(References.MOD_ID)
	public static EnderScience instance;

	@SidedProxy(clientSide = References.PROXY_CLASS_CLIENT, serverSide = References.PROXY_CLASS_SERVER)
	public static CommonProxy proxy;

	/**
	 * Custom payload channel
	 */
	public static FMLEventChannel channel;
	//public static SimpleNetworkWrapper simple_channel;

	@EventHandler
	public void preLoad(FMLPreInitializationEvent event) {

		LOGGER = event.getModLog();
		channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(References.EVENT_CHANNEL);

		proxy.preLoad(event);

		EnderScienceCapabilities.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.load();
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event) {
		proxy.postLoad(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		MorphManager.INSTANCE.setActiveSettings(MorphUtils.reloadMorphSettings());
	}

	/* Logging */

	/* TODO: Set to false when publishing and remove all unnecessary printlns */
	public static boolean DEBUG = false;
	public static Logger LOGGER;

	/**
	 * Log out the message if in DEBUG mode.
	 * 
	 * But I always forget to turn it off before releasing the mod.
	 */
	public static void log(String message) {
		if (DEBUG) {
			LOGGER.log(Level.INFO, message);
		}
	}
}
