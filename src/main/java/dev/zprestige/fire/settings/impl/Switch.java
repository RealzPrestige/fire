package dev.zprestige.fire.settings.impl;

import dev.zprestige.fire.settings.Setting;

public class Switch extends Setting {
    protected boolean value;

    public Switch(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean GetSwitch() {
        return value;
    }

}
