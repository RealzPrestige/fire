package dev.zprestige.fire.module.misc.nointerpolation;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;

public class TickListener extends EventListener<TickEvent, NoInterpolation> {

    public TickListener(final NoInterpolation noInterpolation) {
        super(TickEvent.class, noInterpolation);
    }

    @Override
    public void invoke(final Object object) {
        module.i++;
    }
}
