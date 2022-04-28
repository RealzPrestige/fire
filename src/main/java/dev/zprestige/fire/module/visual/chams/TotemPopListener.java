package dev.zprestige.fire.module.visual.chams;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TotemPopEvent;

public class TotemPopListener extends EventListener<TotemPopEvent, Chams> {

    public TotemPopListener(final Chams chams) {
        super(TotemPopEvent.class, chams);
    }

    @Override
    public void invoke(final Object object) {
        final TotemPopEvent event = (TotemPopEvent) object;
        if (module.popChams.GetSwitch()) {
            module.addEntity(event.getEntityPlayer());
        }
    }
}
