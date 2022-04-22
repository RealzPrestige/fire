package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.module.client.Panel;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Random;

public class ParticleManager extends RegisteredClass {
    protected final Panel PANEL = (Panel) Main.moduleManager.getModuleByClass(Panel.class);
    protected final Minecraft mc = Main.mc;
    protected final ArrayList<Particle> particles = new ArrayList<>();
    protected final Random random = new Random();

    public ParticleManager() {
        super(true, false);
    }

    @RegisterListener
    public void onFrame2D(final FrameEvent.FrameEvent2D event) {
        if (PANEL.particles.GetSwitch() && mc.currentScreen instanceof PanelScreen) {
            final ScaledResolution scaledResolution = new ScaledResolution(mc);
            final float size = PANEL.particleSize.GetSlider();
            if (particles.size() < PANEL.particleAmount.GetSlider()) {
                Particle particle1 = new Particle(scaledResolution, getRandomSide());
                particles.add(particle1);
            } else if (particles.size() > PANEL.particleAmount.GetSlider()) {
                for (final Particle particle : particles) {
                    particles.remove(particle);
                    break;
                }
            }
            new ArrayList<>(particles).forEach(particle -> {
                if (particle.canRemove()) {
                    particles.remove(particle);
                    return;
                }
                particle.render(size);
            });
        } else if (particles.size() > 0) {
            particles.clear();
        }
    }

    protected Side getRandomSide() {
        final float i = randomInt();
        if (i == 0) {
            return Side.TOP;
        } else if (i == 1) {
            return Side.RIGHT;
        } else if (i == 2) {
            return Side.BOTTOM;
        } else {
            return Side.LEFT;
        }
    }

    protected int randomInt() {
        return random.nextInt(3 + 1);
    }

    protected float random(final float min, final float max) {
        return MathHelper.clamp(min + random.nextFloat() * max, min, max);
    }

    protected class Particle {
        protected final ScaledResolution scaledResolution;
        protected float deltaX, deltaY;
        protected float x, y;

        public Particle(final ScaledResolution scaledResolution, final Side side) {
            this.scaledResolution = scaledResolution;
            switch (side) {
                case TOP:
                    x = random(0, scaledResolution.getScaledWidth());
                    y = 0;
                    deltaX = random(-0.5f, 0.5f);
                    deltaY = random(0, 1);
                    break;
                case RIGHT:
                    x = scaledResolution.getScaledWidth();
                    y = random(0, scaledResolution.getScaledHeight());
                    deltaX = random(0, 1);
                    deltaY = random(0.5f, -0.5f);
                    break;
                case LEFT:
                    x = 0;
                    y = random(0, scaledResolution.getScaledHeight());
                    deltaX = random(-1, 0);
                    deltaY = random(0.5f, -0.5f);
                    break;
                case BOTTOM:
                    x = random(0, scaledResolution.getScaledWidth());
                    y = scaledResolution.getScaledHeight();
                    deltaX = random(0.5f, -0.5f);
                    deltaY = random(-1, 0);
                    break;
            }
        }

        public boolean canRemove() {
            return x > scaledResolution.getScaledWidth() + 1 || x < -1 || y > scaledResolution.getScaledHeight() + 1 || y < -1;
        }

        public void render(final float size) {
            x += (deltaX * (1 + PANEL.particleSpeed.GetSlider()));
            y += (deltaY * (1 + PANEL.particleSpeed.GetSlider()));
            RenderUtil.drawRoundedRect(x - size, y - size, x + size, y + size, size * 2, PANEL.particleColor.GetColor());
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    protected enum Side {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }
}
