package dev.zprestige.fire.module.misc.freelook;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.CameraSetupEvent;

public class CameraSetupListener extends EventListener<CameraSetupEvent, Freelook> {

    public CameraSetupListener(final Freelook freelook){
        super(CameraSetupEvent.class, freelook);
    }

    @Override
    public void invoke(final Object object){
        final CameraSetupEvent event = (CameraSetupEvent) object;
        event.setYaw(event.getYaw() + module.yaw);
        event.setPitch(event.getPitch() + module.pitch);
    }
}
