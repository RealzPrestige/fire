package dev.zprestige.fire.module.movement.elytrafly

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.Slider

@Descriptor(description = "Makes flying with an elytra much easier and pleasant")
class ElytraFly : Module() {
    val speed: Slider = Menu.Slider("Speed", 1.0f, 0.1f, 5.0f)
    val glideSpeed: Slider = Menu.Slider("Glide Speed", 0.1f, 0.0f, 10.0f)

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            MoveListener(this)
        )
    }
}