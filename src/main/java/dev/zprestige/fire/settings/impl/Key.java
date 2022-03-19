package dev.zprestige.fire.settings.impl;

import dev.zprestige.fire.settings.Setting;

public class Key extends Setting {
    protected int key;
    protected boolean hold;

    public Key(String name, int key) {
        this.name = name;
        this.key = key;
        this.hold = false;
    }

    public String getName() {
        return name;
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
}
