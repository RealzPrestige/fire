package dev.zprestige.fire.module.movement.noslow;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.MoveEvent;

public class MoveListener extends EventListener<MoveEvent, NoSlow> {

    public MoveListener(final NoSlow noSlow) {
        super(MoveEvent.class, noSlow);
    }

    @Override
    public void invoke(final Object object) {
        if (module.webs.GetSwitch() && module.webbed() && module.mode.GetCombo().equals("Timer")) {
            Main.tickManager.setTimer(module.timer.GetSlider());
            module.timered = true;
        }
    }
}
