package treecree.enderscience.client.gui.elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mchorse.mclib.client.gui.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.BossInfo.Color;
import treecree.enderscience.EnderScience;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.capabilities.player.IEnderScienceCap;
import treecree.enderscience.potions.init.PotionsInit;

/**
 * GUI acquired morph overlay class
 * 
 * This class is responsible for displaying acquired morph in a more graphic way
 * than with chat message.
 */
public class GuiOverlay2 extends Gui {
	/**
	 * List of acquired morphs
	 */
	public List<AcquiredMorph> morphs = new ArrayList<AcquiredMorph>();

	/**
	 * Disappearing cap
	 */
	public final int cap = 60;

	/**
	 * Render acquired morph overlays
	 */
	public void render(int width, int height) {
		if (this.morphs.size() == 0) {
			return;
		}

		Minecraft mc = Minecraft.getMinecraft();
		FontRenderer font = mc.fontRenderer;
		Iterator<AcquiredMorph> iterator = this.morphs.iterator();

		EntityPlayer player = mc.player;

		if (player == null) {
			EnderScience.logger.error("gui overlay 2 no playerSP");
			return;
		}
		
		int duration = 0;
		
		if(player.isPotionActive(PotionsInit.ENDER_MORPH_POTION_EFFECT)) {
		
			PotionEffect effect = player.getActivePotionEffect(PotionsInit.ENDER_MORPH_POTION_EFFECT);
	
			duration = Math.round(effect.getDuration() / 20);
		
		}

		/*
		if (!player.hasCapability(EnderModCapabilities.CAPABILITY_TELEPORT, null)) {
			EnderMod.logger.error("gui overlay 2 playerSP has no cap");
			return;
		}*/

		//ITeleportCapability capa = player.getCapability(EnderModCapabilities.CAPABILITY_TELEPORT, null);
		

		while (iterator.hasNext()) {
			AcquiredMorph morph = iterator.next();

			/* Let's calculate some stuff */
			boolean disappear = morph.timer <= this.cap;

			int progress = this.cap - morph.timer;
			int alpha = (int) (255 * morph.timer / this.cap);
			int y = height - 10 + (disappear ? (int) (40 * (float) progress / this.cap) : 0);
			int color = disappear ? 0x00ffffff + (alpha << 24) : 0xffffffff;

			String string = "remaining: " + ((int) (duration) / 60) + ":" + ((duration % 60 < 10) ? "0" : "") + (duration % 60);

			morph.morph.renderOnScreen(mc.player, 15, y, 15, (float) alpha / 255);

			font.drawString(string, 30, y - 7, color);

			if (duration <= 0) {
				morph.timer--;
			}

			if (morph.timer <= 0) {
				iterator.remove();
			}
		}
	}

	/**
	 * Add an acquired morph to this overlay.
	 */
	public void start(AbstractMorph acquired) {
		/*
		 * for (AcquiredMorph morph : this.morphs) { if (morph.timer > this.cap) {
		 * morph.timer = this.cap; } }
		 */

		if (this.morphs.isEmpty()) {
			this.morphs.add(new AcquiredMorph(acquired));
		}
	}

	/**
	 * Acquired morph class
	 * 
	 * This class is responsible for containing information about currently acquired
	 * morph.
	 */
	public static class AcquiredMorph {
		public AbstractMorph morph;
		public int timer = 60;

		public AcquiredMorph(AbstractMorph morph) {
			this.morph = morph;
		}
	}
}