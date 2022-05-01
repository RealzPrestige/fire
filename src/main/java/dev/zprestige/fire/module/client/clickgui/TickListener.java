package dev.zprestige.fire.module.client.clickgui;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.ui.panel.PanelScreen;

public class TickListener extends EventListener<TickEvent, ClickGui> {

    public TickListener(final ClickGui panel) {
        super(TickEvent.class, panel);
    }

    @Override
    public void invoke(final Object object) {
        if (!(mc.currentScreen instanceof PanelScreen)) {
            module.disableModule();
        }
    }
}