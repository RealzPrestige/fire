package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import dev.zprestige.fire.module.Module;

public class ModuleToggleEvent extends Event {
    protected final Module module;

    public ModuleToggleEvent(Module module){
        this.module = module;
    }

    public static class Enable extends ModuleToggleEvent {

        public Enable(Module module) {
            super(module);
        }
    }

    public static class Disable extends ModuleToggleEvent {
        public Disable(Module module) {
            super(module);
        }
    }

    public Module getModule() {
        return module;
    }
}

