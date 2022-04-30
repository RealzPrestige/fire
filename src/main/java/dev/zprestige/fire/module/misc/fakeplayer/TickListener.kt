package dev.zprestige.fire.module.misc.fakeplayer

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent

class TickListener(fakePlayer: FakePlayer) : EventListener<TickEvent, FakePlayer>(TickEvent::class.java, fakePlayer) {
    
    override fun invoke(e: Any) {
        if (module.start.GetSwitch()) {
            if (module.record.GetSwitch()) {
                Main.chatManager.sendMessage("Cant record and start at the same time, ending recording")
                module.start.value = false
                return
            }
            if (module.recording.isEmpty()) {
                Main.chatManager.sendMessage("No recording found, record first before starting.")
                module.start.value = false
                return
            }
            if (module.running == module.recording.size) {
                module.running = 0
                return
            }
            val location = module.recording[module.running]
            module.fakePlayer!!.prevPosX = module.prevPosX.toDouble()
            module.fakePlayer!!.prevPosY = module.prevPosY.toDouble()
            module.fakePlayer!!.prevPosZ = module.prevPosZ.toDouble()
            module.fakePlayer!!.prevRotationYaw = module.prevRotationYaw
            module.fakePlayer!!.prevRotationYawHead = module.prevRotationYawHead
            module.fakePlayer!!.prevRotationPitch = module.prevRotationPitch
            module.fakePlayer!!.posX = location.x.toDouble()
            module.fakePlayer!!.posY = location.y.toDouble()
            module.fakePlayer!!.posZ = location.z.toDouble()
            module.fakePlayer!!.rotationYaw = location.rotationYaw
            module.fakePlayer!!.rotationYawHead = location.rotationYawHead
            module.fakePlayer!!.rotationPitch = location.rotationPitch
            module.prevPosX = location.x
            module.prevPosY = location.y
            module.prevPosZ = location.z
            module.prevRotationYaw = location.rotationYaw
            module.prevRotationYawHead = location.rotationYawHead
            module.prevRotationPitch = location.rotationPitch
            module.running++
        }
        if (module.record.GetSwitch()) {
            if (!module.recorded) {
                module.index = 0
                module.recorded = true
                module.recording.clear()
            }
            module.recording.add(FakePlayer.Location(mc.player.posX.toFloat(),
                mc.player.posY.toFloat(),
                mc.player.posZ.toFloat(),
                mc.player.rotationYaw,
                mc.player.rotationYawHead,
                mc.player.rotationPitch
            )
            )
        } else if (module.recorded) {
            module.index = 0
            module.recorded = false
        }
        if (module.fakePlayer != null && module.fakePlayer!!.getDistanceSq(mc.player) > 100000) {
            mc.world.removeEntityFromWorld(module.id)
            module.disableModule()
        }
    }
}