package dev.zprestige.fire.manager.hudmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.FrameEvent;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;

public class Frame2DListener extends EventListener<FrameEvent.FrameEvent2D, Object> {

    public Frame2DListener() {
        super(FrameEvent.FrameEvent2D.class);
    }

    @Override
    public void invoke(final Object object) {
        if (Main.listener.checkNull()) {
            Main.hudManager.hudComponents.stream().filter(HudComponent::isEnabled).forEach(HudComponent::render);
        }

    }
}
