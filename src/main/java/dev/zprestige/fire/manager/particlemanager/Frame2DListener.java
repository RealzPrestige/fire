package dev.zprestige.fire.manager.particlemanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.FrameEvent;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

public class Frame2DListener extends EventListener<FrameEvent.FrameEvent2D, Object> {

    public Frame2DListener() {
        super(FrameEvent.FrameEvent2D.class);
    }

    @Override
    public void invoke(final Object object) {
        if (Main.particleManager.PANEL.particles.GetSwitch() && mc.currentScreen instanceof PanelScreen) {
            final ScaledResolution scaledResolution = new ScaledResolution(mc);
            final float size = Main.particleManager.PANEL.particleSize.GetSlider();
            if (Main.particleManager.particles.size() < Main.particleManager.PANEL.particleAmount.GetSlider()) {
                ParticleManager.Particle particle1 = new ParticleManager.Particle(scaledResolution, Main.particleManager.getRandomSide());
                Main.particleManager.particles.add(particle1);
            } else if (Main.particleManager.particles.size() > Main.particleManager.PANEL.particleAmount.GetSlider()) {
                for (final ParticleManager.Particle particle : Main.particleManager.particles) {
                    Main.particleManager.particles.remove(particle);
                    break;
                }
            }
            new ArrayList<>(Main.particleManager.particles).forEach(particle -> {
                if (particle.canRemove()) {
                    Main.particleManager.particles.remove(particle);
                    return;
                }
                particle.render(size);
            });
        } else if (Main.particleManager.particles.size() > 0) {
            Main.particleManager.particles.clear();
        }
    }
}
