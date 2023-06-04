package treecree.enderscience.factory.editors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.client.gui.editor.GuiAbstractMorph;
import treecree.enderscience.factory.morphs.PlayerMorph;

@SideOnly(Side.CLIENT)
public class GuiPlayerMorph extends GuiAbstractMorph<PlayerMorph>
{
    public GuiUsernamePanel username;

    public GuiPlayerMorph(Minecraft mc)
    {
        super(mc);

        this.defaultPanel = this.username = new GuiUsernamePanel(mc, this);
        this.registerPanel(this.username, GuiAbstractMorph.PANEL_ICONS, I18n.format("metamorph.gui.panels.username"), 16, 0, 16, 16);
    }

    @Override
    public boolean canEdit(AbstractMorph morph)
    {
        return morph instanceof PlayerMorph;
    }
}