package dev.zprestige.fire.manager.particlemanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.module.client.panel.Panel;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Random;

public class ParticleManager  {
    protected final Panel PANEL = (Panel) Main.moduleManager.getModuleByClass(Panel.class);
    protected final Minecraft mc = Main.mc;
    protected final ArrayList<Particle> particles = new ArrayList<>();
    protected final Random random = new Random();

    public ParticleManager() {
        Main.newBus.registerListeners(new EventListener[]{
                new Frame2DListener()
        });
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

    protected static class Particle {
        protected final ScaledResolution scaledResolution;
        protected float deltaX, deltaY;
        protected float x, y;

        public Particle(final ScaledResolution scaledResolution, final Side side) {
            this.scaledResolution = scaledResolution;
            switch (side) {
                case TOP:
                    x = Main.particleManager.random(0, scaledResolution.getScaledWidth());
                    y = 0;
                    deltaX = Main.particleManager.random(-0.5f, 0.5f);
                    deltaY = Main.particleManager.random(0, 1);
                    break;
                case RIGHT:
                    x = scaledResolution.getScaledWidth();
                    y = Main.particleManager.random(0, scaledResolution.getScaledHeight());
                    deltaX = Main.particleManager.random(0, 1);
                    deltaY = Main.particleManager.random(0.5f, -0.5f);
                    break;
                case LEFT:
                    x = 0;
                    y = Main.particleManager.random(0, scaledResolution.getScaledHeight());
                    deltaX = Main.particleManager.random(-1, 0);
                    deltaY = Main.particleManager.random(0.5f, -0.5f);
                    break;
                case BOTTOM:
                    x = Main.particleManager.random(0, scaledResolution.getScaledWidth());
                    y = scaledResolution.getScaledHeight();
                    deltaX = Main.particleManager.random(0.5f, -0.5f);
                    deltaY = Main.particleManager.random(-1, 0);
                    break;
            }
        }

        public boolean canRemove() {
            return x > scaledResolution.getScaledWidth() + 1 || x < -1 || y > scaledResolution.getScaledHeight() + 1 || y < -1;
        }

        public void render(final float size) {
            x += (deltaX * (1 + Main.particleManager.PANEL.particleSpeed.GetSlider()));
            y += (deltaY * (1 + Main.particleManager.PANEL.particleSpeed.GetSlider()));
            RenderUtil.drawRoundedRect(x - size, y - size, x + size, y + size, size * 2, Main.particleManager.PANEL.particleColor.GetColor());
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
