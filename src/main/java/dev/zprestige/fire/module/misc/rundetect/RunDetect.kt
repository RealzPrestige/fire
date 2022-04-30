package dev.zprestige.fire.module.misc.rundetect

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.Slider
import net.minecraft.entity.player.EntityPlayer

@Descriptor(description = "Predicts when enemies will run out of holes")
class RunDetect : Module() {
    val radius: Slider = Menu.Slider("Radius", 3.0f, 0.1f, 15.0f)
    val potentialRunnersList = ArrayList<EntityPlayer>()
    val swordedPotentialRunnersList = ArrayList<EntityPlayer>()
    val finalRunningPlayers = ArrayList<EntityPlayer>()

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            Frame3DListener(this),
            TickListener(this)
        )
    }

    fun remove(entityPlayer: EntityPlayer?) {
        potentialRunnersList.remove(entityPlayer)
        swordedPotentialRunnersList.remove(entityPlayer)
        finalRunningPlayers.remove(entityPlayer)
    }
}