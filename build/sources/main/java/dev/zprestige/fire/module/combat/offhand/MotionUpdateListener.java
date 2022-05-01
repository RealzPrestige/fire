package dev.zprestige.fire.module.combat.offhand;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.MotionUpdateEvent;

public class MotionUpdateListener extends EventListener<MotionUpdateEvent, Offhand> {

    public MotionUpdateListener(final Offhand offhand) {
        super(MotionUpdateEvent.class, offhand);
    }

    @Override
    public void invoke(final Object object) {
        final int slot = module.findSlot();
        if (slot != -1) {
            module.swapItem(slot);
        }
    }
}
