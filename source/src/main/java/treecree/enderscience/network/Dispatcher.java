package treecree.enderscience.network;

import mchorse.mclib.network.AbstractDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import treecree.enderscience.EnderScience;
import treecree.enderscience.network.client.ClientHandlerAcquireMorph;
import treecree.enderscience.network.client.ClientHandlerAcquiredMorphs;
import treecree.enderscience.network.client.ClientHandlerAddEffects;
import treecree.enderscience.network.client.ClientHandlerMorph;
import treecree.enderscience.network.client.ClientHandlerMorphPlayer;
import treecree.enderscience.network.client.ClientHandlerMorphState;
import treecree.enderscience.network.client.ClientHandlerRemoveMorph;
import treecree.enderscience.network.client.ClientHandlerSettings;
import treecree.enderscience.network.client.ClientHandlerPlayerDefaults;
import treecree.enderscience.network.client.ClientHandlerPotionEffects;
import treecree.enderscience.network.common.PacketAcquireMorph;
import treecree.enderscience.network.common.PacketAcquiredMorphs;
import treecree.enderscience.network.common.PacketAction;
import treecree.enderscience.network.common.PacketAddEffects;
import treecree.enderscience.network.common.PacketMorph;
import treecree.enderscience.network.common.PacketMorphPlayer;
import treecree.enderscience.network.common.PacketMorphState;
import treecree.enderscience.network.common.PacketPlayerDefaults;
import treecree.enderscience.network.common.PacketPotionEffects;
import treecree.enderscience.network.common.PacketRemoveMorph;
import treecree.enderscience.network.common.PacketSelectMorph;
import treecree.enderscience.network.common.PacketSettings;
import treecree.enderscience.network.server.ServerHandlerAcquireMorph;
import treecree.enderscience.network.server.ServerHandlerAction;
import treecree.enderscience.network.server.ServerHandlerMorph;
import treecree.enderscience.network.server.ServerHandlerPlayerDefaults;
import treecree.enderscience.network.server.ServerHandlerPotionEffects;
import treecree.enderscience.network.server.ServerHandlerRemoveMorph;
import treecree.enderscience.network.server.ServerHandlerSelectMorph;
import treecree.enderscience.references.References;

public class Dispatcher {
	public static final AbstractDispatcher DISPATCHER = new AbstractDispatcher(References.MOD_ID) {
		@Override
		public void register() {
			register(PacketAction.class, ServerHandlerAction.class, Side.SERVER);

			register(PacketMorph.class, ClientHandlerMorph.class, Side.CLIENT);
			register(PacketMorph.class, ServerHandlerMorph.class, Side.SERVER);
			register(PacketMorphPlayer.class, ClientHandlerMorphPlayer.class, Side.CLIENT);

			register(PacketAcquireMorph.class, ClientHandlerAcquireMorph.class, Side.CLIENT);
			register(PacketAcquireMorph.class, ServerHandlerAcquireMorph.class, Side.SERVER);
			register(PacketAcquiredMorphs.class, ClientHandlerAcquiredMorphs.class, Side.CLIENT);

			register(PacketSelectMorph.class, ServerHandlerSelectMorph.class, Side.SERVER);

			register(PacketMorphState.class, ClientHandlerMorphState.class, Side.CLIENT);

			register(PacketRemoveMorph.class, ClientHandlerRemoveMorph.class, Side.CLIENT);
			register(PacketRemoveMorph.class, ServerHandlerRemoveMorph.class, Side.SERVER);

			register(PacketSettings.class, ClientHandlerSettings.class, Side.CLIENT);

			register(PacketPlayerDefaults.class, ClientHandlerPlayerDefaults.class, Side.CLIENT);
			register(PacketPlayerDefaults.class, ServerHandlerPlayerDefaults.class, Side.SERVER);
			
			register(PacketAddEffects.class, ClientHandlerAddEffects.class, Side.CLIENT);
			
			register(PacketPotionEffects.class, ClientHandlerPotionEffects.class, Side.CLIENT);
			register(PacketPotionEffects.class, ServerHandlerPotionEffects.class, Side.SERVER);
		}
	};

	public static void sendToTracked(Entity entity, IMessage message) {
		DISPATCHER.sendToTracked(entity, message);
	}

	public static void sendTo(IMessage message, EntityPlayerMP player) {
		DISPATCHER.sendTo(message, player);
	}

	public static void sendToServer(IMessage message) {
		DISPATCHER.sendToServer(message);
	}

	public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		DISPATCHER.get().sendToAllAround(message, point);
	}

	public static void register() {
		DISPATCHER.register();
	}
}