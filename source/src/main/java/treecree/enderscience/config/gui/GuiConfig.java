package treecree.enderscience.config.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.EnderScience;
import treecree.enderscience.references.References;

/**
 * Config GUI
 *
 * This config GUI is responsible for managing Blockbuster's config. Most of
 * the code that implements config features is located in the parent of the
 * class.
 */
@SideOnly(Side.CLIENT)
public class GuiConfig extends net.minecraftforge.fml.client.config.GuiConfig
{
    public GuiConfig(GuiScreen parent)
    {
        super(parent, getConfigElements(), References.MOD_ID, false, false, "Metamorph");
    }

    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> elements = new ArrayList<IConfigElement>();

        for (String name : EnderScience.proxy.forge.getCategoryNames())
        {
            ConfigCategory category = EnderScience.proxy.forge.getCategory(name);

            category.setLanguageKey("metamorph.config." + name + ".title");
            elements.add(new ConfigElement(category));
        }

        return elements;
    }
}