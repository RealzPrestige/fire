package dev.zprestige.fire.manager;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import dev.zprestige.fire.Main;
import net.minecraft.client.Minecraft;

public class DiscordRPCManager {
    protected final Minecraft mc = Main.mc;
    protected final DiscordRichPresence presence = new DiscordRichPresence();
    protected final DiscordRPC rpc = DiscordRPC.INSTANCE;
    protected Thread thread;

    @SuppressWarnings("BusyWait")
    public void start() {
        rpc.Discord_Initialize("954802818328457256", new DiscordEventHandlers(), true, "");
        presence.startTimestamp = System.currentTimeMillis() / 1000L;
        presence.largeImageKey = Main.modid;
        presence.largeImageText = Main.name + " " + Main.version;
        rpc.Discord_UpdatePresence(presence);
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                presence.state = (mc.isSingleplayer() ? "Playing singleplayer." : mc.getCurrentServerData() != null ? "Playing on " + mc.getCurrentServerData().serverIP + "." : "In the menus.");
                rpc.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException ignored) {
                }
            }
        }, "RPC-Callback-Handler");
        thread.start();
    }

    public void stop() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }
}
