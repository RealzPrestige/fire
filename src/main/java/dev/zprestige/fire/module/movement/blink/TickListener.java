package dev.zprestige.fire.module.movement.blink;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;

public class TickListener extends EventListener<TickEvent, Blink> {

    public TickListener(final Blink blink) {
        super(TickEvent.class, blink);
    }

    @Override
    public void invoke(final Object object) {
        if (module.mode.GetCombo().equals("Pulse")) {
            module.i++;
        }
        if (module.i >= module.ticks.GetSlider()) {
            module.poll(true);
            module.spawnEntity();
            module.i = 0;
        }
    }
}