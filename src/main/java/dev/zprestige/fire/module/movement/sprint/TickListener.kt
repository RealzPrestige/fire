package dev.zprestige.fire.module.movement.sprint

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.PacketEvent
import dev.zprestige.fire.util.impl.EntityUtil

class TickListener(sprint: Sprint) :
    EventListener<PacketEvent.PacketReceiveEvent, Sprint>(PacketEvent.PacketReceiveEvent::class.java, sprint) {

    override fun invoke(e: Any) {
        if (module.nullCheck() && EntityUtil.isMoving()){
            mc.player.isSprinting = true
        }
    }
}