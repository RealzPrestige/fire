package dev.zprestige.fire.module.client.clickgui;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;
import dev.zprestige.fire.ui.hudeditor.HudEditorScreen;
import dev.zprestige.fire.ui.menu.dropdown.MenuScreen;

public class TickListener extends EventListener<TickEvent, ClickGui> {

    public TickListener(final ClickGui clickGui){
        super(TickEvent.class, clickGui);
    }

    @Override
    public void invoke(final Object object) {
        if (!(mc.currentScreen instanceof MenuScreen) && !(mc.currentScreen instanceof HudEditorScreen)){
            module.disableModule();
        }
    }
}
