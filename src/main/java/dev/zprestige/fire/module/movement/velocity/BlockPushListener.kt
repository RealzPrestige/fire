package dev.zprestige.fire.module.movement.velocity

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.BlockPushEvent

class BlockPushListener(velocity: Velocity) :
    EventListener<BlockPushEvent, Velocity>(BlockPushEvent::class.java, velocity) {

    override fun invoke(e: Any) {
        val event: BlockPushEvent = e as BlockPushEvent
        event.setCancelled()
    }
}