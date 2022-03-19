package dev.zprestige.fire.settings.impl;

import dev.zprestige.fire.settings.Setting;

public class Slider extends Setting {
    protected float value, min, max;

    public Slider(String name, float value, float min, float max) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
    }


    public void setValue(float value) {
        this.value = value;
    }

    public String getName() {
        return name;
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
}