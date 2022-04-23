package dev.zprestige.fire.module.visual.damagemarkers;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Descriptor(description = "Renders when enemies take damage")
public class DamageMarkers extends Module {
    public final Slider fadeSpeed = Menu.Slider("Fade Speed", 50.0f, 1.0f, 100.0f);
    public final Slider scale = Menu.Slider("Scale", 1.5f, 0.1f, 5.0f);
    public final ColorBox color = Menu.Color("Color", Color.WHITE);
    protected final ArrayList<DamageMarker> damageMarkers = new ArrayList<>();
    protected HashMap<String, Float> playerMap = new HashMap<>();
    protected final Random random = new Random();

    public DamageMarkers() {
        eventListeners = new EventListener[]{
                new Frame3DListener(this),
                new TickListener(this)
        };
    }

    protected float getRandom() {
        return MathHelper.clamp(-0.5f + random.nextFloat() * 0.5f, -0.5f, 0.5f);
    }

    protected static class DamageMarker {
        protected final Entity entity;
        protected final double randX, randY, randZ;
        protected double damage, alpha, y, scale;
        protected final Color color;


        public DamageMarker(final double damage, final Entity entity, final double randX, final double randY, final double randZ, final double alpha, final double scale, final Color color) {
            this.damage = damage;
            this.entity = entity;
            this.randX = randX;
            this.randY = randY;
            this.randZ = randZ;
            this.alpha = alpha;
            this.scale = scale;
            this.color = color;
        }

        public void render() {
            final Vec3d i = RenderUtil.interpolateEntity(entity);
            RenderUtil.draw3DText(damage + "", i.x + randX, i.y + y + randY, i.z + randZ, scale / 1000, color.getRed(), color.getGreen(), color.getBlue(), (float) alpha);
        }

        public void setAlpha(double alpha) {
            this.alpha = alpha;
        }

        public double getAlpha() {
            return alpha;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getY() {
            return y;
        }
    }
}
