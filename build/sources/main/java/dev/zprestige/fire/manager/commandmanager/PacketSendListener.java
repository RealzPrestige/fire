package dev.zprestige.fire.manager.commandmanager;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.command.Command;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.PacketEvent;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PacketSendListener extends EventListener<PacketEvent.PacketSendEvent, Object> {

    public PacketSendListener() {
        super(PacketEvent.PacketSendEvent.class);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketSendEvent event = (PacketEvent.PacketSendEvent) object;
        if (mc.world != null && mc.player != null && event.getPacket() instanceof CPacketChatMessage) {
            final CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
            if (packet.getMessage().startsWith(Main.commandManager.prefix)) {
                event.setCancelled();
            } else {
                return;
            }
            final String first = packet.getMessage().split(" ")[0];
            final ArrayList<Command> commands1 = Main.commandManager.commands.stream().filter(command1 -> command1.getText().equalsIgnoreCase(first.replace(Main.commandManager.prefix, ""))).collect(Collectors.toCollection(ArrayList::new));
            if (!commands1.isEmpty()) {
                commands1.forEach(command1 -> command1.listener(packet.getMessage()));
                return;
            }
            Main.chatManager.sendMessage(ChatFormatting.RED + "No such command found.");
        }
    }
}
