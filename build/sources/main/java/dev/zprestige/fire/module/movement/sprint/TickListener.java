package dev.zprestige.fire.module.movement.sprint;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.util.impl.EntityUtil;

public class TickListener extends EventListener<TickEvent, Sprint> {

    public TickListener(final Sprint sprint){
        super(TickEvent.class, sprint);
    }

    @Override
    public void invoke(final Object object){
        if (EntityUtil.isMoving()){
            mc.player.setSprinting(true);
        }
    }
}
