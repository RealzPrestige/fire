package dev.zprestige.fire.manager.tickmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;

public class TickListener extends EventListener<TickEvent, Object> {

    public TickListener() {
        super(TickEvent.class);
    }

    @Override
    public void invoke(final Object object) {
        if (Main.tickManager.timer != 1.0f) {
            mc.timer.tickLength = 50.0f / Main.tickManager.timer;
        }
    }
}
