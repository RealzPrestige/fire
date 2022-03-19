package dev.zprestige.fire.settings.impl;

import dev.zprestige.fire.settings.Setting;

public class ComboBox extends Setting {
    protected String value;
    protected String[] values;

    public ComboBox(String name, String value, String[] values) {
        this.name = name;
        this.value = value;
        this.values = values;
    }

    public String getName() {
        return name;
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

}
