package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.ConnectionEvent;
import dev.zprestige.fire.events.impl.DeathEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager extends RegisteredClass {
    protected final Minecraft mc = Main.mc;
    protected ExecutorService executorService = Executors.newFixedThreadPool(2);

    public void run(Runnable command) {
        try {
            executorService.execute(command);
        } catch (Exception ignored) {
        }
    }

    @RegisterListener
    public void onDisconnect(ConnectionEvent.Disconnect event) {
        reset();
    }

    @RegisterListener
    public void onDeath(DeathEvent event) {
        if (event.getEntity().equals(mc.player)) {
            reset();
        }
    }

    public void reset() {
        this.executorService = Executors.newFixedThreadPool(2);
    }
}