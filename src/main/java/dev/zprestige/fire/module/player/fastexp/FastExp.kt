package dev.zprestige.fire.module.player.fastexp

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.ComboBox
import dev.zprestige.fire.settings.impl.Key
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch
import org.lwjgl.input.Keyboard

@Descriptor(description = "Speeds up throwing Exp")
class FastExp : Module() {
    val mode: ComboBox = Menu.ComboBox("Mode", "Packet", arrayOf(
        "Vanilla",
        "Packet"
    )
    )
    val activateMode: ComboBox = Menu.ComboBox("Activate Mode", "RightClick", arrayOf(
        "RightClick",
        "MiddleClick",
        "Custom"
    )
    ).visibility { mode.GetCombo() == "Packet" }
    val customKey: Key = Menu.Key("Custom Key", Keyboard.KEY_NONE)
        .visibility { mode.GetCombo() == "Packet" && activateMode.GetCombo() == "Custom" }
    val packets: Slider = Menu.Slider("Packets", 2, 1, 10).visibility { mode.GetCombo() == "Packet" }
    val handOnly: Switch = Menu.Switch("Hand Only", false).visibility { mode.GetCombo() == "Packet" }

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            TickListener(this)
        )
    }
}