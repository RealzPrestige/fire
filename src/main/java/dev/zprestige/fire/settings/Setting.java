package dev.zprestige.fire.settings;

import dev.zprestige.fire.module.Module;

public class Setting {
    protected Module module;
    protected String name;
    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }
}
