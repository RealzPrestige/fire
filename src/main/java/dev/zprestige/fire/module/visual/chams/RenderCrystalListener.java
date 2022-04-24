package dev.zprestige.fire.module.visual.chams;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.RenderCrystalEvent;

public class RenderCrystalListener extends EventListener<RenderCrystalEvent, Chams> {

    public RenderCrystalListener(final Chams chams) {
        super(RenderCrystalEvent.class, chams);
    }

    @Override
    public void invoke(final Object object) {
        final RenderCrystalEvent event = (RenderCrystalEvent) object;
        event.setCancelled();
        if (module.fill.GetSwitch()) {
            module.prepareFill();
            event.render();
            module.releaseFill();
        }
        if (module.outline.GetSwitch()) {
            module.prepareOutline();
            event.render();
            module.releaseOutline();
        }
    }
}