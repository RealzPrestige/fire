package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import net.minecraft.entity.player.EntityPlayer;

public class TotemPopEvent extends Event {
    protected final EntityPlayer entityPlayer;

    public TotemPopEvent(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }
}
