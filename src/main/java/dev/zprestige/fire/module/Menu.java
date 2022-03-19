package dev.zprestige.fire.module;


import dev.zprestige.fire.settings.Setting;
import dev.zprestige.fire.settings.impl.*;

import java.awt.*;

public class Menu {
    protected final Module module;
    public Menu(Module module) {
        this.module = module;
    }

    protected void addSetting(Setting setting) {
        module.settings.add(setting);
    }

    protected void setModule(Setting setting) {
        setting.setModule(module);
    }

    public ColorBox Color(String name, Color value) {
        ColorBox setting = new ColorBox(name, value);
        setModule(setting);
        addSetting(setting);
        return setting;
    }
    public ComboBox ComboBox(String name, String value, String[] values) {
        ComboBox setting = new ComboBox(name, value, values);
        setModule(setting);
        addSetting(setting);
        return setting;
    }
    public Key Key(String name, int key) {
        Key setting = new Key(name, key);
        setModule(setting);
        addSetting(setting);
        return setting;
    }
    public Slider Slider(String name, int value, int min, int max) {
        Slider setting = new Slider(name, value, min, max);
        setModule(setting);
        addSetting(setting);
        return setting;
    }
    public Slider Slider(String name, float value, float min, float max) {
        Slider setting = new Slider(name, value, min, max);
        setModule(setting);
        addSetting(setting);
        return setting;
    }
    public Switch Switch(String name,boolean value) {
        Switch setting = new Switch(name, value);
        setModule(setting);
        addSetting(setting);
        return setting;
    }
}