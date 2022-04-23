package dev.zprestige.fire.module.movement.longjump;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.MoveEvent;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.init.MobEffects;

import java.util.Objects;

public class MoveListener extends EventListener<MoveEvent, LongJump> {

    public MoveListener(final LongJump longJump) {
        super(MoveEvent.class, longJump);
    }

    @Override
    public void invoke(final Object object) {
        final MoveEvent event = (MoveEvent) object;
        if (module.liquids.GetSwitch() || !(mc.player.isInLava() || mc.player.isInWater())) {
            switch (module.currentState) {
                case 0:
                    module.currentState++;
                    module.previousDistance = 0.0;
                    break;
                case 1:
                default:
                    if ((mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size() > 0 || mc.player.collidedVertically) && module.currentState > 0) {
                        module.currentState = mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F ? 0 : 1;
                    }
                    module.motionSpeed = module.previousDistance - module.previousDistance / 159.0;
                    break;
                case 2:
                    double height = module.verticalFactor.GetSlider() / 10.0f;
                    if ((mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) && mc.player.onGround) {
                        if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                            height += (float) (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier() + 1) * 0.1f;
                        }
                        event.setMotionY((mc.player.motionY = height));
                        module.motionSpeed *= module.accelerationFactor.GetSlider();
                    }
                    break;
                case 3:
                    module.motionSpeed = module.previousDistance - 0.76 * (module.previousDistance - EntityUtil.getBaseMotionSpeed() * module.factor.GetSlider());
            }
            module.motionSpeed = Math.max(module.motionSpeed, EntityUtil.getBaseMotionSpeed() * module.factor.GetSlider());
            double moveForward = mc.player.movementInput.moveForward;
            double moveStrafe = mc.player.movementInput.moveStrafe;
            double yaw = mc.player.rotationYaw;
            if (moveForward != 0.0 && moveStrafe != 0.0) {
                moveForward *= Math.sin(0.7853981633974483);
                moveStrafe *= Math.cos(0.7853981633974483);
            }
            final double sin = Math.sin(Math.toRadians(yaw));
            final double cos = Math.cos(Math.toRadians(yaw));
            event.setMotionX((moveForward * module.motionSpeed * -sin) + (moveStrafe * module.motionSpeed * cos) * 0.99);
            event.setMotionZ((moveForward * module.motionSpeed * cos) - (moveStrafe * module.motionSpeed * -sin) * 0.99);
            module.currentState++;
        }
    }
}
