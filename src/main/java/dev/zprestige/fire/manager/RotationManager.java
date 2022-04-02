package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
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
    protected float[] target;
    protected boolean needsRotations = false;

    @RegisterListener
    public void onFrame3D(final FrameEvent.FrameEvent3D event){
        final long time = System.currentTimeMillis();
        rotationsPerTick.removeIf(l -> l < time);
    }
    @RegisterListener
    public void onPacketSend(PacketEvent.PacketSendEvent event) {
        if (Main.listener.checkNull() && needsRotations && event.getPacket() instanceof CPacketPlayer && mc.player.onGround) {
            final CPacketPlayer packet = ((CPacketPlayer) event.getPacket());
            packet.yaw = target[0];
            packet.pitch = target[1];
            needsRotations = false;
        }
        if (event.getPacket() instanceof CPacketPlayer.Rotation){
            rotationsPerTick.add(System.currentTimeMillis() + 50L);
        }
    }

    public void setRotations(final float yaw, final float pitch, final boolean sync) {
        if (sync) {
            target = new float[]{yaw, pitch};
            needsRotations = true;
        } else {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, mc.player.onGround));
        }
    }

    public void setClientRotations(final float yaw, final float pitch){
        mc.player.rotationYaw = yaw;
        mc.player.rotationYawHead = yaw;
        mc.player.rotationPitch = pitch;
    }

    public void facePos(final BlockPos pos, final boolean sync) {
        final float[] angle = calculateAngle(new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f));
        setRotations(angle[0], angle[1], sync);
    }

    public void faceEntity(final Entity entity, final boolean sync) {
        final float partialTicks = mc.getRenderPartialTicks();
        final float[] angle = calculateAngle(entity.getPositionEyes(partialTicks));
        setRotations(angle[0], angle[1], sync);
    }

    public float[] calculateAngle(final Vec3d to) {
        float yaw = (float) (Math.toDegrees(Math.atan2(to.subtract(mc.player.getPositionEyes(1)).z, to.subtract(mc.player.getPositionEyes(1)).x)) - 90);
        float pitch = (float) Math.toDegrees(-Math.atan2(to.subtract(mc.player.getPositionEyes(1)).y, Math.hypot(to.subtract(mc.player.getPositionEyes(1)).x, to.subtract(mc.player.getPositionEyes(1)).z)));
        return new float[]{MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean maxRotations(){
        return rotationsPerTick.size() > max;
    }
}
