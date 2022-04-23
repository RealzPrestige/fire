package dev.zprestige.fire.module.client.hudeditor;

import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;
import dev.zprestige.fire.ui.hudeditor.HudEditorScreen;

public class TickListener extends EventListener<TickEvent, HudEditor> {

    public TickListener(final HudEditor hudEditor) {
        super(TickEvent.class, hudEditor);
    }

    @Override
    public void invoke(final Object object) {
        if (!(mc.currentScreen instanceof HudEditorScreen)){
            module.disableModule();
            ClickGui.Instance.enableModule();
        }
    }
}
