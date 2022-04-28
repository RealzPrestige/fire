package dev.zprestige.fire.module.player.freecam;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.util.impl.EntityUtil;

public class TickListener extends EventListener<TickEvent, Freecam> {

    public TickListener(final Freecam freecam){
        super(TickEvent.class, freecam);
    }

    @Override
    public void invoke(final Object object){
        mc.player.noClip = true;
        final float[] speed = EntityUtil.getSpeed(module.speed.GetSlider());
        mc.player.motionX = speed[0];
        mc.player.motionZ = speed[1];
        mc.player.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? module.speed.GetSlider() : mc.gameSettings.keyBindSneak.isKeyDown() ? -module.speed.GetSlider() : 0.0f;
    }
}
