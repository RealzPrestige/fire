package dev.zprestige.fire.module.movement.longjump;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;

public class TickListener extends EventListener<TickEvent, LongJump> {

    public TickListener(final LongJump longJump){
        super(TickEvent.class, longJump);
    }

    @Override
    public void invoke(final Object object){
        module.previousDistance = Math.sqrt((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX) + (mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ));
        if (module.useTimer.GetSwitch()) {
            Main.tickManager.setTimer(module.timerAmount.GetSlider());
        }
    }
}
