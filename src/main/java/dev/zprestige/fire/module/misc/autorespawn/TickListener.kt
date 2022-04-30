package dev.zprestige.fire.module.misc.autorespawn

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import net.minecraft.client.gui.GuiGameOver

class TickListener(autoRespawn: AutoRespawn) : EventListener<TickEvent, AutoRespawn>(TickEvent::class.java, autoRespawn) {

    override fun invoke(e: Any) {
        if (mc.currentScreen is GuiGameOver) {
            mc.player.respawnPlayer()
            mc.displayGuiScreen(null)
        }
    }
}
