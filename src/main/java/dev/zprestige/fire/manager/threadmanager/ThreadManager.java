package dev.zprestige.fire.manager.threadmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import net.minecraft.client.Minecraft;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    protected final Minecraft mc = Main.mc;
    protected ExecutorService executorService = Executors.newFixedThreadPool(2);

    public void run(final Runnable command) {
        executorService.execute(command);
    }
}