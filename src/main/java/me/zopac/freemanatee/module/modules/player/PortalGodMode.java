package me.zopac.freemanatee.module.modules.player;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zopac.freemanatee.event.events.PacketEvent;
import me.zopac.freemanatee.module.Module;
import net.minecraft.network.play.client.CPacketConfirmTeleport;

@Module.Info(name = "PortalGodMode", category = Module.Category.PLAYER)
public class PortalGodMode extends Module {

    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (isEnabled() && event.getPacket() instanceof CPacketConfirmTeleport) {
            event.cancel();
        }
    });
}
