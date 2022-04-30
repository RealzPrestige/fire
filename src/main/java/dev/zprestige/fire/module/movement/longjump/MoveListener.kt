package dev.zprestige.fire.module.movement.longjump

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.MoveEvent
import dev.zprestige.fire.module.movement.noslow.NoSlow
import dev.zprestige.fire.util.impl.EntityUtil
import net.minecraft.init.MobEffects
import java.util.*

class MoveListener(longJump: LongJump) : EventListener<MoveEvent, LongJump>(MoveEvent::class.java, longJump) {

    override fun invoke(e: Any) {
        val event = e as MoveEvent
        if (module.liquids.GetSwitch() || !(mc.player.isInLava || mc.player.isInWater)) {
            val noSlow = Main.moduleManager.getModuleByClass(NoSlow::class.java) as NoSlow
            val factor = if (noSlow.isEnabled && noSlow.slowed()) 5.0f else 1.0f
            when (module.currentState) {
                0 -> {
                    module.currentState++
                    module.previousDistance = 0.0
                }
                1 -> {
                    if ((mc.world.getCollisionBoxes(mc.player,
                            mc.player.entityBoundingBox.offset(0.0, mc.player.motionY, 0.0)
                        ).size > 0 || mc.player.collidedVertically) && module.currentState > 0
                    ) {
                        module.currentState =
                            if (mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f) 0 else 1
                    }
                    module.motionSpeed = module.previousDistance - module.previousDistance / 159.0
                }
                2 -> {
                    var height = (module.verticalFactor.GetSlider() / 10.0f).toDouble()
                    if ((mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) && mc.player.onGround) {
                        if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                            height += ((Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST))!!.amplifier + 1).toFloat() * 0.1f).toDouble()
                        }
                        event.motionY = height.also { mc.player.motionY = it }
                        module.motionSpeed *= module.accelerationFactor.GetSlider().toDouble()
                    }
                }
                3 -> module.motionSpeed =
                    module.previousDistance - 0.76 * (module.previousDistance - EntityUtil.getBaseMotionSpeed() * module.factor.GetSlider())
                else -> {
                    if ((mc.world.getCollisionBoxes(mc.player,
                            mc.player.entityBoundingBox.offset(0.0, mc.player.motionY, 0.0)
                        ).size > 0 || mc.player.collidedVertically) && module.currentState > 0
                    ) {
                        module.currentState =
                            if (mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f) 0 else 1
                    }
                    module.motionSpeed = module.previousDistance - module.previousDistance / 159.0
                }
            }
            module.motionSpeed =
                Math.max(module.motionSpeed, EntityUtil.getBaseMotionSpeed() * module.factor.GetSlider())
            var moveForward = (mc.player.movementInput.moveForward / factor).toDouble()
            var moveStrafe = (mc.player.movementInput.moveStrafe / factor).toDouble()
            val yaw = mc.player.rotationYaw.toDouble()
            if (moveForward != 0.0 && moveStrafe != 0.0) {
                moveForward *= Math.sin(0.7853981633974483)
                moveStrafe *= Math.cos(0.7853981633974483)
            }
            val sin = Math.sin(Math.toRadians(yaw))
            val cos = Math.cos(Math.toRadians(yaw))
            event.motionX = moveForward * module.motionSpeed * -sin + moveStrafe * module.motionSpeed * cos * 0.99
            event.motionZ = moveForward * module.motionSpeed * cos - moveStrafe * module.motionSpeed * -sin * 0.99
            module.currentState++
        }
    }
}