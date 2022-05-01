package dev.zprestige.fire.settings.impl;

import dev.zprestige.fire.settings.Setting;

import java.util.function.Predicate;


public class Key extends Setting<Integer> {
    protected int key;
    protected boolean hold;

    public Key(final String name, final int key) {
        super(name, key);
        this.name = name;
        this.key = key;
        this.hold = false;
    }

    public void setValue(int value) {
        this.key = value;
    }

    public int GetKey() {
        return key;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public boolean isHold() {
        return hold;
    }

    public Key visibility(final Predicate<Integer> visibility) {
        shown = visibility;
        return this;
    }

    public Key panel(final String panel) {
        this.panel = panel;
        return this;
    }

    @Override
    public Integer getValue() {
        return GetKey();
    }
}
