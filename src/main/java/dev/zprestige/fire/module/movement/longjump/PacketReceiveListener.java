package dev.zprestige.fire.module.movement.longjump;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.PacketEvent;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class PacketReceiveListener extends EventListener<PacketEvent.PacketReceiveEvent, LongJump> {

    public PacketReceiveListener(final LongJump longJump){
        super(PacketEvent.PacketReceiveEvent.class, longJump);
    }

    @Override
    public void invoke(final Object object){
        final PacketEvent.PacketReceiveEvent event = (PacketEvent.PacketReceiveEvent) object;
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            if (module.disableOnLag.GetSwitch()) {
                module.disableModule();
            }
            module.motionSpeed = 0.0f;
            mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());
        }
    }
}
