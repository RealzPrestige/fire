package dev.zprestige.fire.module.misc.removeexpdesync;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.PacketEvent;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;

public class PacketSendListener extends EventListener<PacketEvent.PacketSendEvent, RemoveEXPDesync> {

    public PacketSendListener(final RemoveEXPDesync removeEXPDesync) {
        super(PacketEvent.PacketSendEvent.class, removeEXPDesync);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketSendEvent event = (PacketEvent.PacketSendEvent) object;
        if (event.getPacket() instanceof CPacketPlayerTryUseItem) {
            final CPacketPlayerTryUseItem packet = (CPacketPlayerTryUseItem) event.getPacket();
            if (module.isEXP(packet.getHand())) {
                module.started = true;
            }
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
            if (module.isEXP(packet.getHand())) {
                module.started = true;
            }
        }
    }
}
