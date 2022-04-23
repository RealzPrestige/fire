package dev.zprestige.fire.module.movement.speed;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class TickListener extends EventListener<TickEvent, Speed> {

    public TickListener(final Speed speed) {
        super(TickEvent.class, speed);
    }

    @Override
    public void invoke(final Object object) {
        module.previousDistance = Math.sqrt((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX) + (mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ));
        if (module.useTimer.GetSwitch()) {
            Main.tickManager.setTimer(module.timerAmount.GetSlider());
        }
        final float health = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        float damage = 0.0f;
        for (Map.Entry<Long, Float> entry : new HashMap<>(module.damageMap).entrySet()) {
            double val = entry.getValue() / (((System.currentTimeMillis() - entry.getKey())) / module.boostAmplifier.GetSlider());
            if (val < 0.1) {
                module.damageMap.remove(entry.getKey());
            }
            damage += val;
        }
        final float dmg = module.lastHealth - health;
        if (dmg > 0) {
            module.damageMap.put(System.currentTimeMillis(), dmg);
        }
        module.lastHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        if (module.strict.GetSwitch()) {
            module.strafeFactor.setValue(0.9f);
        } else {
            if (module.velocityBoost.GetSwitch()) {
                module.strafeFactor.setValue(Math.max(1.0f, damage));
            }
        }
    }
}
