package dev.zprestige.fire.module.combat.autoarmor;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.KeyEvent;

public class KeyListener extends EventListener<KeyEvent, AutoArmor> {

    public KeyListener(final AutoArmor autoArmor){
        super(KeyEvent.class, autoArmor);
    }

    @Override
    public void invoke(final Object object) {
        final KeyEvent event = (KeyEvent) object;
        if (event.getKey() == module.singleMend.GetKey()) {
            module.takingOff = !module.takingOff;
        }
        if (event.getKey() == module.elytraSwap.GetKey()) {
            module.elytra = !module.elytra ;
        }
    }
}
