package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import dev.zprestige.fire.events.eventbus.event.IsCancellable;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PacketEvent extends Event {

    protected final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    @IsCancellable
    public static class PacketReceiveEvent extends PacketEvent {
        public PacketReceiveEvent(Packet<?> packet) {
            super(packet);
        }
    }

    @IsCancellable
    public static class PacketSendEvent extends PacketEvent {
        public PacketSendEvent(Packet<?> packet) {
            super(packet);
        }
    }
}
