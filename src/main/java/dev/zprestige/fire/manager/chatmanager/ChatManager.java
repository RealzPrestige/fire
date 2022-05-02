package dev.zprestige.fire.manager.chatmanager;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatManager {
    protected final Minecraft mc = Main.mc;
    public ChatFormatting prefixColor = ChatFormatting.GOLD;
    public String prefix = "Fire";
    public String finalPrefix = prefixColor + "[" + prefix + "] " + ChatFormatting.GRAY;

    public void sendRawMessage(String message) {
        if (mc.player != null && mc.world != null) {
            mc.player.sendMessage(new TextComponentString(message));
        }
    }

    public void sendMessage(String message) {
        if (mc.player != null && mc.world != null) {
            sendRawMessage(finalPrefix + message);
        }
    }

    public void sendRemovableMessage(String message, int id) {
        if (mc.player != null && mc.world != null) {
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(finalPrefix + message), id);
        }
    }

    public void setPrefixColor(ChatFormatting prefixColor) {
        this.prefixColor = prefixColor;
        this.finalPrefix = prefixColor + "[" + prefix + "] " + ChatFormatting.GRAY;
    }

    public void setPrefix(final String prefix){
        this.prefix = prefix;
        this.finalPrefix = prefixColor + "[" + prefix + "] " + ChatFormatting.GRAY;
    }
}