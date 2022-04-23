package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;
import net.minecraft.entity.Entity;

public class DeathEvent extends Event {
    protected final Entity entity;

    public DeathEvent(final Entity entity){
        super(Stage.None, false);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}