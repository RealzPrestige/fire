package dev.zprestige.fire.command.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.command.Command;
import dev.zprestige.fire.module.Module;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "Bind <Module> <Keybind>");
    }

    @Override
    public void listener(final String string) {
        try {
            final String[] split = string.split(" ");
            for (Module module : Main.moduleManager.getModules()) {
                if (module.getName().equalsIgnoreCase(split[1])) {
                    final int keybind = Keyboard.getKeyIndex(split[2].toUpperCase());
                    module.setKeybind(keybind);
                    completeMessage("keybinded " + module.getName() + "'s keybind to " + split[2].toUpperCase());
                }
            }
        } catch (Exception ignored) {
            throwException(format);
        }
    }
}