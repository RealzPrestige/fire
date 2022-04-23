package dev.zprestige.fire.manager.rotationmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.FrameEvent;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, Object> {

    public Frame3DListener(){
        super(FrameEvent.FrameEvent3D.class);
    }

    @Override
    public void invoke(final Object object){
        final long time = System.currentTimeMillis();
        Main.rotationManager.rotationsPerTick.removeIf(l -> l < time);
    }
}

