package dev.zprestige.fire.settings.impl;

import dev.zprestige.fire.settings.Setting;

import java.util.function.Predicate;

public class ComboBox extends Setting<String> {
    protected String value;
    protected String[] values;

    public ComboBox(final String name, final String value, final String[] values) {
        super(name, value);
        this.name = name;
        this.value = value;
        this.values = values;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String GetCombo() {
        return value;
    }

    public String[] getValues() {
        return values;
    }

    public ComboBox visibility(final Predicate<String> visibility) {
        shown = visibility;
        return this;
    }

    public ComboBox panel(final String panel) {
        this.panel = panel;
        return this;
    }

    @Override
    public String getValue() {
        return GetCombo();
    }
}
