package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import net.minecraft.client.Minecraft;

public class TickManager extends RegisteredClass {
    protected final Minecraft mc = Main.mc;
    protected float timer = 1.0f;

    public TickManager(){
        super(true, false);
    }

    @RegisterListener
    public void onTick(final TickEvent event){
        if (timer != 1.0f) {
            mc.timer.tickLength = 50.0f / timer;
        }
    }


    public void setTimer(float timer) {
        this.timer = timer;
    }

    public void syncTimer(){
        this.timer = 1.0f;
        mc.timer.tickLength = 50.0f;
    }
}
