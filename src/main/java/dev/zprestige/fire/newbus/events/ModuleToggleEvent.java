package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;

public class ModuleToggleEvent extends Event {
    protected final Module module;

    public ModuleToggleEvent(final Module module){
        super(Stage.None, false);
        this.module = module;
    }

    public static class Enable extends ModuleToggleEvent {

        public Enable(final Module module) {
            super(module);
        }
    }

    public static class Disable extends ModuleToggleEvent {
        public Disable(final Module module) {
            super(module);
        }
    }

    public Module getModule() {
        return module;
    }
}