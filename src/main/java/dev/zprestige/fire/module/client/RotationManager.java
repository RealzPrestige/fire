package dev.zprestige.fire.module.client;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.PacketEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.manager.PlayerManager;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

import java.util.Arrays;
import java.util.Set;

@Descriptor(description = "Controls the max amount of rotations the client can send per tick")
public class RotationManager extends Module {
    public final Slider maxRotations = Menu.Slider("Max Rotations", 2.0f, 0.1f, 10.0f);
    public final Switch noForceRotations = Menu.Switch("No Force Rotations", false);

    public RotationManager() {
        enableModule();
    }

    @Override
    public String getData() {
        return maxRotations.GetSlider() + "";
    }

    @RegisterListener
    public void onPacketReceive(final PacketEvent.PacketReceiveEvent event) {
        if (noForceRotations.GetSwitch() && event.getPacket() instanceof SPacketPlayerPosLook && mc.currentScreen == null) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
            final Set<SPacketPlayerPosLook.EnumFlags> flags = packet.getFlags();
            flags.remove(SPacketPlayerPosLook.EnumFlags.X_ROT);
            flags.remove(SPacketPlayerPosLook.EnumFlags.Y_ROT);
        }
    }
}
