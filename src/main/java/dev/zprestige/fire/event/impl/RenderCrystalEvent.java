package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class RenderCrystalEvent extends Event {
    protected final ModelBase modelBase;
    protected final Entity entity;
    protected final float limbSwing;
    protected final float limbSwingAmount;
    protected final float ageInTicks;
    protected final float netHeadYaw;
    protected final float headPitch;
    protected final float scaleFactor;

    public RenderCrystalEvent(final ModelBase modelBase, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
        super(Stage.None, true);
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

    public void render(){
        getModelBase().render(getEntity(), getLimbSwing(), getLimbSwingAmount(), getAgeInTicks(), getNetHeadYaw(), getHeadPitch(), getScaleFactor());
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