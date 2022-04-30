package dev.zprestige.fire.module.movement.velocity

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.EntityPushEvent

class EntityPushListener(velocity: Velocity) :
    EventListener<EntityPushEvent, Velocity>(EntityPushEvent::class.java, velocity) {

    override fun invoke(e: Any) {
        val event: EntityPushEvent = e as EntityPushEvent
        event.setCancelled()
    }
}