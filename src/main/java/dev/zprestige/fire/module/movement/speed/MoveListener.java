package dev.zprestige.fire.module.movement.speed;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.MoveEvent;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;

import java.util.Objects;

public class MoveListener extends EventListener<MoveEvent, Speed> {

    public MoveListener(final Speed speed){
        super(MoveEvent.class, speed);
    }

    @Override
    public void invoke(final Object object){
        final MoveEvent event = (MoveEvent) object;
        if (!module.nullCheck() || (!module.liquids.GetSwitch() && (mc.player.isInWater() || mc.player.isInLava() || mc.player.isSpectator())) || mc.player.isElytraFlying()) {
            return;
        }
        if (!mc.player.isSprinting() && EntityUtil.isMoving()) {
            mc.player.setSprinting(true);
        }
        switch (module.speedMode.GetCombo()) {
            case "Strafe":
                switch (module.currentState) {
                    case 0:
                        ++module.currentState;
                        module.previousDistance = 0.0;
                        break;
                    case 1:
                    default:
                        if ((mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size() > 0 || mc.player.collidedVertically) && module.currentState > 0)
                            module.currentState = mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F ? 0 : 1;
                        module.motionSpeed = module.previousDistance - module.previousDistance / 159.0;
                        break;
                    case 2:
                        double var2 = module.strict.GetSwitch() ? 0.42 : 0.40123128;
                        if ((mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) && mc.player.onGround) {
                            if (mc.player.isPotionActive(MobEffects.JUMP_BOOST))
                                var2 += (float) (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier() + 1) * 0.1f;
                            event.setMotionY(mc.player.motionY = var2);
                            module.motionSpeed *= 2.149;
                        }
                        break;
                    case 3:
                        module.motionSpeed = module.previousDistance - 0.76 * (module.previousDistance - EntityUtil.getBaseMotionSpeed() * module.strafeFactor.GetSlider());
                }
                module.motionSpeed = Math.max(module.motionSpeed, EntityUtil.getBaseMotionSpeed() * module.strafeFactor.GetSlider());
                double var4 = mc.player.movementInput.moveForward;
                double var6 = mc.player.movementInput.moveStrafe;
                double var8 = mc.player.rotationYaw;
                if (var4 != 0.0 && var6 != 0.0) {
                    var4 *= Math.sin(0.7853981633974483);
                    var6 *= Math.cos(0.7853981633974483);
                }
                event.setMotionX((var4 * module.motionSpeed * -Math.sin(Math.toRadians(var8)) + var6 * module.motionSpeed * Math.cos(Math.toRadians(var8))) * 0.99);
                event.setMotionZ((var4 * module.motionSpeed * Math.cos(Math.toRadians(var8)) - var6 * module.motionSpeed * -Math.sin(Math.toRadians(var8))) * 0.99);
                ++module.currentState;
                break;
            case "OnGround":
                if (!(mc.player.isSneaking() || mc.player.movementInput.moveForward == 0.0f && mc.player.movementInput.moveStrafe == 0.0f) || !mc.player.onGround) {
                    MovementInput movementInput = mc.player.movementInput;
                    float moveForward = movementInput.moveForward;
                    float moveStrafe = movementInput.moveStrafe;
                    float rotationYaw = mc.player.rotationYaw;
                    if ((double) moveForward == 0.0 && (double) moveStrafe == 0.0) {
                        event.setMotionX(0.0);
                        event.setMotionZ(0.0);
                    } else {
                        if ((double) moveForward != 0.0) {
                            if ((double) moveStrafe > 0.0) {
                                rotationYaw += (float) ((double) moveForward > 0.0 ? -45 : 45);
                            } else if ((double) moveStrafe < 0.0) {
                                rotationYaw += (float) ((double) moveForward > 0.0 ? 45 : -45);
                            }
                            moveStrafe = 0.0f;
                        }
                        moveStrafe = moveStrafe == 0.0f ? moveStrafe : ((double) moveStrafe > 0.0 ? 1.0f : -1.0f);
                        final double cos = Math.cos(Math.toRadians(rotationYaw + 90.0f));
                        final double sin = Math.sin(Math.toRadians(rotationYaw + 90.0f));
                        event.setMotionX((double) moveForward * EntityUtil.getMaxSpeed() * cos + (double) moveStrafe * EntityUtil.getMaxSpeed() * sin);
                        event.setMotionZ((double) moveForward * EntityUtil.getMaxSpeed() * sin - (double) moveStrafe * EntityUtil.getMaxSpeed() * cos);
                    }
                }
                break;
        }
    }
}
