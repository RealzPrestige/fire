package dev.zprestige.fire.manager.tickmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import net.minecraft.client.Minecraft;

public class TickManager {
    protected final Minecraft mc = Main.mc;
    protected float timer = 1.0f;

    public TickManager() {
        Main.newBus.registerListeners(new EventListener[]{
                new TickListener()
        });
    }


    public void setTimer(float timer) {
        this.timer = timer;
    }

    public void syncTimer() {
        this.timer = 1.0f;
        mc.timer.tickLength = 50.0f;
    }
}
