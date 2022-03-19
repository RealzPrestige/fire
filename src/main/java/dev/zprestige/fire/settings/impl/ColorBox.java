package dev.zprestige.fire.settings.impl;

import dev.zprestige.fire.settings.Setting;

import java.awt.*;

public class ColorBox extends Setting {
    protected Color value;

    public ColorBox(String name, Color value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setValue(Color value) {
        this.value = value;
    }

    public Color GetColor() {
        return value;
    }
}
