package me.zopac.freemanatee.module.modules.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zopac.freemanatee.event.KamiEvent;
import me.zopac.freemanatee.event.events.EntityEvent;
import me.zopac.freemanatee.event.events.PacketEvent;
import me.zopac.freemanatee.module.Module;
import me.zopac.freemanatee.setting.Setting;
import me.zopac.freemanatee.setting.Settings;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

@Module.Info(
        name = "Velocity",
        category = Module.Category.MOVEMENT
)
public class Velocity extends Module {

    private Setting<Float> horizontal = register(Settings.f("Horizontal", 0));
    private Setting<Float> vertical = register(Settings.f("Vertical", 0));

    @EventHandler
    private Listener<PacketEvent.Receive> packetEventListener = new Listener<>(event -> {
        if (event.getEra() == KamiEvent.Era.PRE) {
            if (event.getPacket() instanceof SPacketEntityVelocity) {
                SPacketEntityVelocity velocity = (SPacketEntityVelocity) event.getPacket();
                if (velocity.getEntityID() == mc.player.entityId) {
                    if (horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
                    velocity.motionX *= horizontal.getValue();
                    velocity.motionY *= vertical.getValue();
                    velocity.motionZ *= horizontal.getValue();
                }
            } else if (event.getPacket() instanceof SPacketExplosion) {
                if (horizontal.getValue() == 0 && vertical.getValue() == 0) event.cancel();
                SPacketExplosion velocity = (SPacketExplosion) event.getPacket();
                velocity.motionX *= horizontal.getValue();
                velocity.motionY *= vertical.getValue();
                velocity.motionZ *= horizontal.getValue();
            }
        }
    });

    @EventHandler
    private Listener<EntityEvent.EntityCollision> entityCollisionListener = new Listener<>(event -> {
        if (event.getEntity() == mc.player) {
            if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
                event.cancel();
                return;
            }
            event.setX(-event.getX() * horizontal.getValue());
            event.setY(0);
            event.setZ(-event.getZ() * horizontal.getValue());
        }
    });

}
