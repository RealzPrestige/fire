package dev.zprestige.fire.module.visual;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FogEvent;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.PacketEvent;
import dev.zprestige.fire.events.impl.ParticleEvent;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.server.SPacketEffect;
import java.awt.*;

public class ViewTweaks extends Module {
    public final Slider fov = Menu.Slider("Fov", 130.0f, 50.0f, 200.0f);
    public final ComboBox weather = Menu.ComboBox("Weather", "Clear", new String[]{
            "Clear",
            "Rain",
            "Thunder"
    });
    public final Slider time = Menu.Slider("Time", 10000.0f, 0.0f, 24000.0f);
    public final ColorBox fogColor = Menu.Color("Fog Color", Color.WHITE);
    public final Switch fullBright = Menu.Switch("FullBright", false);
    public final Slider chunkLoadDelay = Menu.Slider("Chunk Load Delay", 0.0f, 0.0f, 300.0f);
    public final Switch removeParticles = Menu.Switch("Remove Particles", false);
    public final Switch removeSPacketEffects = Menu.Switch("Remove SPacketEffects", false);
    public final Switch removeSwing = Menu.Switch("Remove Swing", false);
    public final Switch removeSwitchAnimation = Menu.Switch("Remove Switch Animation", false);

    @RegisterListener
    public void onFrame3D(final FrameEvent.FrameEvent3D event) {
        mc.world.setTotalWorldTime((long) time.GetSlider());
        switch (weather.GetCombo()) {
            case "Clear":
                mc.world.setRainStrength(0);
                break;
            case "Rain":
                mc.world.setRainStrength(1);
                break;
            case "Thunder":
                mc.world.setRainStrength(2);
                break;
        }
        if (removeSwitchAnimation.GetSwitch()) {
            if (mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
                mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
                mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItemMainhand();
            }
            if (mc.entityRenderer.itemRenderer.prevEquippedProgressOffHand >= 0.9) {
                mc.entityRenderer.itemRenderer.equippedProgressOffHand = 1.0f;
                mc.entityRenderer.itemRenderer.itemStackOffHand = mc.player.getHeldItemOffhand();
            }
        }
        if (removeSwing.GetSwitch()) {
            mc.player.isSwingInProgress = false;
            mc.player.swingProgressInt = 0;
            mc.player.swingProgress = 0.0f;
            mc.player.prevSwingProgress = 0.0f;
        }
        if (fullBright.GetSwitch() && mc.gameSettings.gammaSetting != 1000) {
            mc.gameSettings.gammaSetting = 1000;
        }
        mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, fov.GetSlider());
    }

    @RegisterListener
    public void onFogColor(final FogEvent event) {
        event.getFogColors().setRed(fogColor.GetColor().getRed() / 255.0f);
        event.getFogColors().setGreen(fogColor.GetColor().getGreen() / 255.0f);
        event.getFogColors().setBlue(fogColor.GetColor().getBlue() / 255.0f);
    }

    @RegisterListener
    public void onPacketReceive(final PacketEvent.PacketReceiveEvent event) {
        if (removeSPacketEffects.GetSwitch() && event.getPacket() instanceof SPacketEffect) {
            event.setCancelled(true);
        }
    }

    @RegisterListener
    public void onParticle(final ParticleEvent event) {
        if (removeParticles.GetSwitch()) {
            event.setCancelled(true);
        }
    }
}
