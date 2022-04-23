package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {
    protected final Packet<?> packet;

    public PacketEvent(final Packet<?> packet) {
        super(Stage.None, true);
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public static class PacketReceiveEvent extends PacketEvent {
        public PacketReceiveEvent(final Packet<?> packet) {
            super(packet);
        }
    }

    public static class PacketSendEvent extends PacketEvent {
        public PacketSendEvent(final Packet<?> packet) {
            super(packet);
        }
    }
}