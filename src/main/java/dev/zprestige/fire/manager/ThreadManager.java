package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import net.minecraft.client.Minecraft;

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
}