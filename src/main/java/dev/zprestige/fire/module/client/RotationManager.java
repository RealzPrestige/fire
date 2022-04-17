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
    public final Switch rotateToTargetOnIdle = Menu.Switch("Rotate To Target On Idle", false);
    public final Slider targetRange = Menu.Slider("Target Range", 9.0f, 0.1f, 15.0f).visibility(z -> rotateToTargetOnIdle.GetSwitch());
    public final ComboBox targetPriority = Menu.ComboBox("Target Priority", "UnSafe", new String[]{
            "Range",
            "UnSafe",
            "Health",
            "Fov",
    }).visibility(z -> rotateToTargetOnIdle.GetSwitch());

    public RotationManager() {
        enableModule();
    }

    @RegisterListener
    public void onTick(final TickEvent event) {
        Main.rotationManager.setMax((int) maxRotations.GetSlider());
        if (rotateToTargetOnIdle.GetSwitch() && !Main.rotationManager.needsRotations()) {
            final PlayerManager.Player player = EntityUtil.getClosestTarget(targetPriority(targetPriority.GetCombo()), targetRange.GetSlider());
            if (player != null) {
                Main.rotationManager.faceEntity(player.getEntityPlayer(), true);
            }
        }
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

    protected EntityUtil.TargetPriority targetPriority(final String string) {
        return Arrays.stream(EntityUtil.TargetPriority.values()).filter(targetPriority1 -> targetPriority1.toString().equals(string)).findFirst().orElse(null);
    }

}
