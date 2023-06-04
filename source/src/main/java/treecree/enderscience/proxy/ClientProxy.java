package treecree.enderscience.proxy;

import net.minecraftforge.fml.relauncher.Side;
import treecree.enderscience.EnderScience;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.client.NetworkHandler;
import treecree.enderscience.client.RenderingHandler;
import treecree.enderscience.client.gui.elements.GuiHud;
import treecree.enderscience.client.gui.elements.GuiOverlay;
import treecree.enderscience.client.gui.elements.GuiOverlay2;
import treecree.enderscience.client.render.RenderMorph;
import treecree.enderscience.entity.EntityMorph;
import treecree.enderscience.events.CapabilitySyncHandler;
import treecree.enderscience.events.ClientPlayerEventHandler;
import treecree.enderscience.events.InputEventHandler;
import treecree.enderscience.references.HotKeys;
import treecree.enderscience.registry.Keybindings;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RenderSubPlayer;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.GameType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.client.model.ModelLoader;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public static final String getComputerName()
	{
	    Map<String, String> env = System.getenv();
	    if (env.containsKey("COMPUTERNAME"))
	        return env.get("COMPUTERNAME");
	    else if (env.containsKey("HOSTNAME"))
	        return env.get("HOSTNAME");
	    else
	        return "Unknown Computer";
	}
	
	/**
	 * GUI overlay which is responsible for showing up acquired morphs
	 */
	public static GuiOverlay morphOverlay = new GuiOverlay();
	
	public static GuiOverlay2 morphOverlay2 = new GuiOverlay2();

	public static GuiHud hud = new GuiHud();

	//public static KeyboardHandler keys;

	@Override
	public EntityPlayer getClientPlayer() {
		return FMLClientHandler.instance().getClientPlayerEntity();
	}

	@Override
	public EntityPlayer getPlayerFromMessageContext(MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			return FMLClientHandler.instance().getClientPlayerEntity();
		} else if (ctx.side == Side.SERVER) {
			return ctx.getServerHandler().player;
		} else {
			EnderScience.logger.warn("INVALID SIDE: getPlayerFromMessageContext = " + ctx.side);
		}

		throw new NullPointerException("INVALID SIDE: getPlayerFromMessageContext");
	}

	@Override
	public void preLoad(FMLPreInitializationEvent event) {
		super.preLoad(event);

		/* Register entity renderers */
		RenderingRegistry.registerEntityRenderingHandler(EntityMorph.class, new RenderMorph.MorphFactory());

		/* Registering an event channel for custom payload */
		EnderScience.channel.register(new NetworkHandler());
	}

	@Override
	public void load() {
		super.load();

		/* Register client event handlers */
		MinecraftForge.EVENT_BUS.register(new RenderingHandler(morphOverlay, morphOverlay2, hud));
		//MinecraftForge.EVENT_BUS.register(keys = new KeyboardHandler());

		/* Register client morph manager */
		MorphManager.INSTANCE.registerClient();

		MinecraftForge.EVENT_BUS.register(new InputEventHandler());
		MinecraftForge.EVENT_BUS.register(new ClientPlayerEventHandler());

		registerKeyBindings();
	}

	/**
	 * In post load, we're going to substitute player renderers
	 */
	@Override
	public void postLoad(FMLPostInitializationEvent event) {
		super.postLoad(event);

		/* Rendering stuff */
		RenderManager manager = Minecraft.getMinecraft().getRenderManager();

		this.substitutePlayerRenderers(manager);
	}

	public void registerKeyBindings() {
		
		Keybindings.keyAction = new KeyBinding(HotKeys.KEYBIND_ACTION, HotKeys.KEYBIND_ACTION_BIND, HotKeys.KEYBIND_CATEGORY_ENDERMOD);

		/*Keybindings.keyToPlayerTeleport = new KeyBinding(HotKeys.KEYBIND_TO_PLAYER_TELEPORT,
				HotKeys.TO_RANDOM_PLAYER_KEYBIND_TELEPORT, HotKeys.KEYBIND_CATEGORY_ENDERMOD);

		Keybindings.keyToBedTeleport = new KeyBinding(HotKeys.KEYBIND_TO_BED_TELEPORT, HotKeys.TO_BED_TELEPORT,
				HotKeys.KEYBIND_CATEGORY_ENDERMOD);*/

		ClientRegistry.registerKeyBinding(Keybindings.keyAction);
		/*ClientRegistry.registerKeyBinding(Keybindings.keyToPlayerTeleport);
		ClientRegistry.registerKeyBinding(Keybindings.keyToBedTeleport);*/
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void substitutePlayerRenderers(RenderManager manager) {
		Map<String, net.minecraft.client.renderer.entity.RenderPlayer> skins = null;

		/* Iterate over all render manager fields and get access to skinMap */
		for (Field field : manager.getClass().getDeclaredFields()) {
			if (field.getType().equals(Map.class)) {
				field.setAccessible(true);

				try {
					Map map = (Map) field.get(manager);

					if (map.get("default") instanceof net.minecraft.client.renderer.entity.RenderPlayer) {
						skins = map;

						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		/* Replace player renderers with Blockbuster substitutes */
		if (skins != null) {
			RenderPlayer slim = skins.get("slim");
			RenderPlayer def = skins.get("default");

			skins.put("slim", new RenderSubPlayer(manager, slim, true));
			skins.put("default", new RenderSubPlayer(manager, def, false));

			EnderScience.log("Skin map renderers were successfully replaced with Metamorph substitutes!");
		}
	}

	@Override
	public void playSound(int soundId, float pitch, float volume, boolean repeat, boolean stop, double x, double y,
			double z) {
		SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
		SoundEvent sound = SoundEvent.REGISTRY.getObjectById(soundId);

		if (sound != null) {
			if (stop) {
				soundHandler.stop(sound.getRegistryName().toString(), null);
			} else {
				PositionedSoundRecord positionedSound = new PositionedSoundRecord(sound.getSoundName(),
						SoundCategory.RECORDS, volume, pitch, repeat, 0, AttenuationType.LINEAR, (float) x, (float) y,
						(float) z);
				soundHandler.playSound(positionedSound);
			}
		}
	}
	
    /**
     * Get game mode of a player 
     */
    public static GameType getGameMode(EntityPlayer player)
    {
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(player.getGameProfile().getId());

        return networkplayerinfo != null ? networkplayerinfo.getGameType() : GameType.CREATIVE;
    }
    
    public void registerItemRenderer(Item item, int meta, String id) {
    	ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

}
