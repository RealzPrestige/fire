package dev.zprestige.fire.module.movement.blink

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.FrameEvent.FrameEvent3D
import dev.zprestige.fire.util.impl.RenderUtil
import net.minecraft.client.renderer.GlStateManager

class Frame3DListener(blink: Blink) : EventListener<FrameEvent3D, Blink>(FrameEvent3D::class.java, blink) {
    
    override fun invoke(e: Any) {
        val event = e as FrameEvent3D
        if (module.entity != null) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 0.2f)
            RenderUtil.renderEntity(module.entity, event.partialTicks)
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        }
    }
}
