package dev.zprestige.fire.manager.chatmanager;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatManager {
    protected final Minecraft mc = Main.mc;
    public ChatFormatting prefixColor = ChatFormatting.GOLD;
    public String prefix = prefixColor + "[Fire] " + ChatFormatting.GRAY;

    public void sendRawMessage(String message) {
        if (mc.player != null && mc.world != null) {
            mc.player.sendMessage(new TextComponentString(message));
        }
    }

    public void sendMessage(String message) {
        if (mc.player != null && mc.world != null) {
            sendRawMessage(prefix + message);
        }
    }

    public void sendRemovableMessage(String message, int id) {
        if (mc.player != null && mc.world != null) {
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(prefix + message), id);
        }
    }
}