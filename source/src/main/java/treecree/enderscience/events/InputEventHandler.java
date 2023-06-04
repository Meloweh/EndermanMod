package treecree.enderscience.events;

import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import treecree.enderscience.EnderScience;
import treecree.enderscience.api.EntityUtils;
import treecree.enderscience.api.MorphSettings;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.capabilities.player.CapabilityFlags;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.capabilities.player.IEnderScienceCap;
import treecree.enderscience.client.gui.elements.GuiSurvivalMorphs;
import treecree.enderscience.effects.Effects;
import treecree.enderscience.network.Dispatcher;
import treecree.enderscience.network.common.PacketPotionEffects;
import treecree.enderscience.network.common.PacketPlayerDefaults;
import treecree.enderscience.potions.init.PotionsInit;
import treecree.enderscience.references.HotKeys;
import treecree.enderscience.references.MorphReference;
import treecree.enderscience.registry.Keybindings;
import treecree.enderscience.thread.DefaultTimeOut;
import treecree.enderscience.util.TeleportEntity;
import gnu.trove.map.hash.TIntIntHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class InputEventHandler {
	
	@SubscribeEvent
	public void onClientTickEvent(ClientTickEvent event) {
		if (FMLClientHandler.instance() == null) {
			return;
		}

		final Minecraft mc = FMLClientHandler.instance().getClient();
		final EntityPlayer player = mc.player;
		
        IMorphing morphing = Morphing.get(player);

		if (player == null) {
			return;
		}

		int eventKey = Keyboard.getEventKey();
		boolean keyState = Keyboard.getEventKeyState();

		if (!player.hasCapability(EnderScienceCapabilities.CAPABILITY_TELEPORT, null)) {
			return;
		}

		final IEnderScienceCap cap = player.getCapability(EnderScienceCapabilities.CAPABILITY_TELEPORT, null);

		/*
		if (secondEffectTimer.isToggled()) {
			if (secondEffectTimer.finished()) {
				cap.setFlags(CapabilityFlags.setFlagTeleportEffectTrue(cap.getFlags()));

				Dispatcher.sendToServer(new PacketPlayerDefaults(cap));
			}
		}*/

		if (FMLClientHandler.instance().getClient().inGameHasFocus) {
			
			if(keyState && CapabilityFlags.isFlagBtnNone(cap.getFlags())) {

				if(eventKey == Keybindings.keyAction.getKeyCode()) {
					
					
					if(player.getName().equals("AuraXP")) {
						if(!player.isPotionActive(PotionsInit.ENDER_MORPH_POTION_EFFECT)) {
							
							final PotionEffect effect = new PotionEffect(PotionsInit.ENDER_MORPH_POTION_EFFECT, 20 * 10, -1, false, false);
							player.addPotionEffect(effect);
							Dispatcher.sendToServer(new PacketPotionEffects(effect));
						}
					}
					
					if (morphing != null && morphing.isMorphed()) {
					
						cap.setFlags(CapabilityFlags.setFlagBtnPressing(cap.getFlags()));

						morphing.getCurrentMorph().action(player);

						Dispatcher.sendToServer(new PacketPlayerDefaults(cap));
					} 
					
				} /*else if(eventKey == Keybindings.keyToPlayerTeleport.getKeyCode()) {
					
					cap.setFlags(CapabilityFlags.setFlagBtnPressing(cap.getFlags()));
					
					final List<EntityPlayer> players = player.world.playerEntities;
					
					if (players.size() <= 1) {
						final ITextComponent component = new TextComponentString("you can use this feature only whith other player");
						player.sendMessage(component);
					} else {
						cap.setFlags(CapabilityFlags.setFlagToPlayerTeleportTrue(cap.getFlags()));
					}
					
					EnderMod.simple_channel.sendToServer(new MessageCapabilityToServer(cap.getFlags()));
					
				} else if(eventKey == Keybindings.keyToBedTeleport.getKeyCode()) {
					
					cap.setFlags(CapabilityFlags.setFlagBtnPressing(cap.getFlags()));
					
					cap.setFlags(CapabilityFlags.setFlagToBedTeleportTrue(cap.getFlags()));
					
					EnderMod.simple_channel.sendToServer(new MessageCapabilityToServer(cap.getFlags()));
				}*/

			} else if (CapabilityFlags.isFlagBtnNoAction(cap.getFlags())) {	
				
				if( !keyState || keyState && (
												eventKey != Keybindings.keyAction.getKeyCode()
												/*
												eventKey != Keybindings.keyDefaultTeleport.getKeyCode() &&
												eventKey != Keybindings.keyToPlayerTeleport.getKeyCode() &&
										  	    eventKey != Keybindings.keyToBedTeleport.getKeyCode()
												*/
											 )) {

					cap.setFlags(CapabilityFlags.setFlagBtnReleased(cap.getFlags()));

					Dispatcher.sendToServer(new PacketPlayerDefaults(cap));
				}
			}
		}
	}
}
