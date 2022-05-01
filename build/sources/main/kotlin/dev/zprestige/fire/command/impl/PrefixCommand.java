package dev.zprestige.fire.command.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.command.Command;

public class PrefixCommand extends Command {

    public PrefixCommand() {
        super("prefix", "Prefix <Prefix>");
    }

    @Override
    public void listener(String string) {
        try {
            final String[] split = string.split(" ");
            final String split1 = split[1];
            Main.commandManager.setPrefix(split1);
            completeMessage("set prefix to " + split1);
        } catch (Exception ignored) {
            throwException(format);
        }
    }
}
