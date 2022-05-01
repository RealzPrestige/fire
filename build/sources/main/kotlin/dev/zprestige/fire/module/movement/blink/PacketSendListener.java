package dev.zprestige.fire.module.movement.blink;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

public class PacketSendListener extends EventListener<PacketEvent.PacketSendEvent, Blink> {

    public PacketSendListener(final Blink blink) {
        super(PacketEvent.PacketSendEvent.class, blink);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketSendEvent event = (PacketEvent.PacketSendEvent) object;
        if (module.nullCheck() && !mc.isSingleplayer()) {
            final Packet<?> packet = event.getPacket();
            if (module.cPacketPlayer.getValue() && packet instanceof CPacketPlayer) {
                event.setCancelled();
                module.packets.add(packet);
            }
            if (!module.cPacketPlayer.getValue() && !(packet instanceof CPacketChatMessage || packet instanceof CPacketConfirmTeleport || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus)) {
                module.packets.add(packet);
                event.setCancelled();
            }
        }
    }
}
