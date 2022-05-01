package dev.zprestige.fire.module.client.rotationmanager;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.PacketEvent;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

import java.util.Set;

public class PacketReceiveListener extends EventListener<PacketEvent.PacketReceiveEvent, RotationManager> {

    public PacketReceiveListener(final RotationManager rotationManager) {
        super(PacketEvent.PacketReceiveEvent.class, rotationManager);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketReceiveEvent event = (PacketEvent.PacketReceiveEvent) object;
        if (module.noForceRotations.GetSwitch() && event.getPacket() instanceof SPacketPlayerPosLook && mc.currentScreen == null) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
            final Set<SPacketPlayerPosLook.EnumFlags> flags = packet.getFlags();
            flags.remove(SPacketPlayerPosLook.EnumFlags.X_ROT);
            flags.remove(SPacketPlayerPosLook.EnumFlags.Y_ROT);
        }
    }
}
