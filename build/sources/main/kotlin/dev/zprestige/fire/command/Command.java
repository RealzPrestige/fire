package dev.zprestige.fire.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import net.minecraft.client.Minecraft;

public class Command {
    protected final Minecraft mc = Main.mc;
    protected String text;
    protected String format;

    public Command(String text, String format) {
        this.text = text;
        this.format = format;
    }

    public String getText() {
        return text;
    }

    public String getFormat() {
        return format;
    }

    public void listener(String string) {
    }

    public void completeMessage(String format) {
        Main.chatManager.sendMessage(ChatFormatting.GREEN + "Successfully " + format + ".");
    }

    public void throwException(String format) {
        Main.chatManager.sendMessage(ChatFormatting.RED + "Invalid command, try " + format + ".");
    }
}
