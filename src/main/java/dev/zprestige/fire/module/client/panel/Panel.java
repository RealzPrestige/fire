package dev.zprestige.fire.module.client.panel;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;

import java.awt.*;

@Descriptor(description = "Opens Fire's csgo like clickgui")
public class Panel extends Module {
    public final ColorBox color = Menu.Color("Color", new Color(0x501BE5));
    public final Switch particles = Menu.Switch("Particles", false);
    public final ColorBox particleColor = Menu.Color("Particle Color", new Color(0x501BE5)).visibility(z -> particles.GetSwitch());
    public final Slider particleSpeed = Menu.Slider("Particle Speed", 0.5f, 0.0f, 2.0f).visibility(z -> particles.GetSwitch());
    public final Slider particleSize = Menu.Slider("Particle Size", 1.0f, 0.5f, 10.0f).visibility(z -> particles.GetSwitch());
    public final Slider particleAmount = Menu.Slider("Particle Amount", 200.0f, 1.0f, 1000.0f).visibility(z -> particles.GetSwitch());

    public Panel(){
        eventListeners = new EventListener[]{
                new TickListener(this)
        };
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new PanelScreen());
    }

    @Override
    public void onDisable() {
        mc.displayGuiScreen(null);
    }
}
