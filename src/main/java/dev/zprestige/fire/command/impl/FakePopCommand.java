package dev.zprestige.fire.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.command.Command;

public class FakePopCommand extends Command {

    public FakePopCommand() {
        super("fakepop", "FakePop <Player> <Amount>");
    }

    @Override
    public void listener(final String string) {
        try {
            final String[] split = string.split(" ");
            Main.chatManager.sendMessage(ChatFormatting.WHITE + "" + ChatFormatting.BOLD + split[1] + ChatFormatting.WHITE + " has popped " + Main.chatManager.prefixColor + split[2] + ChatFormatting.WHITE + (Integer.parseInt(split[2]) == 1 ? " totem." : " totems."));
        } catch (Exception ignored) {
            throwException(format);
        }
    }
}
