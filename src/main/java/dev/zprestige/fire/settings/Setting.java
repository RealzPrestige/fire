package dev.zprestige.fire.settings;

import dev.zprestige.fire.module.Module;

import java.util.function.Predicate;

public class Setting<T> {
    protected Module module;
    protected String name;
    protected T value;
    protected Predicate<T> shown;

    public Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public boolean isVisible() {
        if (shown == null)
            return true;

        return shown.test(getValue());
    }
}
