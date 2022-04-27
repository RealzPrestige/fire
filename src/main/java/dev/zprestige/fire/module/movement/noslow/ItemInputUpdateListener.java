package dev.zprestige.fire.module.movement.noslow;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.ItemInputUpdateEvent;
import net.minecraft.util.MovementInput;

public class ItemInputUpdateListener extends EventListener<ItemInputUpdateEvent, NoSlow> {

    public ItemInputUpdateListener(final NoSlow noSlow) {
        super(ItemInputUpdateEvent.class, noSlow);
    }

    @Override
    public void invoke(final Object object) {
        if (module.slowed()) {
            final ItemInputUpdateEvent event = (ItemInputUpdateEvent) object;
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveForward /= 0.2f;
            movementInput.moveStrafe /= 0.2f;
        }
    }
}
