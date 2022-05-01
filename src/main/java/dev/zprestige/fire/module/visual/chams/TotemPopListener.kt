package dev.zprestige.fire.module.visual.chams

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TotemPopEvent

class TotemPopListener(chams: Chams) : EventListener<TotemPopEvent, Chams>(TotemPopEvent::class.java, chams) {

    override fun invoke(e: Any) {
        val event = e as TotemPopEvent
        val entityPlayer = event.entityPlayer
        if (module!!.popChams.GetSwitch() && (entityPlayer != mc.player || module!!.popSelf.GetSwitch())) {
            module!!.addEntity(event.entityPlayer)
        }
    }
}