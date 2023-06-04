package treecree.enderscience.potions.init;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import treecree.enderscience.potions.EnderMorphPotion;
import treecree.enderscience.references.MorphReference;
import treecree.enderscience.references.References;

public class PotionsInit {
	public static final String enderMorphPotion = "ender_morph_potion";
	public static final String longEnderMorphPotion = "long_ender_morph_potion";
	public static final Potion ENDER_MORPH_POTION_EFFECT = new EnderMorphPotion(enderMorphPotion, false, 2424996, 0, 0);

	public static final PotionType ENDER_MORPH_POTION = new PotionType(enderMorphPotion, new PotionEffect[] {new PotionEffect(ENDER_MORPH_POTION_EFFECT, MorphReference.MORPH_DURATION_ENDERMAN * 20, 0, false, false)}).setRegistryName(new ResourceLocation(References.MOD_ID, enderMorphPotion));
	public static final PotionType LONG_ENDER_MORPH_POTION = new PotionType(longEnderMorphPotion, new PotionEffect[] {new PotionEffect(ENDER_MORPH_POTION_EFFECT, MorphReference.LONG_MORPH_DURATION_ENDERMAN * 20, 0, false, false)}).setRegistryName(new ResourceLocation(References.MOD_ID, longEnderMorphPotion));

	public static void registerPotions() {
		registerPotion(ENDER_MORPH_POTION, LONG_ENDER_MORPH_POTION, ENDER_MORPH_POTION_EFFECT);
		registerPotionMixes();
	}
	
	private static void registerPotion(PotionType potionType, PotionType longPotionType, Potion effect) {
		ForgeRegistries.POTIONS.register(effect);
		
		ForgeRegistries.POTION_TYPES.register(potionType);
		ForgeRegistries.POTION_TYPES.register(longPotionType);
	}
	
	private static void registerPotion(PotionType potionType, Potion effect) {
		ForgeRegistries.POTIONS.register(effect);
		
		ForgeRegistries.POTION_TYPES.register(potionType);
	}
	
	private static void registerPotionMixes() {
		PotionHelper.addMix(PotionTypes.AWKWARD, treecree.enderscience.items.Items.CHORUS_MIXTURE, ENDER_MORPH_POTION);
		PotionHelper.addMix(ENDER_MORPH_POTION, Items.REDSTONE, LONG_ENDER_MORPH_POTION);
	}
}
