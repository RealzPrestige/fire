package dev.zprestige.fire.module.player.noentitytrace

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.Switch

@Descriptor(description = "Disallows interaction with entities when holding selected items")
class NoEntityTrace : Module() {
    val pickaxe: Switch = Menu.Switch("Pickaxe", false)
    val gapple: Switch = Menu.Switch("Gapple", false)

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            MouseOverListener(this)
        )
    }
}