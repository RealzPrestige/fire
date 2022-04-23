package dev.zprestige.fire.manager.commandmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.command.Command;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.util.impl.ClassFinder;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class CommandManager {
    protected ArrayList<Command> commands;
    protected final Minecraft mc = Main.mc;
    protected String prefix = ".";

    public CommandManager() {
        commands = ClassFinder.commandArrayList();
        Main.newBus.registerListeners(new EventListener[]{
                new PacketSendListener()
        });
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
