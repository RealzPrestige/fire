package dev.zprestige.fire.module.movement.step;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.MoveEvent;
import dev.zprestige.fire.util.impl.EntityUtil;

public class MoveListener extends EventListener<MoveEvent, Step> {

    public MoveListener(final Step step) {
        super(MoveEvent.class, step);
    }

    @Override
    public void invoke(final Object object) {
        if (module.checkRiding()) {
            switch (module.mode.GetCombo()) {
                case "Vanilla":
                    mc.player.stepHeight = module.height.GetSlider();
                    break;
                case "Ncp":
                    if (module.canStep()) {
                        final int height = (int) module.height.GetSlider();
                        final float[] i = EntityUtil.getSpeed(0.1f);
                        if (module.checkEmpty(height, i)) {
                            module.performStep(height, i);
                        }
                    }
                    break;
            }
        }
    }
}
