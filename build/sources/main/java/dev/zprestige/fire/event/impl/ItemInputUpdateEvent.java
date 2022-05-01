package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;
import net.minecraft.util.MovementInput;

public class ItemInputUpdateEvent extends Event {
    protected final MovementInput movementInput;

    public ItemInputUpdateEvent(final MovementInput movementInput) {
        super(Stage.None, false);
        this.movementInput = movementInput;
    }

    public MovementInput getMovementInput() {
        return movementInput;
    }
}