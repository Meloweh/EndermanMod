package treecree.enderscience.network.client;

import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.client.gui.GuiSurvivalMenu;
import treecree.enderscience.network.common.PacketRemoveMorph;

public class ClientHandlerRemoveMorph extends ClientMessageHandler<PacketRemoveMorph>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketRemoveMorph message)
    {
        Morphing.get(player).remove(message.index);

        if (Minecraft.getMinecraft().currentScreen instanceof GuiSurvivalMenu)
        {
            ((GuiSurvivalMenu) Minecraft.getMinecraft().currentScreen).updateButtons();
        }
    }
}