package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import dev.zprestige.fire.events.eventbus.event.IsCancellable;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

@IsCancellable
public class RenderCrystalEvent extends Event {
    protected final ModelBase modelBase;
    protected final Entity entity;
    protected final float limbSwing;
    protected final float limbSwingAmount;
    protected final float ageInTicks;
    protected final float netHeadYaw;
    protected final float headPitch;
    protected final float scaleFactor;

    public RenderCrystalEvent(ModelBase modelBase, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, final float scaleFactor) {
        this.modelBase = modelBase;
        this.entity = entity;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scaleFactor = scaleFactor;
    }

    public ModelBase getModelBase() {
        return this.modelBase;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public float getLimbSwing() {
        return this.limbSwing;
    }

    public float getLimbSwingAmount() {
        return this.limbSwingAmount;
    }

    public float getAgeInTicks() {
        return this.ageInTicks;
    }

    public float getNetHeadYaw() {
        return this.netHeadYaw;
    }

    public float getHeadPitch() {
        return this.headPitch;
    }

    public float getScaleFactor() {
        return this.scaleFactor;
    }
}