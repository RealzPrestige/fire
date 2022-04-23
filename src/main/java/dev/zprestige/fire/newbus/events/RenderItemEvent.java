package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;
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