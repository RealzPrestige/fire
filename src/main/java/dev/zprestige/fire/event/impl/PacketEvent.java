package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;
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