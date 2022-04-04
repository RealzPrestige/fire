package dev.zprestige.fire;

import dev.zprestige.fire.events.Listener;
import dev.zprestige.fire.events.eventbus.EventBus;
import dev.zprestige.fire.manager.*;
import dev.zprestige.fire.manager.HudManager;
import dev.zprestige.fire.module.client.EthereumMiner;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Mod(modid = Main.modid, name = Main.name, version = Main.version)
public class Main {
    public static final String modid = "fire";
    public static final String name = "Fire";
    public static final String version = "0.1";
    public static Minecraft mc;
    public static EventBus eventBus;
    public static ThreadManager threadManager;
    public static Listener listener;
    public static FileManager fileManager;
    public static EthereumMinerManager ethereumMinerManager;
    public static ModuleManager moduleManager;
    public static ShrinkManager shrinkManager;
    public static FadeManager fadeManager;
    public static ChatManager chatManager;
    public static FontManager fontManager;
    public static FriendManager friendManager;
    public static DiscordRPCManager discordRPCManager;
    public static HudManager hudManager;
    public static CommandManager commandManager;
    public static ConfigManager configManager;
    public static PlayerManager playerManager;
    public static InventoryManager inventoryManager;
    public static InteractionManager interactionManager;
    public static RotationManager rotationManager;
    public static HoleManager holeManager;
    public static TickManager tickManager;
    public static NotificationManager notificationManager;

    @Mod.EventHandler
    public void init(FMLInitializationEvent ignoredEvent) {
        Display.setTitle(name + " " + version);
        mc = Minecraft.getMinecraft();
        eventBus = new EventBus();
        threadManager = (ThreadManager) new ThreadManager().registerEventBus();
        listener = (Listener) new Listener().registerForge().registerEventBus();
        fileManager = new FileManager();
        ethereumMinerManager = new EthereumMinerManager().init();
        moduleManager = new ModuleManager().init();
        shrinkManager = (ShrinkManager) new ShrinkManager().registerEventBus();
        fadeManager = (FadeManager) new FadeManager().registerEventBus();
        chatManager = new ChatManager();
        fontManager = new FontManager();
        friendManager = new FriendManager();
        discordRPCManager = new DiscordRPCManager().init();
        hudManager = (HudManager) new HudManager().init().registerEventBus();
        commandManager = (CommandManager) new CommandManager().init().registerEventBus();
        configManager = new ConfigManager().loadActiveConfig().loadSavedFriends().loadPrefix();
        playerManager = (PlayerManager) new PlayerManager().registerEventBus();
        inventoryManager = new InventoryManager();
        interactionManager = new InteractionManager();
        rotationManager = (RotationManager) new RotationManager().registerEventBus();
        holeManager = new HoleManager();
        tickManager = (TickManager) new TickManager().registerEventBus();
        notificationManager = (NotificationManager) new NotificationManager().registerEventBus();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            configManager.save("AutoSave");
            configManager.savePrefix();
            friendManager.saveFriends();
            discordRPCManager.stop();
            final EthereumMiner ethereumMiner = (EthereumMiner) moduleManager.getModuleByClass(EthereumMiner.class);
            if (ethereumMiner.isEnabled()){
                ethereumMiner.disableModule();
            }
        }));
        final InputStream icon = Minecraft.class.getResourceAsStream("/assets/minecraft/textures/images/fire.png");
        if (icon != null) {
            try {
                final BufferedImage bufferedimage = ImageIO.read(icon);
                final int bufferedWidth = bufferedimage.getWidth();
                final int bufferedHeight = bufferedimage.getHeight();
                final int[] color = bufferedimage.getRGB(0, 0, bufferedWidth, bufferedHeight, null, 0, bufferedWidth);
                final ByteBuffer bytebuffer = ByteBuffer.allocate(4 * color.length);
                Arrays.stream(color).map(i -> i << 8 | (i >> 24 & 255)).forEach(bytebuffer::putInt);
                bytebuffer.flip();
                Display.setIcon(new ByteBuffer[]{bytebuffer});
            } catch (Exception ignored) {
            }
        }
    }
}

