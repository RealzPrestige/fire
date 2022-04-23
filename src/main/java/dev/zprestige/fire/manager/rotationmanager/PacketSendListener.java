package dev.zprestige.fire.manager.rotationmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.PacketEvent;
import net.minecraft.network.play.client.CPacketPlayer;

public class PacketSendListener extends EventListener<PacketEvent.PacketSendEvent, Object> {

    public PacketSendListener(){
        super(PacketEvent.PacketSendEvent.class);
    }

    @Override
    public void invoke(final Object object){
        final PacketEvent.PacketSendEvent event = (PacketEvent.PacketSendEvent) object;
        if (event.getPacket() instanceof CPacketPlayer.Rotation) {
            Main.rotationManager.rotationsPerTick.add(System.currentTimeMillis() + 50L);
        }
    }
}
