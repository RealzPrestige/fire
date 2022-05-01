package dev.zprestige.fire.module.combat.holefiller;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.FrameEvent;
import dev.zprestige.fire.util.impl.RenderUtil;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, HoleFiller> {

    public Frame3DListener(final HoleFiller holeFiller) {
        super(FrameEvent.FrameEvent3D.class, holeFiller);
    }

    @Override
    public void invoke(final Object object) {
        if (module.render.GetSwitch() && module.renderPos != null) {
            if (module.box.GetSwitch()) {
                RenderUtil.drawBox(module.renderPos, module.boxColor.GetColor());
            }
            if (module.outline.GetSwitch()) {
                RenderUtil.drawOutline(module.renderPos, module.outlineColor.GetColor(), module.outlineWidth.GetSlider());
            }
        }
    }
}
