package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;
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