package dev.zprestige.fire.module.player.freecam;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.PacketEvent;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;

public class PacketSendListener extends EventListener<PacketEvent.PacketSendEvent, Freecam> {

    public PacketSendListener(final Freecam freecam) {
        super(PacketEvent.PacketSendEvent.class, freecam);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketSendEvent event = (PacketEvent.PacketSendEvent) object;
        if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput) {
            event.setCancelled();
        }
    }
}
