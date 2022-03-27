package dev.zprestige.fire.module.visual;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class DamageMarkers extends Module {
    public final Slider fadeSpeed = Menu.Slider("Fade Speed", 50.0f, 1.0f, 100.0f);
    public final Slider scale = Menu.Slider("Scale", 1.5f, 0.1f, 5.0f);
    public final ColorBox color = Menu.Color("Color", Color.WHITE);
    protected final ArrayList<DamageMarker> damageMarkers = new ArrayList<>();
    protected HashMap<String, Float> playerMap = new HashMap<>();

    @RegisterListener
    public void onFrame3D(final FrameEvent.FrameEvent3D event) {
        for (final DamageMarker marker : new ArrayList<>(damageMarkers)) {
            if (marker.getAlpha() < 30.0) {
                damageMarkers.remove(marker);
                continue;
            }
            marker.setAlpha(marker.getAlpha() - (marker.getAlpha() / (101.0f - fadeSpeed.GetSlider())));
            marker.setY(marker.getY() + 0.005);
            marker.render();
        }
    }

    @RegisterListener
    public void onTick(final TickEvent event) {
        playerMap.forEach((key, value) -> mc.world.playerEntities.stream().filter(entityPlayer -> !entityPlayer.equals(mc.player) && entityPlayer.getName().equals(key)).forEach(entityPlayer -> {
            final double val = Math.ceil(value - (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()));
            if (val > 0) {
                final Vec3d i = RenderUtil.interpolateEntity(entityPlayer);
                damageMarkers.add(new DamageMarker(val, i.x + getRandom(), i.y + getRandom(), i.z + getRandom(), 255.0));
            }
        }));
        playerMap = mc.world.playerEntities.stream().filter(entityPlayer -> !entityPlayer.equals(mc.player)).collect(Collectors.toMap(EntityPlayer::getName, entityPlayer -> entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount(), (a, b) -> b, HashMap::new));
    }

    protected final Random random = new Random();

    protected float getRandom() {
        return MathHelper.clamp(-0.5f + random.nextFloat() * 0.5f, -0.5f, 0.5f);
    }

    protected class DamageMarker {
        protected double damage, x, y, z, alpha;

        public DamageMarker(double damage, double x, double y, double z, double alpha) {
            this.damage = damage;
            this.x = x;
            this.y = y;
            this.z = z;
            this.alpha = alpha;
        }

        public void render() {
            RenderUtil.draw3DText(damage + "", x, y, z, scale.GetSlider(), color.GetColor().getRed(), color.GetColor().getGreen(), color.GetColor().getBlue(), (float) alpha);
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
