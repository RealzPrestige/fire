package dev.zprestige.fire.module.client.panel;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;

public class TickListener extends EventListener<TickEvent, Panel> {

    public TickListener(final Panel panel) {
        super(TickEvent.class, panel);
    }

    @Override
    public void invoke(final Object object) {
        if (!(mc.currentScreen instanceof PanelScreen)) {
            module.disableModule();
        }
    }
}