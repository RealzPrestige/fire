package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.MotionUpdateEvent;
import dev.zprestige.fire.events.impl.PacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;


public class RotationManager extends RegisteredClass {
    protected final Minecraft mc = Main.mc;
    protected final ArrayList<Long> rotationsPerTick = new ArrayList<>();
    protected int max;

    public RotationManager(){
        super(true, false);
    }
    @RegisterListener
    public void onTick(final FrameEvent.FrameEvent3D event) {
        final long time = System.currentTimeMillis();
        rotationsPerTick.removeIf(l -> l < time);
    }

    @RegisterListener
    public void onPacketSend(PacketEvent.PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayer.Rotation) {
            rotationsPerTick.add(System.currentTimeMillis() + 50L);
        }
    }

    public void setClientRotations(final float yaw, final float pitch) {
        mc.player.rotationYaw = yaw;
        mc.player.rotationYawHead = yaw;
        mc.player.rotationPitch = pitch;
    }

    public void facePos(final BlockPos pos, final MotionUpdateEvent event){
        final float[] angle = calculateAngle(new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f));
        event.setYaw(angle[0]);
        event.setPitch(angle[1]);
    }

    public void faceEntity(final Entity entity, final MotionUpdateEvent event){
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
