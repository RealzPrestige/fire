package dev.zprestige.fire.module.movement.speed;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.KeyEvent;

public class KeyListener extends EventListener<KeyEvent, Speed> {

    public KeyListener(final Speed speed){
        super(KeyEvent.class, speed);
    }

    @Override
    public void invoke(final Object object){
        final KeyEvent event = (KeyEvent) object;
        if (module.switchKey.GetKey() == event.getKey()) {
            if (module.speedMode.GetCombo().equals("Strafe")) {
                module.speedMode.setValue("OnGround");
            } else {
                module.speedMode.setValue("Strafe");
            }
            module.sendSwitchMessage();
        }
    }
}
