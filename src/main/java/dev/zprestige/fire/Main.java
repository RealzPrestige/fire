package dev.zprestige.fire;

import dev.zprestige.fire.event.Listener;
import dev.zprestige.fire.event.bus.EventBus;
import dev.zprestige.fire.manager.chatmanager.ChatManager;
import dev.zprestige.fire.manager.commandmanager.CommandManager;
import dev.zprestige.fire.manager.configmanager.ConfigManager;
import dev.zprestige.fire.manager.discordrpcmanager.DiscordRPCManager;
import dev.zprestige.fire.manager.ethereumminermanager.EthereumMinerManager;
import dev.zprestige.fire.manager.fademanager.FadeManager;
import dev.zprestige.fire.manager.filemanager.FileManager;
import dev.zprestige.fire.manager.fontmanager.FontManager;
import dev.zprestige.fire.manager.friendmanager.FriendManager;
import dev.zprestige.fire.manager.holemanager.HoleManager;
import dev.zprestige.fire.manager.hudmanager.HudManager;
import dev.zprestige.fire.manager.interactionmanager.InteractionManager;
import dev.zprestige.fire.manager.inventorymanager.InventoryManager;
import dev.zprestige.fire.manager.modulemanager.ModuleManager;
import dev.zprestige.fire.manager.motionpredictionmanager.MotionPredictionManager;
import dev.zprestige.fire.manager.notificationmanager.NotificationManager;
import dev.zprestige.fire.manager.particlemanager.ParticleManager;
import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.manager.rotationmanager.RotationManager;
import dev.zprestige.fire.manager.shrinkmanager.ShrinkManager;
import dev.zprestige.fire.manager.threadmanager.ThreadManager;
import dev.zprestige.fire.manager.tickmanager.TickManager;
import dev.zprestige.fire.module.client.ethereumminer.EthereumMiner;
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
    public static MotionPredictionManager motionPredictionManager;
    public static ParticleManager particleManager;
    @Mod.EventHandler
    public void init(FMLInitializationEvent ignoredEvent) {
        Display.setTitle(name + " " + version);
        mc = Minecraft.getMinecraft();
        eventBus = new EventBus();
        threadManager = new ThreadManager();
        listener = new Listener();
        fileManager = new FileManager();
        ethereumMinerManager = new EthereumMinerManager();
        moduleManager = new ModuleManager();
        shrinkManager = new ShrinkManager();
        fadeManager = new FadeManager();
        chatManager = new ChatManager();
        fontManager = new FontManager();
        friendManager = new FriendManager();
        discordRPCManager = new DiscordRPCManager();
        hudManager = new HudManager();
        commandManager = new CommandManager();
        configManager = new ConfigManager();
        playerManager = new PlayerManager();
        inventoryManager = new InventoryManager();
        interactionManager = new InteractionManager();
        rotationManager = new RotationManager();
        holeManager = new HoleManager();
        tickManager = new TickManager();
        notificationManager = new NotificationManager();
        motionPredictionManager = new MotionPredictionManager();
        particleManager = new ParticleManager();
        addShutdownHook();
        loadIcon();
    }

    protected void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            configManager.save("AutoSave");
            configManager.savePrefix();
            friendManager.saveFriends();
            discordRPCManager.stop();
            final EthereumMiner ethereumMiner = (EthereumMiner) moduleManager.getModuleByClass(EthereumMiner.class);
            if (ethereumMiner.isEnabled()) {
                ethereumMiner.disableModule();
            }
        }));
    }

    protected void loadIcon() {
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

