package dev.zprestige.fire.module.client.panel;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;
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