package treecree.enderscience.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import treecree.enderscience.references.References;

public class EnderMorphPotion extends Potion{

	public EnderMorphPotion(String name, boolean isBadEffectIn, int liquidColorIn, int iconIndexX, int iconIndexY) {
		super(isBadEffectIn, liquidColorIn);
		this.setPotionName("effect." + name);
		this.setIconIndex(iconIndexX, iconIndexY);
		this.setRegistryName(References.MOD_ID, name);
	}
	
	
	@Override
	public boolean hasStatusIcon() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(References.MOD_ID, "textures/gui/ender_morph_effect.png"));
		return true;
	}

}
