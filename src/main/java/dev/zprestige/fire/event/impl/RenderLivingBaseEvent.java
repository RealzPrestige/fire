package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

public class RenderLivingBaseEvent extends Event {
    protected final ModelBase modelBase;
    protected final EntityLivingBase entityLivingBase;
    protected final float limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor;

    public RenderLivingBaseEvent(ModelBase modelBase, EntityLivingBase entityLivingBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super(Stage.None, true);
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

    public void render() {
        getModelBase().render(getEntityLivingBase(), getLimbSwing(), getLimbSwingAmount(), getAgeInTicks(), getNetHeadYaw(), getHeadPitch(), getScaleFactor());
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
