package dev.zprestige.fire.module.movement.longjump

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch

@Descriptor(description = "Allows you to jump further than usual")
class LongJump : Module() {
    val factor: Slider = Menu.Slider("Factor", 5.0f, 0.1f, 20.0f)
    val accelerationFactor: Slider = Menu.Slider("Acceleration Factor", 2.0f, 0.1f, 10.0f)
    val verticalFactor: Slider = Menu.Slider("Vertical Factor", 4.0f, 0.1f, 6.0f)
    val useTimer: Switch = Menu.Switch("Use Timer", false)
    val timerAmount: Slider = Menu.Slider("Timer Amount", 1.0f, 0.9f, 2.0f).visibility { z: Float? -> useTimer.GetSwitch() }
    val liquids: Switch = Menu.Switch("Liquids", false)
    val disableOnLag: Switch = Menu.Switch("Disable On Lag", false)
    var previousDistance = 0.0
    var motionSpeed = 0.0
    var currentState = 1

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            MoveListener(this),
            PacketReceiveListener(this),
            TickListener(this)
        )
    }

    override fun onDisable() {
        Main.tickManager.syncTimer()
    }
}
