package dev.zprestige.fire;

import net.minecraftforge.common.MinecraftForge;

public class RegisteredClass {

    public RegisteredClass(final boolean eventBus, final boolean forge){
        if (eventBus){
            registerEventBus();
        }
        if (forge){
            registerForge();
        }
    }
    protected void registerEventBus(){
        Main.eventBus.register(this);
    }

    protected void registerForge(){
        MinecraftForge.EVENT_BUS.register(this);
    }
}
