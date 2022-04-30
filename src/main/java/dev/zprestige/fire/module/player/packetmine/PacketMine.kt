package dev.zprestige.fire.module.player.packetmine

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.*
import dev.zprestige.fire.util.impl.BlockUtil
import dev.zprestige.fire.util.impl.Timer
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import org.lwjgl.input.Keyboard
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

@Descriptor(description = "Assists and renders when you mine blocks")
class PacketMine : Module() {
    val range: Slider = Menu.Slider("Range", 5.0f, 0.1f, 10.0f)
    val silentSwitch: ComboBox = Menu.ComboBox("Silent On Finish", "None", arrayOf(
        "None",
        "Clicked"
    )
    )
    val instant: Switch = Menu.Switch("Instant", false)
    val instantKey: Key = Menu.Key("Instant Key", Keyboard.KEY_NONE).visibility { z: Int? -> instant.GetSwitch() }
    val instantTiming: Slider =
        Menu.Slider("Instant Timing", 100.0f, 0.1f, 500.0f).visibility { z: Float? -> instant.GetSwitch() }
    val instantSilentSwitch: Switch =
        Menu.Switch("Instant Silent Switch", false).visibility { z: Boolean? -> instant.GetSwitch() }
    val instantPlaceCrystal: Switch =
        Menu.Switch("Instant Place Crystal", false).visibility { z: Boolean? -> instant.GetSwitch() }
    val rotate: Switch = Menu.Switch("Rotate", false)
    val abortOnSwitch: Switch = Menu.Switch("Abort On Switch", false)
    val reBreak: Switch = Menu.Switch("ReBreak", false)
    val render: Switch = Menu.Switch("Render", false)
    val renderMode: ComboBox = Menu.ComboBox("Render Mode", "Fade", arrayOf(
        "Fade",
        "Grow",
        "Shrink",
        "Height"
    )
    ).visibility { render.GetSwitch() }
    val colorMode: ComboBox = Menu.ComboBox("Color Mode", "Fade", arrayOf(
        "Static",
        "Fade"
    )
    ).visibility { render.GetSwitch() }
    val inactiveColor: ColorBox = Menu.Color("Inactive Color", Color.RED).visibility { render.GetSwitch() }
    val activeColor: ColorBox = Menu.Color("Active Color", Color.GREEN).visibility { render.GetSwitch() }
    val outlineWidth: Slider = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f).visibility { render.GetSwitch() }
    var changed = false
    var attemptingReBreak = false
    var shouldCancel = false
    var facing: EnumFacing? = null
    var prevFace: EnumFacing? = null
    var minedFace: EnumFacing? = null
    var activePos: BlockPos? = null
    var prevPos: BlockPos? = null
    var minedPos: BlockPos? = null
    val timer = Timer()
    var col = floatArrayOf()
    var size = 0f

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            ClickBlockListener(this),
            DamageBlockListener(this),
            Frame3DListener(this),
            PacketSendListener(this),
            TickListener(this)
        )
    }

    fun safety(input: Float): Float {
        return min(1.0f, max(0.0f, input))
    }

    fun updateValue(activePos: BlockPos?): Float {
        return if (BlockUtil.getState(activePos) == Blocks.OBSIDIAN) {
            0.025f
        } else if (BlockUtil.getState(activePos) == Blocks.ENDER_CHEST) {
            0.05f
        } else {
            0.5f
        }
    }

    fun updateColor(input: Float, target: Float, factor: Float): Float {
        if (input > target) {
            return input - input * factor / 10
        }
        return if (input < target) {
            input + (target - input) * factor / 10
        } else input
    }

    fun attemptBreak(pos: BlockPos?, enumFacing: EnumFacing?) {
        if (rotate.GetSwitch()) {
            Main.rotationManager.facePos(pos)
        }
        Main.interactionManager.attemptBreak(pos, enumFacing)
        prevPos = pos
        prevFace = enumFacing
        if (reBreak.GetSwitch() && BlockUtil.getState(pos).equals(Blocks.AIR)) {
            attemptingReBreak = true
        }
    }

    fun initiateBreaking(pos: BlockPos?, enumFacing: EnumFacing?) {
        if (rotate.GetSwitch()) {
            Main.rotationManager.facePos(pos)
        }
        Main.interactionManager.initiateBreaking(pos, enumFacing, true)
        activePos = pos
        col = floatArrayOf(inactiveColor.GetColor().red.toFloat(),
            inactiveColor.GetColor().green.toFloat(),
            inactiveColor.GetColor().blue.toFloat(),
            inactiveColor.GetColor().alpha.toFloat()
        )
        facing = enumFacing
    }

    fun abort(pos: BlockPos?, enumFacing: EnumFacing?) {
        if (rotate.GetSwitch()) {
            Main.rotationManager.facePos(pos)
        }
        Main.interactionManager.abort(pos, enumFacing)
        end()
    }

    fun abortWithoutEnding(pos: BlockPos?, enumFacing: EnumFacing?) {
        if (rotate.GetSwitch()) {
            Main.rotationManager.facePos(pos)
        }
        Main.interactionManager.abort(pos, enumFacing)
    }

    fun end() {
        minedPos = activePos
        minedFace = facing
        activePos = null
        facing = null
        size = 0.0f
    }
}
