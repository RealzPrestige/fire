package dev.zprestige.fire.module.player.packetmine

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.FrameEvent.FrameEvent3D
import dev.zprestige.fire.settings.impl.ColorBox
import dev.zprestige.fire.util.impl.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.util.math.AxisAlignedBB
import java.awt.Color

class Frame3DListener(packetMine: PacketMine) : EventListener<FrameEvent3D, PacketMine>(
    FrameEvent3D::class.java, packetMine
) {

    override fun invoke(`object`: Any) {
        if (module.nullCheck() && module.activePos != null) {
            val factor = Minecraft.getDebugFPS() / 20.0f
            val updateValue = module.updateValue(module.activePos)
            if (module.size < 1.0f) {
                module.size += updateValue / factor
            }
            if (module.render.GetSwitch()) {
                val bb = AxisAlignedBB(module.activePos!!)
                var color = module.inactiveColor.GetColor()
                when (module.colorMode.GetCombo()) {
                    "Static" -> if (module.size > 0.95f) {
                        color = module.activeColor.GetColor()
                    }
                    "Fade" -> {
                        val active = module.activeColor.GetColor()
                        val fac = updateValue * module.size
                        module.col[0] = module.updateColor(module.col[0], active.red.toFloat(), fac)
                        module.col[1] = module.updateColor(module.col[1], active.green.toFloat(), fac)
                        module.col[2] = module.updateColor(module.col[2], active.blue.toFloat(), fac)
                        module.col[3] = module.updateColor(module.col[3], active.alpha.toFloat(), fac)
                        color = Color(module.safety(module.col[0] / 255.0f),
                            module.safety(module.col[1] / 255.0f),
                            module.safety(
                                module.col[2] / 255.0f
                            ),
                            module.safety(module.col[3] / 255.0f)
                        )
                    }
                }
                when (module.renderMode.GetCombo()) {
                    "Shrink" -> {
                        val bb1 = bb.shrink((module.size / 2.0f).toDouble())
                        RenderUtil.drawBoxWithHeight(bb1, color, 1f)
                        RenderUtil.drawBlockOutlineBBWithHeight(bb1, color, module.outlineWidth.GetSlider(), 1f)
                    }
                    "Grow" -> {
                        val bb2 = bb.shrink((0.5f - module.size / 2.0f).toDouble())
                        RenderUtil.drawBoxWithHeight(bb2, color, 1f)
                        RenderUtil.drawBlockOutlineBBWithHeight(bb2, color, module.outlineWidth.GetSlider(), 1f)
                    }
                    "Height" -> {
                        RenderUtil.drawBoxWithHeight(bb, color, module.size)
                        RenderUtil.drawBlockOutlineBBWithHeight(bb,
                            color,
                            module.outlineWidth.GetSlider(),
                            module.size
                        )
                    }
                    "Fade" -> {
                        val colorBox: ColorBox
                        if (module.size > 0.9f) {
                            if (module.changed) {
                                Main.fadeManager.removePosition(module.activePos)
                                module.changed = false
                            }
                            colorBox = module.activeColor
                        } else {
                            module.changed = true
                            colorBox = module.inactiveColor
                        }
                        Main.fadeManager.createFadePosition(module.activePos,
                            colorBox.GetColor(),
                            colorBox.GetColor(),
                            true,
                            true,
                            module.outlineWidth.GetSlider(),
                            50f,
                            colorBox.GetColor().alpha.toFloat()
                        )
                    }
                }
            }
        }
    }
}