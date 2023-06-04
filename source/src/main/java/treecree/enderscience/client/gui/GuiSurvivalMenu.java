package treecree.enderscience.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiButtonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import treecree.enderscience.client.gui.elements.GuiSurvivalMorphs;
import treecree.enderscience.client.gui.elements.GuiSurvivalMorphs.MorphCell;

/**
 * Survival morph menu GUI
 * 
 * This is menu which allows users to manage their acquired morphs.
 */
public class GuiSurvivalMenu extends GuiBase
{
    private GuiButtonElement<GuiButton> close;
    private GuiButtonElement<GuiButton> remove;
    private GuiButtonElement<GuiButton> morph;

    private GuiSurvivalMorphs morphs;

    /* Initiate GUI */

    public GuiSurvivalMenu(GuiSurvivalMorphs morphs)
    {
        this.morphs = morphs;
        this.morphs.inGUI = true;

        Minecraft mc = Minecraft.getMinecraft();

        this.remove = GuiButtonElement.button(mc, I18n.format("metamorph.gui.remove"), (b) ->
        {
            this.morphs.remove();
            this.updateButtons();
        });

        this.close = GuiButtonElement.button(mc, I18n.format("metamorph.gui.close"), (b) -> this.exit());

        this.morph = GuiButtonElement.button(mc, I18n.format("metamorph.gui.morph"), (b) ->
        {
            this.morphs.selectCurrent();
            this.exit();
        });

        this.remove.resizer().parent(this.area).set(20, 0, 60, 20).y(1, -30);
        this.morph.resizer().parent(this.area).set(0, 0, 60, 20).x(1, -80).y(1, -30);

        this.close.resizer().parent(this.area).set(0, 10, 60, 20).x(1, -80);

        this.elements.add(this.remove, this.close, this.morph);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.updateButtons();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.morphs.clickMorph(mouseX, mouseY, this.width, this.height);
        this.updateButtons();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == Keyboard.KEY_ESCAPE)
        {
            this.exit();
        }

        super.keyTyped(typedChar, keyCode);
    }

    /**
     * Exit from this GUI 
     */
    private void exit()
    {
        this.morphs.exitGUI();
        this.mc.displayGuiScreen(null);
    }

    public void updateButtons()
    {
        int index = this.morphs.index;

        this.remove.button.enabled = index >= 0;
    }

    /* Drawing code */

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        /* Background and stuff */
        this.drawDefaultBackground();
        Gui.drawRect(0, 0, width, 40, 0x88000000);
        this.drawString(this.fontRenderer, I18n.format("metamorph.gui.survival_title"), 20, 16, 0xffffff);

        this.morphs.render(this.width, this.height);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}