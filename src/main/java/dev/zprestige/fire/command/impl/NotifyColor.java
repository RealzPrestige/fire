package dev.zprestige.fire.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.command.Command;

import java.util.Arrays;

public class NotifyColor extends Command {

    public NotifyColor() {
        super("notifycolor", "NotifyColor <Color>");
    }

    @Override
    public void listener(final String string) {
        try {
            final String[] split = string.split(" ");
            Arrays.stream(ChatFormatting.values()).filter(chatFormatting -> split[1].charAt(0) == chatFormatting.getChar()).forEach(chatFormatting -> {
                Main.chatManager.setPrefixColor(chatFormatting);
                completeMessage("set notify color to " + split[1]);
            });
        } catch (Exception ignored) {
            throwException(format);
        }
    }
}
