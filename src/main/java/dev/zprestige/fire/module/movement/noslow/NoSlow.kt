package dev.zprestige.fire.module.movement.noslow

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.ComboBox
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch

@Descriptor(description = "Prevents slow down on items")
class NoSlow : Module() {
    val items: Switch = Menu.Switch("Items", true)
    val ncpStrict: Switch = Menu.Switch("Ncp Strict", false).visibility { items.GetSwitch() }
    val webs: Switch = Menu.Switch("Webs", false)
    val mode: ComboBox = Menu.ComboBox(
        "Mode", "Vanilla", arrayOf(
            "Vanilla",
            "Ncp",
            "Fast",
            "Factor",
            "Timer"
        )
    ).visibility { webs.GetSwitch() }
    val fastFactor: Slider =
        Menu.Slider("Fast Factor", 1.1f, 0.1f, 5.0f).visibility { webs.GetSwitch() && mode.GetCombo() == "Fast" }
    val factor: Slider =
        Menu.Slider("Factor", 1.0f, 0.1f, 5.0f).visibility { webs.GetSwitch() && mode.GetCombo() == "Factor" }
    val verticalFactor: Slider =
        Menu.Slider("Vertical Factor", 1.0f, 0.1f, 10.0f).visibility { webs.GetSwitch() && mode.GetCombo() == "Factor" }
    val timer: Slider =
        Menu.Slider("Timer", 1.0f, 0.1f, 20.0f).visibility { webs.GetSwitch() && mode.GetCombo() == "Timer" }
    var sneaked = false
    var timered = false
    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            EntityUseItemListener(this),
            ItemInputUpdateListener(this),
            MoveListener(this),
            TickListener(this)
        )
    }

    fun webbed(): Boolean {
        return mc.player.isInWeb
    }

    fun slowed(): Boolean {
        return items.GetSwitch() && mc.player.isHandActive && !mc.player.isRiding && !mc.player.isElytraFlying
    }
}