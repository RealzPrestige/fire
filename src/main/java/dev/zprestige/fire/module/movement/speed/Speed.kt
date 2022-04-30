package dev.zprestige.fire.module.movement.speed

import com.mojang.realmsclient.gui.ChatFormatting
import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.ComboBox
import dev.zprestige.fire.settings.impl.Key
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch
import org.lwjgl.input.Keyboard

@Descriptor(description = "Speeds up and controls moving")
class Speed : Module() {
    val speedMode: ComboBox = Menu.ComboBox("Speed Mode", "Strafe", arrayOf("OnGround", "Strafe"))
    private val dataMode: ComboBox = Menu.ComboBox("Data Mode", "Mode", arrayOf("Mode", "Factor"))
    val switchKey: Key = Menu.Key("Switch Key", Keyboard.KEY_NONE)
    val strict: Switch = Menu.Switch("Strict", false)
    val liquids: Switch = Menu.Switch("Liquids", false)
    val useTimer: Switch = Menu.Switch("Use Timer", false)
    val timerAmount: Slider = Menu.Slider("Timer Amount", 1.0f, 0.9f, 2.0f).visibility { z: Float? -> useTimer.GetSwitch() }
    val velocityBoost: Switch = Menu.Switch("Velocity Boost", false)
    val boostAmplifier: Slider = Menu.Slider("Velocity Boost Amplifier", 10.0f, 1.0f, 20.0f)
        .visibility { velocityBoost.GetSwitch() }
    val strafeFactor: Slider = Menu.Slider("Strafe Factor", 1.0f, 0.1f, 3.0f).visibility { z: Float? -> !strict.GetSwitch() }
    var previousDistance = 0.0
    var motionSpeed = 0.0
    var lastHealth = 0f
    var currentState = 1
    var damageMap = HashMap<Long, Float>()

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            KeyListener(this),
            MoveListener(this),
            TickListener(this)
        )
    }

    override fun onDisable() {
        Main.tickManager.syncTimer()
    }

    fun sendSwitchMessage() {
        Main.notificationManager.addNotifications("Speed mode switched to " + speedMode.GetCombo() + ".")
        Main.chatManager.sendRemovableMessage(ChatFormatting.WHITE.toString() + "" + ChatFormatting.BOLD + "Speed" + ChatFormatting.RESET + " mode switched to " + Main.chatManager.prefixColor + speedMode.GetCombo() + ChatFormatting.RESET + ".",
            1
        )
    }

    override fun getData(): String {
        when (dataMode.GetCombo()) {
            "Factor" -> return strafeFactor.GetSlider().toString() + ""
            "Mode" -> return speedMode.GetCombo()
        }
        return ""
    }
}