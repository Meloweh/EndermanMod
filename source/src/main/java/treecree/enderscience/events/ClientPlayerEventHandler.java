package treecree.enderscience.events;

import treecree.enderscience.EnderScience;
import treecree.enderscience.api.MorphAPI;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.capabilities.player.CapabilityFlags;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.capabilities.player.IEnderScienceCap;
import treecree.enderscience.effects.Effects;
import treecree.enderscience.network.Dispatcher;
import treecree.enderscience.network.common.PacketMorph;
import treecree.enderscience.network.common.PacketPotionEffects;
import treecree.enderscience.potions.init.PotionsInit;
import treecree.enderscience.thread.DefaultTimeOut;
import treecree.enderscience.util.TeleportEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientPlayerEventHandler {

}
