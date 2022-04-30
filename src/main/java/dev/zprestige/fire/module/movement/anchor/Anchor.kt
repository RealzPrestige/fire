package dev.zprestige.fire.module.movement.anchor

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.ComboBox
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch
import dev.zprestige.fire.util.impl.Timer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i

@Descriptor(description = "Makes getting into holes easier")
class Anchor : Module() {
    val mode: ComboBox = Menu.ComboBox("Mode", "Stop Motion", arrayOf(
        "Stop Motion",
        "Pull",
        "Snap"
    )
    )
    val snapFall: Slider = Menu.Slider("Snap Fall", 1.0f, 0.1f, 5.0f).visibility { z: Float? -> mode.GetCombo() == "Snap" }
    val pullSpeed: Slider = Menu.Slider("Pull Speed", 1.0f, 0.1f, 5.0f).visibility { z: Float? -> mode.GetCombo() == "Pull" }
    val height: Slider = Menu.Slider("Height", 1.0f, 0.1f, 5.0f).visibility { z: Float? -> mode.GetCombo() == "Stop Motion" }
    val range: Slider = Menu.Slider("Range", 1.0f, 0.1f, 5.0f)
    val onGround: Switch = Menu.Switch("On Ground", true).visibility { z: Boolean? -> mode.GetCombo() == "Snap" }
    private val offsets = arrayOf(
        Vec3i(0, 0, -1),
        Vec3i(0, 0, 1),
        Vec3i(-1, 0, 0),
        Vec3i(1, 0, 0)
    )
    val timer = Timer()
    var pos: BlockPos? = null

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            TickListener(this)
        )
    }

    fun setMovementsFalse() {
        mc.gameSettings.keyBindForward.pressed = false
        mc.gameSettings.keyBindBack.pressed = false
        mc.gameSettings.keyBindRight.pressed = false
        mc.gameSettings.keyBindLeft.pressed = false
    }

    fun isOver(pos: BlockPos): Boolean {
        val bb = mc.player.entityBoundingBox
        var i = 0
        while (i < height.GetSlider()) {
            val pos1 = pos.up(i)
            val bb1 = AxisAlignedBB(pos1)
            if (bb.intersects(bb1)) {
                return true
            }
            i++
        }
        return false
    }

    fun intersects(pos: BlockPos): Boolean {
        val bb = mc.player.entityBoundingBox
        for (i in 0..4) {
            for (vec3i in offsets) {
                val pos1 = pos.up(i).add(vec3i)
                val bb1 = AxisAlignedBB(pos1)
                if (bb.intersects(bb1)) {
                    return false
                }
            }
        }
        return true
    }
}