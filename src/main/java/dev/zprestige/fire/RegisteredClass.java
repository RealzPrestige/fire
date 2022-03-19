package dev.zprestige.fire;

import net.minecraftforge.common.MinecraftForge;

public class RegisteredClass {

    protected RegisteredClass registerEventBus(){
        Main.eventBus.register(this);
        return this;
    }

    protected RegisteredClass registerForge(){
        MinecraftForge.EVENT_BUS.register(this);
        return this;
    }
}
