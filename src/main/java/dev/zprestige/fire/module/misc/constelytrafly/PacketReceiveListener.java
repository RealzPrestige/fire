package dev.zprestige.fire.module.misc.constelytrafly;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.PacketEvent;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class PacketReceiveListener extends EventListener<PacketEvent.PacketReceiveEvent, ConstElytraFly> {

    public PacketReceiveListener(final ConstElytraFly constElytraFly){
        super(PacketEvent.PacketReceiveEvent.class, constElytraFly);
    }

    @Override
    public void invoke(final Object object){
        final PacketEvent.PacketReceiveEvent event = (PacketEvent.PacketReceiveEvent) object;
        if (event.getPacket() instanceof SPacketPlayerPosLook){
            module.jumpTimer.syncTime();
        }
    }
}
