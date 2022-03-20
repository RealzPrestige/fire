package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationManager extends RegisteredClass {
    protected final Minecraft mc = Main.mc;

    public void setRotations(final float yaw, final float pitch) {
        setYaw(yaw);
        setPitch(pitch);
    }

    public void setYaw(final float yaw) {
        mc.player.rotationYaw = yaw;
        mc.player.rotationYawHead = yaw;
    }

    public void setPitch(final float pitch) {
        mc.player.rotationPitch = pitch;
    }

    public void facePos(final BlockPos pos) {
        final float[] angle = calculateAngle(getEyePosition(), new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f));
        setRotations(angle[0], angle[1]);
    }

    public void faceEntity(final Entity entity) {
        final float partialTicks = mc.getRenderPartialTicks();
        final float[] angle = calculateAngle(getEyePosition(), entity.getPositionEyes(partialTicks));
        setRotations(angle[0], angle[1]);
    }

    public float[] calculateAngle(final Vec3d start, final Vec3d end) {
        final double x = start.x - end.x;
        final double y = (start.y - end.y) * -1;
        final double z = start.z - end.z;
        final double distance = MathHelper.sqrt(x * x + z * z);
        return new float[]{(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(y, distance)))};
    }

    public Vec3d getEyePosition(){
        return mc.player.getPositionEyes(mc.getRenderPartialTicks());
    }

}
