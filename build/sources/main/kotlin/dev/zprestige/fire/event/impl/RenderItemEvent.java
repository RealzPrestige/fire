package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class RenderItemEvent extends Event {
    protected final ItemStack stack;
    protected final EntityLivingBase entityLivingBase;

    public RenderItemEvent(final ItemStack stack, final EntityLivingBase entityLivingBase) {
        super(Stage.None, false);
        this.stack = stack;
        this.entityLivingBase = entityLivingBase;
    }

    public static class MainHand extends RenderItemEvent {
        public MainHand(ItemStack stack, EntityLivingBase entityLivingBase) {
            super(stack, entityLivingBase);
        }
    }

    public static class Offhand extends RenderItemEvent {
        public Offhand(ItemStack stack, EntityLivingBase entityLivingBase) {
            super(stack, entityLivingBase);
        }
    }

    public EntityLivingBase getEntityLivingBase() {
        return entityLivingBase;
    }

    public ItemStack getStack() {
        return stack;
    }
}