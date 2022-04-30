package dev.zprestige.fire.module.misc.freelook

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TurnEvent
import net.minecraft.util.math.MathHelper

class TurnListener(freelook: Freelook) : EventListener<TurnEvent, Freelook>(
    TurnEvent::class.java, freelook
) {
    override fun invoke(e: Any) {
        val event = e as TurnEvent
        module.yaw = module.yaw + event.yaw * 0.15f
        module.pitch = module.pitch - event.pitch * 0.15f
        module.pitch = MathHelper.clamp(module.pitch, -90.0f, 90.0f)
        event.setCancelled()
    }
}