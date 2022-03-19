package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import net.minecraft.entity.Entity;

public class DeathEvent extends Event {
    protected final Entity entity;

    public DeathEvent(Entity entity){
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
