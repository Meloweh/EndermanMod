package treecree.enderscience.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Items {
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	//public static final Item ENDER_CHUNK = new ItemBase("ender_chunk");
	
	public static final Item ENDERMAN_SCALE = new ItemBase("enderman_scale", CreativeTabs.MATERIALS);
	
	public static final Item GRINDED_ENDERMAN_SCALE = new ItemBase("grinded_enderman_scale", CreativeTabs.MATERIALS);
	
	public static final Item PURIFIED_CHORUS_POWDER = new ItemBase("purified_chorus_powder", CreativeTabs.MATERIALS);
	
	public static final Item CHORUS_MIXTURE = new ItemBase("chorus_mixture", CreativeTabs.BREWING);

}
