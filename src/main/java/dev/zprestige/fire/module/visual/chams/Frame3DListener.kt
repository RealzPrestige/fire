package dev.zprestige.fire.module.visual.chams

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.FrameEvent.FrameEvent3D
import dev.zprestige.fire.util.impl.RenderUtil
import net.minecraft.client.Minecraft
import java.awt.Color

class Frame3DListener(chams: Chams) : EventListener<FrameEvent3D, Chams>(FrameEvent3D::class.java, chams) {
    
    override fun invoke(e: Any) {
        val event = e as FrameEvent3D
        val partialTicks = event.partialTicks
        val fps = Minecraft.getDebugFPS()
        for (popEntity in ArrayList(module.popEntities)) {
            val alpha = popEntity.alpha.toInt()
            val entity = popEntity.entity
            if (module.popAnimateVertical.GetSwitch()) {
                entity.posY += (module.popVerticalAnimationSpeed.GetSlider() / 100.0f).toDouble()
            }
            if (module.popFill.GetSwitch()) {
                val color = module.popFillColor.GetColor()
                module.prepareFill(Color(color.red, color.green, color.blue, alpha))
                RenderUtil.renderEntity(entity, partialTicks)
                module.releaseFill()
            }
            if (module.popOutline.GetSwitch()) {
                val color = module.popOutlineColor.GetColor()
                module.prepareOutline(Color(color.red, color.green, color.blue, alpha),
                    module.popOutlineWidth.GetSlider()
                )
                RenderUtil.renderEntity(entity, partialTicks)
                module.releaseOutline()
            }
            popEntity.updateAlpha(fps)
            if (popEntity.alpha < 30) {
                module.popEntities.remove(popEntity)
            }
        }
    }
}
