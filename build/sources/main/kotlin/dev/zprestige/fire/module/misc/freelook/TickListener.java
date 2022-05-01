package dev.zprestige.fire.module.misc.freelook;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;

public class TickListener extends EventListener<TickEvent, Freelook> {

    public TickListener(final Freelook freelook){
        super(TickEvent.class, freelook);
    }

    @Override
    public void invoke(final Object object){
        mc.gameSettings.thirdPersonView = 1;
    }
}
