package dev.zprestige.fire.module.movement.elytrafly;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.MoveEvent;
import dev.zprestige.fire.util.impl.EntityUtil;

public class MoveListener extends EventListener<MoveEvent, ElytraFly> {

    public MoveListener(final ElytraFly elytraFly) {
        super(MoveEvent.class, elytraFly);
    }

    @Override
    public void invoke(final Object object) {
        if (!mc.player.isElytraFlying() || mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }
        final MoveEvent event = (MoveEvent) object;
        final float glideSpeed = -module.glideSpeed.GetSlider() / 100.0f;
        final float[] speed = EntityUtil.getSpeed(module.speed.GetSlider());
        final float x = speed[0], z = speed[1];
        event.setMotion(x, glideSpeed, z);
        mc.player.motionX = x;
        mc.player.motionY = glideSpeed;
        mc.player.motionZ = z;
    }
}
