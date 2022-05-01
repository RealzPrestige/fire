package dev.zprestige.fire.module.movement.noslow;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.util.impl.EntityUtil;

public class TickListener extends EventListener<TickEvent, NoSlow> {

    public TickListener(final NoSlow noSlow) {
        super(TickEvent.class, noSlow);
    }

    @Override
    public void invoke(final Object object) {
        if (module.timered && !module.mode.GetCombo().equals("Timer")) {
            Main.tickManager.syncTimer();
        }
        if (module.webs.GetSwitch()) {
            switch (module.mode.GetCombo()) {
                case "Vanilla":
                    if (module.webbed()) {
                        mc.player.isInWeb = false;
                    }
                    break;
                case "Ncp":
                    if (module.webbed()) {
                        if (!module.sneaked) {
                            mc.player.setSneaking(true);
                            module.sneaked = true;
                        }
                        mc.player.isInWeb = false;
                    } else if (module.sneaked) {
                        mc.player.setSneaking(false);
                        module.sneaked = false;
                    }
                    break;
                case "Fast":
                    if (module.webbed()) {
                        final float factor = module.fastFactor.GetSlider();
                        mc.player.motionX *= factor;
                        mc.player.motionY *= factor;
                        mc.player.motionZ *= factor;
                    }
                    break;
                case "Factor":
                    if (module.webbed()) {
                        final float[] speed = EntityUtil.getSpeed(module.factor.GetSlider());
                        mc.player.motionX = speed[0];
                        mc.player.motionY = -module.verticalFactor.GetSlider();
                        mc.player.motionZ = speed[0];
                    }
                    break;
                case "Timer":
                    if (module.webbed() && !mc.player.onGround) {
                        Main.tickManager.setTimer(module.timer.GetSlider());
                        module.timered = true;
                        break;
                    }
                    if (module.timered) {
                        if (!EntityUtil.isMoving() || !module.webbed()) {
                            Main.tickManager.syncTimer();
                            module.timered = false;
                        }
                    }
                    break;
            }
        }
    }
}
