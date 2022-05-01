package dev.zprestige.fire.module.movement.antivoid;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.MoveEvent;

public class MoveListener extends EventListener<MoveEvent, AntiVoid> {

    public MoveListener(final AntiVoid antiVoid){
        super(MoveEvent.class, antiVoid);
    }

    @Override
    public void invoke(final Object object){
        final MoveEvent event = (MoveEvent) object;
        if (!module.alreadyInVoid && mc.player.posY <= 0.0f) {
            event.setCancelled();
        }
    }
}
