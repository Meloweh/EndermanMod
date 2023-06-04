package treecree.enderscience.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import treecree.enderscience.EnderScience;

public class ItemBase extends Item implements IHasModel{

	public ItemBase(final String name, final CreativeTabs tab) {
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(tab);
		
		Items.ITEMS.add(this);
	}
	
	@Override
	public void registerModels() {
		EnderScience.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
