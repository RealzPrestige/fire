package dev.zprestige.fire.event;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.ChorusEvent;
import dev.zprestige.fire.event.impl.PacketEvent;
import dev.zprestige.fire.event.impl.TotemPopEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;

public class PacketReceiveListener extends EventListener<PacketEvent, Object> {

    public PacketReceiveListener() {
        super(PacketEvent.PacketReceiveEvent.class);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketReceiveEvent event = (PacketEvent.PacketReceiveEvent) object;
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getSound().equals(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT)) {
                final double x = packet.getX(), y = packet.getY(), z = packet.getZ();
                if (mc.player.getDistanceSq(new BlockPos(x, y, z)) > 1.0f) {
                    final ChorusEvent chorusEvent = new ChorusEvent(x, y, z);
                    Main.eventBus.invokeEvent(chorusEvent);
                }
            }
        }
        if (mc.world != null && mc.player != null && event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            final Entity entity = packet.getEntity(mc.world);
            if (entity instanceof EntityPlayer && packet.getOpCode() == 35) {
                final TotemPopEvent totemPopEvent = new TotemPopEvent((EntityPlayer) entity);
                Main.eventBus.invokeEvent(totemPopEvent);
            }
        }
    }
}
