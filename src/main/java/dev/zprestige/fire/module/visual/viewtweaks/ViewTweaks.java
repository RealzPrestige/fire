package dev.zprestige.fire.module.visual.viewtweaks;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;

import java.awt.*;

@Descriptor(description = "Tweaks your vision and how the world looks")
public class ViewTweaks extends Module {
    public final Slider fov = Menu.Slider("Fov", 130.0f, 50.0f, 200.0f);
    public final ComboBox weather = Menu.ComboBox("Weather", "Clear", new String[]{
            "Clear",
            "Rain",
            "Thunder"
    });
    public final Slider time = Menu.Slider("Time", 10000.0f, 0.0f, 24000.0f);
    public final ColorBox fogColor = Menu.Color("Fog Color", Color.WHITE);
    public final Switch keepFog = Menu.Switch("Keep Fog", false);
    public final Slider fogDensity = Menu.Slider("Fog Density", 0.0f, 0.0f, 100.0f).visibility(z -> keepFog.GetSwitch());
    public final Switch fullBright = Menu.Switch("FullBright", false);
    public final Slider chunkLoadDelay = Menu.Slider("Chunk Load Delay", 0.0f, 0.0f, 300.0f);
    public final Switch removeParticles = Menu.Switch("Remove Particles", false);
    public final Switch removeSPacketEffects = Menu.Switch("Remove SPacketEffects", false);
    public final Switch removeSwing = Menu.Switch("Remove Swing", false);
    public final Switch removeSwitchAnimation = Menu.Switch("Remove Switch Animation", false);

    public ViewTweaks() {
        eventListeners = new EventListener[]{
                new FogDensityListener(this),
                new FogListener(this),
                new Frame3DListener(this),
                new PacketReceiveListener(this),
                new ParticleListener(this)
        };
    }
}
