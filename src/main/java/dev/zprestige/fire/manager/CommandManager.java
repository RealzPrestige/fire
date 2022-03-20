package dev.zprestige.fire.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.command.Command;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.PacketEvent;
import dev.zprestige.fire.util.impl.ClassFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CommandManager extends RegisteredClass {
    protected ArrayList<Command> commands = new ArrayList<>();
    protected final Minecraft mc = Main.mc;
    protected String prefix = ".";

    public CommandManager init() {
        commands = ClassFinder.commandArrayList();
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @RegisterListener
    public void onPacketSend(PacketEvent.PacketSendEvent event) {
        if (mc.world == null || mc.player == null || !(event.getPacket() instanceof CPacketChatMessage))
            return;
        final CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
        if (packet.getMessage().startsWith(prefix)) {
            event.setCancelled(true);
        } else {
            return;
        }
        final String first = packet.getMessage().split(" ")[0];
        final ArrayList<Command> commands1 = commands.stream().filter(command1 -> command1.getText().equalsIgnoreCase(first.replace(prefix, ""))).collect(Collectors.toCollection(ArrayList::new));
        if (!commands1.isEmpty()) {
            commands1.forEach(command1 -> command1.listener(packet.getMessage()));
            return;
        }
        Main.chatManager.sendMessage(ChatFormatting.RED + "No such command found.");
    }
}
