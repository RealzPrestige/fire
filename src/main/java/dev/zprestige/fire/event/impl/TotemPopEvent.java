package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;
import net.minecraft.entity.player.EntityPlayer;

public class TotemPopEvent extends Event {
    protected final EntityPlayer entityPlayer;

    public TotemPopEvent(final EntityPlayer entityPlayer) {
        super(Stage.None, false);
        this.entityPlayer = entityPlayer;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }
}
