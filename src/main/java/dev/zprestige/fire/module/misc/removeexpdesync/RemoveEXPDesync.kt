package dev.zprestige.fire.module.misc.removeexpdesync

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.Slider
import net.minecraft.entity.Entity
import net.minecraft.init.Items
import net.minecraft.util.EnumHand

@Descriptor(description = "Attempts to remove exp desync")
class RemoveEXPDesync : Module() {
    val timer: Slider = Menu.Slider("Timer", 1.0f, 0.1f, 10.0f)
    val force: Slider = Menu.Slider("Force", 111.0f, 0.1f, 120.0f)
    val attempts: Slider = Menu.Slider("Attempts", 1.0f, 1.0f, 20.0f)
    var activeEntities: List<Entity> = ArrayList()
    var started = false
    var index = 0

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            PacketSendListener(this),
            TickListener(this)
        )
    }

    fun isEXP(enumHand: EnumHand?): Boolean {
        return mc.player.getHeldItem(enumHand!!).getItem() == Items.EXPERIENCE_BOTTLE
    }
}
