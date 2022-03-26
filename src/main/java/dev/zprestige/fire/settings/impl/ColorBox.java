package dev.zprestige.fire.settings.impl;

import dev.zprestige.fire.settings.Setting;

import java.awt.*;
import java.util.function.Predicate;

public class ColorBox extends Setting<Color> {
    protected Color value;

    public ColorBox(final String name,final Color value) {
        super(name, value);
        this.name = name;
        this.value = value;
    }
    public void setValue(Color value) {
        this.value = value;
    }

    public Color GetColor() {
        return value;
    }

    public ColorBox visibility(final Predicate<Color> visibility){
        shown = visibility;
        return this;
    }

    @Override
    public Color getValue(){
        return GetColor();
    }
}
