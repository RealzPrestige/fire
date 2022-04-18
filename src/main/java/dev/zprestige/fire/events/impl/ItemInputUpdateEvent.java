package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import net.minecraft.util.MovementInput;

public class ItemInputUpdateEvent extends Event {
    protected final MovementInput movementInput;

    public ItemInputUpdateEvent(MovementInput movementInput) {
        this.movementInput = movementInput;
    }
    public MovementInput getMovementInput() {
        return movementInput;
    }
}