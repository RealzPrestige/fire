package dev.zprestige.fire.settings.impl;

import dev.zprestige.fire.settings.Setting;

import java.util.function.Predicate;

public class Switch extends Setting<Boolean> {
    protected boolean value;

    public Switch(final String name, final boolean value) {
        super(name, value);
        this.name = name;
        this.value = value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean GetSwitch() {
        return value;
    }

    public Switch visibility(final Predicate<Boolean> visibility) {
        shown = visibility;
        return this;
    }

    public Switch panel(final String panel) {
        this.panel = panel;
        return this;
    }

    @Override
    public Boolean getValue() {
        return GetSwitch();
    }

}
