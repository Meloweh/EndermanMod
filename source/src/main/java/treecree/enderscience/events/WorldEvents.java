package treecree.enderscience.events;

import java.util.Random;

import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import treecree.enderscience.api.MorphAPI;
import treecree.enderscience.api.MorphManager;
import treecree.enderscience.api.morphs.AbstractMorph;
import treecree.enderscience.capabilities.CapabilityHandler;
import treecree.enderscience.capabilities.morphing.IMorphing;
import treecree.enderscience.capabilities.morphing.Morphing;
import treecree.enderscience.capabilities.morphing.MorphingProvider;
import treecree.enderscience.capabilities.player.EnderScienceCapabilities;
import treecree.enderscience.capabilities.player.IEnderScienceCap;
import treecree.enderscience.items.Items;
import treecree.enderscience.network.Dispatcher;
import treecree.enderscience.network.common.PacketMorph;
import treecree.enderscience.network.common.PacketPlayerDefaults;
import treecree.enderscience.network.common.PacketPotionEffects;
import treecree.enderscience.network.common.PacketSelectMorph;
import treecree.enderscience.potions.init.PotionsInit;
import treecree.enderscience.references.MorphReference;
import treecree.enderscience.references.NameReference;
import treecree.enderscience.references.ResourceReference;

public class WorldEvents {
	
    public final static ResourceLocation resource = new ResourceLocation(NameReference.DOMAIN_MINECRAFT, "enderman");

    public final static NBTTagCompound tag = new NBTTagCompound();
    
    private final Random rand;
    
    public WorldEvents() {
        tag.setString(ResourceReference.RESOURCE_DOMAIN, resource.getResourceDomain());
        tag.setString(ResourceReference.RESOURCE_PATH, resource.getResourcePath());
        
        rand = new Random();
    }
    
    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event) {
    	if(event.getEntity() instanceof EntityEnderman) {
    		if(rand.nextInt(7) == 0) {
    			final Entity entity = event.getEntity();
    			EntityItem item = new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ,  new ItemStack(Items.ENDERMAN_SCALE, 1 + rand.nextInt(2)));
    			entity.world.spawnEntity(item);
    		}
    	}
    }
	
	@SubscribeEvent
	public void onPotionActive(PlayerTickEvent event) {
		
		IMorphing cap = Morphing.get(event.player);
		
		if(event.player.isPotionActive(PotionsInit.ENDER_MORPH_POTION_EFFECT)) {
	        
	        if(event.player.getActivePotionEffect(PotionsInit.ENDER_MORPH_POTION_EFFECT).getDuration() > 1) {
	        	
	        	final AbstractMorph morph = MorphManager.INSTANCE.morphFromNBT(tag);
	        	
	        	if(!cap.isMorphed()) {
		        	if(!cap.acquiredMorph(morph)) {
		        		cap.acquireMorph(morph);
		        	}
	        	
	        		MorphAPI.morph(event.player, morph, true);
	        	}
	        	
		        //AbstractMorph morph = cap.getCurrentMorph();
		        //mchorse.metamorph.api.morphs.EntityMorph entityMorph = (mchorse.metamorph.api.morphs.EntityMorph)morph;
		        //event.player.addPotionEffect(new PotionEffect(Potion.getPotionById(9), 20 * 3));
	        } 
		} else {
			if(cap.isMorphed() && event.side == Side.SERVER) {
				MorphAPI.demorph(event.player);
				Dispatcher.sendTo(new PacketMorph(null), (EntityPlayerMP)event.player);
			}
		}
	}
}
