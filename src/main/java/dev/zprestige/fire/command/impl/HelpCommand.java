package dev.zprestige.fire.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.command.Command;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Help");
    }

    @Override
    public void listener(final String string) {
        try {
            Main.commandManager.getCommands().forEach(command -> Main.chatManager.sendMessage(ChatFormatting.GRAY + "\u2022 " + ChatFormatting.WHITE + command.getFormat()));
        } catch (Exception ignored) {
            throwException(format);
        }
    }
}