package treecree.enderscience.client.gui.editor;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.api.morphs.AbstractMorph;

@SideOnly(Side.CLIENT)
@SuppressWarnings("rawtypes")
public class GuiMorphPanel<T extends AbstractMorph, E extends GuiAbstractMorph> extends GuiElement
{
    public E editor;
    public T morph;

    public GuiMorphPanel(Minecraft mc, E editor)
    {
        super(mc);
        this.createChildren();

        this.editor = editor;
    }

    public void startEditing()
    {}

    public void finishEditing()
    {}

    public void fillData(T morph)
    {
        this.morph = morph;
    }
}