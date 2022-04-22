package dev.zprestige.fire.mixins.impl;

import com.mojang.authlib.GameProfile;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.impl.MotionUpdateEvent;
import dev.zprestige.fire.events.impl.MoveEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerSP.class, priority = 999)
public class MixinEntityPlayerSP extends AbstractClientPlayer {
    protected MotionUpdateEvent motionUpdateEvent;

    public MixinEntityPlayerSP(final World worldIn, final GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    /**
     * Thanks mmmax for helping me back in client 2.0.0 with this :) (still use it)
     **/
    @SuppressWarnings("NullableProblems")
    public void move(final MoverType type, final double x, final double y, final double z) {
        final MoveEvent event = new MoveEvent(type, x, y, z);
        Main.eventBus.post(event);
        super.move(type, event.getMotionX(), event.getMotionY(), event.getMotionZ());
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(final MoverType type, final double x, final double y, final double z, final CallbackInfo callbackInfo) {
        final MoveEvent event = new MoveEvent(type, x, y, z);
        Main.eventBus.post(event);
        if (event.getMotionX() != x || event.getMotionY() != y || event.getMotionZ() != z) {
            super.move(type, event.getMotionX(), event.getMotionY(), event.getMotionZ());
            callbackInfo.cancel();
        }
    }

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V", shift = At.Shift.BEFORE))
    public void onUpdate(final CallbackInfo callbackInfo) {
        motionUpdateEvent = new MotionUpdateEvent(MotionUpdateEvent.Stage.Pre, this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
        Main.eventBus.post(motionUpdateEvent);
        posX = motionUpdateEvent.getX();
        posY = motionUpdateEvent.getY();
        posZ = motionUpdateEvent.getZ();
        rotationYaw = motionUpdateEvent.getRotationYaw();
        rotationPitch = motionUpdateEvent.getRotationPitch();
        onGround = motionUpdateEvent.isOnGround();

    }

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V", shift = At.Shift.AFTER))
    public void onUpdateWalkingPlayerPost(CallbackInfo callbackInfo) {
        if (posX == motionUpdateEvent.getX()) {
            posX = motionUpdateEvent.getPrevX();
        }
        if (posY == motionUpdateEvent.getY()) {
            posY = motionUpdateEvent.getPrevY();
        }
        if (posZ == motionUpdateEvent.getZ()) {
            posZ = motionUpdateEvent.getPrevZ();
        }
        if (rotationYaw == motionUpdateEvent.getRotationYaw()) {
            rotationYaw = motionUpdateEvent.getPrevYaw();
        }
        if (rotationPitch == motionUpdateEvent.getRotationPitch()) {
            rotationPitch = motionUpdateEvent.getPrevPitch();
        }
        if (onGround == motionUpdateEvent.isOnGround()) {
            onGround = motionUpdateEvent.isPrevOnGround();
        }
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "net/minecraft/client/entity/EntityPlayerSP.posX:D"))
    public double posXHook(EntityPlayerSP entityPlayerSP) {
        return motionUpdateEvent.getX();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "net/minecraft/util/math/AxisAlignedBB.minY:D"))
    public double minYHook(AxisAlignedBB axisAlignedBB) {
        return motionUpdateEvent.getY();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "net/minecraft/client/entity/EntityPlayerSP.posZ:D"))
    public double posZHook(EntityPlayerSP entityPlayerSP) {
        return motionUpdateEvent.getZ();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "net/minecraft/client/entity/EntityPlayerSP.rotationYaw:F"))
    public float rotationYawHook(EntityPlayerSP entityPlayerSP) {
        return motionUpdateEvent.getYaw();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "net/minecraft/client/entity/EntityPlayerSP.rotationPitch:F"))
    public float rotationPitchHook(EntityPlayerSP entityPlayerSP) {
        return motionUpdateEvent.getPitch();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "net/minecraft/client/entity/EntityPlayerSP.onGround:Z"))
    public boolean onGroundHook(EntityPlayerSP entityPlayerSP) {
        return motionUpdateEvent.isOnGround();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At(value = "RETURN"))
    public void onUpdateWalkingPlayerReturn(final CallbackInfo callbackInfo) {
        MotionUpdateEvent event = new MotionUpdateEvent(MotionUpdateEvent.Stage.Post, motionUpdateEvent);
        event.setCancelled(motionUpdateEvent.isCancelled());
        Main.eventBus.post(event);
    }


}