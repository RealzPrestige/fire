package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;
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
