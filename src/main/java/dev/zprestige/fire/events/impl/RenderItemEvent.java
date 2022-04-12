package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class RenderItemEvent extends Event {
    protected final ItemStack stack;
    protected final EntityLivingBase entityLivingBase;

    public RenderItemEvent(ItemStack stack, EntityLivingBase entityLivingBase) {
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