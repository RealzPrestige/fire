package dev.zprestige.fire.module.misc.removeexpdesync;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.util.impl.EntityUtil;

public class TickListener extends EventListener<TickEvent, RemoveEXPDesync> {

    public TickListener(final RemoveEXPDesync removeEXPDesync){
        super(TickEvent.class, removeEXPDesync);
    }

    @Override
    public void invoke(final Object object){
        if (module.started) {
            if (module.index < module.attempts.GetSlider()) {
                mc.player.setPosition(mc.player.posX, mc.player.posY - module.force.GetSlider(), mc.player.posZ );
                module.index++;
                Main.tickManager.setTimer(module.timer.GetSlider());
            } else {
                Main.tickManager.syncTimer();
                module.index = 0;
                module.started = false;
            }
        }
        module.activeEntities = mc.world.loadedEntityList;
    }
}
