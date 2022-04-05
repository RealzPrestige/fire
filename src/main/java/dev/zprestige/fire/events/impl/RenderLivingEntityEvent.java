package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import dev.zprestige.fire.events.eventbus.event.IsCancellable;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

@IsCancellable
public class RenderLivingEntityEvent extends Event {
    protected final ModelBase modelBase;
    protected final EntityLivingBase entityLivingBase;
    protected final float limbSwing;
    protected final float limbSwingAmount;
    protected final float ageInTicks;
    protected final float netHeadYaw;
    protected final float headPitch;
    protected final float scaleFactor;

    public RenderLivingEntityEvent(final ModelBase modelBase, final EntityLivingBase entityLivingBase, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
        this.modelBase = modelBase;
        this.entityLivingBase = entityLivingBase;
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

    public EntityLivingBase getEntityLivingBase() {
        return this.entityLivingBase;
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