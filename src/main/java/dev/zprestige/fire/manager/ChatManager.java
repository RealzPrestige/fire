package dev.zprestige.fire.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatManager {
    protected final Minecraft mc = Main.mc;
    public ChatFormatting prefixColor = ChatFormatting.GOLD;
    public String prefix = prefixColor + "[Fire] " + ChatFormatting.GRAY;

    public void sendRawMessage(String message) {
        if (mc.player != null) {
            mc.player.sendMessage(new TextComponentString(message));
        }
    }

    public void sendMessage(String message) {
        sendRawMessage(prefix + message);
    }

    public void sendRemovableMessage(String message, int id) {
        mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(prefix + message), id);
    }
}