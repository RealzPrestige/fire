package dev.zprestige.fire.module.movement;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.PacketEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.module.exploit.Burrow;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Descriptor(description = "Cancels packets and releases them when disabled")
public class Blink extends Module {
    public final ComboBox mode = Menu.ComboBox("Mode", "Blink", new String[]{
            "Blink",
            "Pulse"
    });
    public final Switch cPacketPlayer = Menu.Switch("CPacketPlayer", false);
    public final Slider ticks = Menu.Slider("Ticks", 10.0f, 1.0f, 20.0f).visibility(z -> mode.GetCombo().equals("Pulse"));
    public final Switch burrowOnFinish = Menu.Switch("Burrow On Finish", false).visibility(z -> mode.GetCombo().equals("Blink"));
    protected final Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
    protected EntityOtherPlayerMP entity;
    protected int i;

    @Override
    public void onEnable() {
        entity = null;
        spawnEntity();
    }

    @Override
    public void onDisable() {
        poll(false);
    }

    @RegisterListener
    public void onTick(final TickEvent event) {
        if (mode.GetCombo().equals("Pulse")) {
            i++;
        }
        if (i >= ticks.GetSlider()) {
            poll(true);
            spawnEntity();
            i = 0;
        }
    }

    @RegisterListener
    public void onPacketSend(final PacketEvent.PacketSendEvent event) {
        if (nullCheck() && !mc.isSingleplayer()) {
            final Packet<?> packet = event.getPacket();
            if (cPacketPlayer.getValue() && packet instanceof CPacketPlayer) {
                event.setCancelled(true);
                packets.add(packet);
            }
            if (!cPacketPlayer.getValue() && !(packet instanceof CPacketChatMessage || packet instanceof CPacketConfirmTeleport || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus)) {
                packets.add(packet);
                event.setCancelled(true);
            }
        }
    }

    @RegisterListener
    public void onRender3D(final FrameEvent.FrameEvent3D event) {
        if (entity != null) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 0.2f);
            RenderUtil.renderEntity(entity, event.getPartialTicks());
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    protected void spawnEntity() {
        final EntityOtherPlayerMP entity = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        entity.copyLocationAndAnglesFrom(mc.player);
        entity.rotationYawHead = mc.player.rotationYawHead;
        entity.prevRotationYawHead = mc.player.rotationYawHead;
        entity.rotationYaw = mc.player.rotationYaw;
        entity.prevRotationYaw = mc.player.rotationYaw;
        entity.rotationPitch = mc.player.rotationPitch;
        entity.prevRotationPitch = mc.player.rotationPitch;
        entity.cameraYaw = mc.player.rotationYaw;
        entity.cameraPitch = mc.player.rotationPitch;
        entity.limbSwing = mc.player.limbSwing;
        this.entity = entity;
    }

    protected void poll(final boolean reEnable) {
        if (nullCheck()) {
            if (entity != null) {
                mc.world.removeEntity(entity);
            }
            while (!packets.isEmpty()) {
                mc.player.connection.sendPacket(packets.poll());
            }
            if (mode.GetCombo().equals("Blink") && burrowOnFinish.GetSwitch()) {
                final Burrow burrow = (Burrow) Main.moduleManager.getModuleByClass(Burrow.class);
                burrow.enableModule();
            }
            if (reEnable){
                disableModule();
                enableModule();
            }
        }
    }

}
