package dev.zprestige.fire.module.movement.speed

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.MoveEvent
import dev.zprestige.fire.module.movement.noslow.NoSlow
import dev.zprestige.fire.util.impl.EntityUtil
import net.minecraft.init.MobEffects
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class MoveListener(speed: Speed) : EventListener<MoveEvent, Speed>(
    MoveEvent::class.java, speed
) {
    override fun invoke(e: Any) {
        val event = e as MoveEvent
        if (!module.nullCheck() || !module.liquids.GetSwitch() && (mc.player.isInWater || mc.player.isInLava || mc.player.isSpectator) || mc.player.isElytraFlying) {
            return
        }
        if (!mc.player.isSprinting && EntityUtil.isMoving()) {
            mc.player.isSprinting = true
        }
        val noSlow = Main.moduleManager.getModuleByClass(NoSlow::class.java) as NoSlow
        val factor = if (noSlow.isEnabled && noSlow.slowed()) 5.0f else 1.0f
        when (module.speedMode.GetCombo()) {
            "Strafe" -> {
                when (module.currentState) {
                    0 -> {
                        ++module.currentState
                        module.previousDistance = 0.0
                    }
                    1 -> {
                        if ((mc.world.getCollisionBoxes(mc.player,
                                mc.player.entityBoundingBox.offset(0.0, mc.player.motionY, 0.0)
                            ).size > 0 || mc.player.collidedVertically) && module.currentState > 0
                        ) module.currentState =
                            if (mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f) 0 else 1
                        module.motionSpeed = module.previousDistance - module.previousDistance / 159.0
                    }
                    2 -> {
                        var height = if (module.strict.GetSwitch()) 0.42 else 0.40123128
                        if ((mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) && mc.player.onGround) {
                            if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                                height += ((Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST))!!.amplifier + 1).toFloat() * 0.1f).toDouble()
                            }
                            event.motionY = height.also { mc.player.motionY = it }
                            module.motionSpeed *= 2.149
                        }
                    }
                    3 -> module.motionSpeed =
                        module.previousDistance - 0.76 * (module.previousDistance - EntityUtil.getBaseMotionSpeed() * module.strafeFactor.GetSlider())
                    else -> {
                        if ((mc.world.getCollisionBoxes(mc.player,
                                mc.player.entityBoundingBox.offset(0.0, mc.player.motionY, 0.0)
                            ).size > 0 || mc.player.collidedVertically) && module.currentState > 0
                        ) module.currentState =
                            if (mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f) 0 else 1
                        module.motionSpeed = module.previousDistance - module.previousDistance / 159.0
                    }
                }
                module.motionSpeed =
                    module.motionSpeed.coerceAtLeast(EntityUtil.getBaseMotionSpeed() * module.strafeFactor.GetSlider())
                var moveForward = (mc.player.movementInput.moveForward / factor).toDouble()
                var moveStrafe = (mc.player.movementInput.moveStrafe / factor).toDouble()
                val rotationYaw = mc.player.rotationYaw.toDouble()
                if (moveForward != 0.0 && moveStrafe != 0.0) {
                    moveForward *= sin(0.7853981633974483)
                    moveStrafe *= cos(0.7853981633974483)
                }
                event.motionX =
                    (moveForward * module.motionSpeed * -sin(Math.toRadians(rotationYaw)) + moveStrafe * module.motionSpeed * cos(
                        Math.toRadians(rotationYaw)
                    )) * 0.99
                event.motionZ =
                    (moveForward * module.motionSpeed * cos(Math.toRadians(rotationYaw)) - moveStrafe * module.motionSpeed * -sin(
                        Math.toRadians(rotationYaw)
                    )) * 0.99
                ++module.currentState
            }
            "OnGround" -> if (!(mc.player.isSneaking || mc.player.movementInput.moveForward == 0.0f && mc.player.movementInput.moveStrafe == 0.0f) || !mc.player.onGround) {
                val movementInput = mc.player.movementInput
                val moveForward = movementInput.moveForward / factor
                var moveStrafe = movementInput.moveStrafe / factor
                var rotationYaw = mc.player.rotationYaw
                if (moveForward.toDouble() == 0.0 && moveStrafe.toDouble() == 0.0) {
                    event.motionX = 0.0
                    event.motionZ = 0.0
                } else {
                    if (moveForward.toDouble() != 0.0) {
                        if (moveStrafe.toDouble() > 0.0) {
                            rotationYaw += (if (moveForward.toDouble() > 0.0) -45 else 45).toFloat()
                        } else if (moveStrafe.toDouble() < 0.0) {
                            rotationYaw += (if (moveForward.toDouble() > 0.0) 45 else -45).toFloat()
                        }
                        moveStrafe = 0.0f
                    }
                    moveStrafe =
                        if (moveStrafe == 0.0f) moveStrafe else if (moveStrafe.toDouble() > 0.0) 1.0f else -1.0f
                    val cos = cos(Math.toRadians((rotationYaw + 90.0f).toDouble()))
                    val sin = sin(Math.toRadians((rotationYaw + 90.0f).toDouble()))
                    event.motionX = moveForward.toDouble() * EntityUtil.getMaxSpeed() * cos + moveStrafe.toDouble() * EntityUtil.getMaxSpeed() * sin
                    event.motionZ = moveForward.toDouble() * EntityUtil.getMaxSpeed() * sin - moveStrafe.toDouble() * EntityUtil.getMaxSpeed() * cos
                }
            }
        }
    }
}