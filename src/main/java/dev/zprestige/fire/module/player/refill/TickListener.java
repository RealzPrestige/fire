package dev.zprestige.fire.module.player.refill;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;
import dev.zprestige.fire.util.impl.EntityUtil;

import java.util.stream.IntStream;

public class TickListener extends EventListener<TickEvent, Refill> {

    public TickListener(final Refill refill){
        super(TickEvent.class, refill);
    }

    @Override
    public void invoke(final Object object){
        if (mc.currentScreen == null && (!module.strict.GetSwitch() || !EntityUtil.isMoving()) && module.timer.getTime((long) module.delay.GetSlider()) && IntStream.range(0, 9).anyMatch(i -> module.refillSlot(i))) {
            module.timer.syncTime();
        }
    }
}
