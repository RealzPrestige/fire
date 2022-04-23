package dev.zprestige.fire.events;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.PacketEvent;
import dev.zprestige.fire.newbus.events.TotemPopEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;

public class PacketReceiveListener extends EventListener<PacketEvent, Object> {

    public PacketReceiveListener() {
        super(PacketEvent.PacketReceiveEvent.class);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketReceiveEvent event = (PacketEvent.PacketReceiveEvent) object;
        if (mc.world != null && mc.player != null && event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            final Entity entity = packet.getEntity(mc.world);
            if (entity instanceof EntityPlayer && packet.getOpCode() == 35) {
                final TotemPopEvent totemPopEvent = new TotemPopEvent((EntityPlayer) entity);
                Main.newBus.invokeEvent(totemPopEvent);
            }
        }
    }
}
