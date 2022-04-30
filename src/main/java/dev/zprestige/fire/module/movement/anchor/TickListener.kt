package dev.zprestige.fire.module.movement.anchor

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import dev.zprestige.fire.manager.playermanager.PlayerManager
import dev.zprestige.fire.util.impl.BlockUtil
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import java.util.*

class TickListener(anchor: Anchor) : EventListener<TickEvent, Anchor>(TickEvent::class.java, anchor) {

    override fun invoke(e: Any) {
        if (module.pos != null && mc.player.getDistanceSq(module.pos!!) > module.range.GetSlider() * 2.0f) {
            module.pos = null
        }
        if (module.pos != null && !Main.holeManager.contains(module.pos)) {
            module.pos = null
        }
        if (BlockUtil.isPlayerSafe(PlayerManager.Player(mc.player))) {
            module.pos = null
            module.timer.syncTime()
            return
        }
        if (module.pos == null) {
            val posses = TreeMap<Double, BlockPos>()
            for (holePos in Main.holeManager.holes) {
                if (holePos.isDouble) {
                    continue
                }
                val pos1 = holePos.pos
                val dist = mc.player.getDistanceSq(pos1)
                if (if (module.mode.GetCombo() == "Snap") mc.player.posY > pos1.getY() else mc.player.posY >= pos1.getY() && dist < module.range.GetSlider() * 2.0f) {
                    posses[dist] = pos1
                }
            }
            if (!posses.isEmpty()) {
                module.pos = posses.firstEntry().value
            }
        } else if (module.timer.getTime(1000)) {
            if (module.pos!!.getY() < mc.player.posY) {
                when (module.mode.GetCombo()) {
                    "Stop Motion" -> if (module.isOver(module.pos!!) && module.intersects(
                            module.pos!!
                        )
                    ) {
                        mc.player.motionX = 0.0
                        mc.player.motionZ = 0.0
                        module.setMovementsFalse()
                    }
                    "Pull" -> {
                        val bb = AxisAlignedBB(module.pos!!).shrink(0.35)
                        val speed = module.pullSpeed.GetSlider() / 10.0f
                        if (mc.player.posX > bb.maxX) {
                            mc.player.motionX = -speed.toDouble()
                        } else if (mc.player.posX < bb.minX) {
                            mc.player.motionX = speed.toDouble()
                        }
                        if (mc.player.posZ > bb.maxZ) {
                            mc.player.motionZ = -speed.toDouble()
                        } else if (mc.player.posZ < bb.minZ) {
                            mc.player.motionZ = speed.toDouble()
                        }
                        module.setMovementsFalse()
                    }
                    "Snap" -> {
                        if (!module.onGround.GetSwitch() || mc.player.onGround) {
                            mc.player.setPosition((module.pos!!.getX() + 0.5f).toDouble(),
                                mc.player.posY,
                                (module.pos!!.getZ() + 0.5f).toDouble()
                            )
                        }
                        mc.player.motionY = -module.snapFall.GetSlider().toDouble()
                        module.setMovementsFalse()
                    }
                }
            }
        }
    }
}