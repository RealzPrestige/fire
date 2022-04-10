package dev.zprestige.fire.settings.impl;

import dev.zprestige.fire.settings.Setting;

import java.util.function.Predicate;

public class Slider extends Setting<Float> {
    protected float value, min, max;

    public Slider(final String name, final float value, final float min, final float max) {
        super(name, value);
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float GetSlider() {
        return value;
    }

    public Slider visibility(final Predicate<Float> visibility) {
        shown = visibility;
        return this;
    }

    public Slider panel(final String panel){
        this.panel = panel;
        return this;
    }

    @Override
    public Float getValue() {
        return GetSlider();
    }
}