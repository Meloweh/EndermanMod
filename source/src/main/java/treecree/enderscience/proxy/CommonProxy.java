package treecree.enderscience.proxy;

import treecree.enderscience.EnderScience;
import treecree.enderscience.api.MorphHandler;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.MorphUtils;
import treecree.enderscience.capabilities.CapabilityHandler;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.capabilities.morphing.MorphingStorage;
import treecree.enderscience.config.MetamorphConfig;
import treecree.enderscience.entity.EntityMorph;
import treecree.enderscience.entity.SoundHandler;
import treecree.enderscience.events.CapabilitySyncHandler;
import treecree.enderscience.events.InputEventHandler;
import treecree.enderscience.events.PlayerEventHandler;
import treecree.enderscience.events.WorldEvents;
import treecree.enderscience.factory.MobMorphFactory;
import treecree.enderscience.factory.PlayerMorphFactory;
import treecree.enderscience.factory.RegisterHandler;
import treecree.enderscience.factory.abilities.ExplosiveDodge;
import treecree.enderscience.factory.abilities.LavaDodge;
import treecree.enderscience.factory.abilities.MagicDamageDodge;
import treecree.enderscience.factory.abilities.ProjectileDodge;
import treecree.enderscience.factory.sounds.EndermanSound;
import treecree.enderscience.items.ItemRegistryHandler;
import treecree.enderscience.network.Dispatcher;
import treecree.enderscience.potions.init.PotionsInit;
import treecree.enderscience.references.NameReference;
import treecree.enderscience.references.References;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
	public MetamorphConfig config;
	public Configuration forge;
	public File morphs;

	public void preLoad(FMLPreInitializationEvent event) {
		Dispatcher.register();

		MorphManager.INSTANCE.factories.add(new MobMorphFactory());
		MorphManager.INSTANCE.factories.add(new PlayerMorphFactory());

		File config = new File(event.getModConfigurationDirectory(), References.MOD_ID + "/config.cfg");
		File morphs = new File(event.getModConfigurationDirectory(), References.MOD_ID + "/models.json");

		this.forge = new Configuration(config);
		this.config = new MetamorphConfig(this.forge);
		this.morphs = morphs;

		EntityRegistry.registerModEntity(new ResourceLocation(References.MOD_ID, NameReference.ENTITY_MORPH_NAME),
				EntityMorph.class, NameReference.ENTITY_MORPH_NAME, 0, EnderScience.instance, 64, 3, false);

		PotionsInit.registerPotions();
	}

	public void load() {

		MinecraftForge.EVENT_BUS.register(this.config);
		MinecraftForge.EVENT_BUS.register(new MorphHandler());
		MinecraftForge.EVENT_BUS.register(new SoundHandler());
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new RegisterHandler());

		CapabilityManager.INSTANCE.register(IMorphing.class, new MorphingStorage(), Morphing.class);

		RegisterHandler.registerAbilities(MorphManager.INSTANCE);
		MorphManager.INSTANCE.register();

		if (!morphs.exists()) {
			MorphUtils.generateFile(morphs, "{}");
		}

		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
		MinecraftForge.EVENT_BUS.register(new CapabilitySyncHandler());
		MinecraftForge.EVENT_BUS.register(new WorldEvents());
		//MinecraftForge.EVENT_BUS.register(new ItemRegistryHandler());

		MinecraftForge.EVENT_BUS.register(new EndermanSound());
		MinecraftForge.EVENT_BUS.register(new ProjectileDodge());
		MinecraftForge.EVENT_BUS.register(new ExplosiveDodge());
		MinecraftForge.EVENT_BUS.register(new LavaDodge());
		MinecraftForge.EVENT_BUS.register(new MagicDamageDodge());

		MinecraftForge.EVENT_BUS.register(this);
	}

	public void postLoad(FMLPostInitializationEvent event) {
	}

	public EntityPlayer getPlayerFromMessageContext(MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			return ctx.getServerHandler().player;
		} else {
			EnderScience.logger.warn("INVALID SIDE: getPlayerFromMessageContext = " + ctx.side);
		}
		throw new NullPointerException("INVALID SIDE: getPlayerFromMessageContext");
	}

	public void registerKeyBindings() {

	}

	public void playSound(int soundId, float pitch, float volume, boolean repeat, boolean stop, double x, double y,
			double z) {

	}

	public String format(String key, Object... args) {
		return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(key, args);
	}

	public boolean isShiftKeyDown() {
		return false;
	}

	public boolean isControlKeyDown() {
		return false;
	}

	public boolean isAltKeyDown() {
		return false;
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}

	public void registerRenderers() {
	}

	public void registerItemRenderer(Item item, int meta, String id) {
	}
}
