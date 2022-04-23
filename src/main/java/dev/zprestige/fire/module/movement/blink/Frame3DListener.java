package dev.zprestige.fire.module.movement.blink;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.FrameEvent;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, Blink> {

    public Frame3DListener(final Blink blink){
        super(FrameEvent.FrameEvent3D.class, blink);
    }

    @Override
    public void invoke(final Object object){
        final FrameEvent.FrameEvent3D event = (FrameEvent.FrameEvent3D) object;
        if (module.entity != null) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 0.2f);
            RenderUtil.renderEntity(module.entity, event.getPartialTicks());
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
