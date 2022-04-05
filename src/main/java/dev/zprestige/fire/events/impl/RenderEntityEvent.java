package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import net.minecraft.entity.Entity;

public abstract class RenderEntityEvent extends Event {

    protected final Entity entity;
    protected final double x, y, z;

    private RenderEntityEvent(final Entity entity, final double x, final double y, final double z) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static class Pre extends RenderEntityEvent {
        public Pre(final Entity entity, final double x, final double y, final double z) {
            super(entity, x, y, z);
        }
    }

    public static class Post extends RenderEntityEvent {
        public Post(final Entity entity, final double x, final double y, final double z) {
            super(entity, x, y, z);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
    public Entity getEntity() {
        return entity;
    }


}