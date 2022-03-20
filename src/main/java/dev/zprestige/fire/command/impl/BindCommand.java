package dev.zprestige.fire.command.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.command.Command;
import dev.zprestige.fire.module.Module;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("config", "Bind <Module> <Keybind>");
    }

    @Override
    public void listener(String string) {
        try {
            final String[] split = string.split(" ");
            for (Module module : Main.moduleManager.getModules()) {
                if (module.getName().equals(split[1])) {
                    final int keybind = Keyboard.getKeyIndex(split[2]);
                    module.setKeybind(keybind);
                    completeMessage("keyinded " + module.getName() + "'s keybind to " + keybind);
                }
            }
        } catch (Exception ignored) {
            throwException(format);
        }
    }
}