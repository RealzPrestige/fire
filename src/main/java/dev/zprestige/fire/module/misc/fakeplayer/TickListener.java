package dev.zprestige.fire.module.misc.fakeplayer;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;

public class TickListener extends EventListener<TickEvent, FakePlayer> {

    public TickListener(final FakePlayer fakePlayer) {
        super(TickEvent.class, fakePlayer);
    }

    @Override
    public void invoke(final Object object) {
        if (module.fakePlayer != null && module.fakePlayer.getDistanceSq(mc.player) > 100000) {
            mc.world.removeEntityFromWorld(module.id);
            module.disableModule();
        }
    }
}
