package dev.zprestige.fire.manager.rotationmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.MotionUpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;


public class RotationManager {
    protected final Minecraft mc = Main.mc;
    protected final ArrayList<Long> rotationsPerTick = new ArrayList<>();
    protected int max;

    public RotationManager() {
        Main.newBus.registerListeners(new EventListener[]{
                new Frame3DListener(),
                new PacketSendListener()
        });
    }

    public void setClientRotations(final float yaw, final float pitch) {
        mc.player.rotationYaw = yaw;
        mc.player.rotationYawHead = yaw;
        mc.player.rotationPitch = pitch;
    }

    public void facePos(final BlockPos pos, final MotionUpdateEvent event) {
        final float[] angle = calculateAngle(new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f));
        event.setYaw(angle[0]);
        event.setPitch(angle[1]);
    }

    public void faceEntity(final Entity entity, final MotionUpdateEvent event) {
        final float partialTicks = mc.getRenderPartialTicks();
        final float[] angle = calculateAngle(entity.getPositionEyes(partialTicks));
        event.setYaw(angle[0]);
        event.setPitch(angle[1]);
    }

    public float[] calculateAngle(final Vec3d to) {
        float yaw = (float) (Math.toDegrees(Math.atan2(to.subtract(mc.player.getPositionEyes(1)).z, to.subtract(mc.player.getPositionEyes(1)).x)) - 90);
        float pitch = (float) Math.toDegrees(-Math.atan2(to.subtract(mc.player.getPositionEyes(1)).y, Math.hypot(to.subtract(mc.player.getPositionEyes(1)).x, to.subtract(mc.player.getPositionEyes(1)).z)));
        return new float[]{MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean maxRotations() {
        return rotationsPerTick.size() > max;
    }
}
