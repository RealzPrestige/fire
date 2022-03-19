package dev.zprestige.fire;

import dev.zprestige.fire.events.Listener;
import dev.zprestige.fire.events.eventbus.EventBus;
import dev.zprestige.fire.manager.*;
import dev.zprestige.fire.manager.HudManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.opengl.Display;

@Mod(modid = Main.modid, name = Main.name, version = Main.version)
public class Main {
    public static final String modid = "fire";
    public static final String name = "Fire";
    public static final String version = "0.1";
    public static Minecraft mc;
    public static EventBus eventBus;
    public static ThreadManager threadManager;
    public static Listener listener;
    public static ModuleManager moduleManager;
    public static FadeManager fadeManager;
    public static ChatManager chatManager;
    public static FontManager fontManager;
    public static FileManager fileManager;
    public static FriendManager friendManager;
    public static ConfigManager configManager;
    public static PlayerManager playerManager;
    public static HudManager hudManager;
    public static DiscordRPCManager discordRPCManager;
    @Mod.EventHandler
    public void init(FMLInitializationEvent ignoredEvent) {
        Display.setTitle(name + " " + version);
        mc = Minecraft.getMinecraft();
        eventBus = new EventBus();
        threadManager = new ThreadManager();
        listener = (Listener) new Listener().registerForge().registerEventBus();
        moduleManager = new ModuleManager().init();
        fadeManager = (FadeManager) new FadeManager().registerEventBus();
        chatManager = new ChatManager();
        fontManager = new FontManager();
        fileManager = new FileManager();
        friendManager = new FriendManager();
        configManager = new ConfigManager().loadActiveConfig().loadSavedFriends();
        playerManager = (PlayerManager) new PlayerManager().registerEventBus();
        hudManager = (HudManager) new HudManager().init().registerEventBus();
        discordRPCManager = new DiscordRPCManager();
    }
}

