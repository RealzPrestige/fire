package dev.zprestige.fire.module.visual.viewtweaks;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.FogEvent;

public class FogListener extends EventListener<FogEvent, ViewTweaks> {

    public FogListener(final ViewTweaks viewTweaks){
        super(FogEvent.class, viewTweaks);
    }

    @Override
    public void invoke(final Object object){
        final FogEvent event = (FogEvent) object;
        event.getFogColors().setRed(module.fogColor.GetColor().getRed() / 255.0f);
        event.getFogColors().setGreen(module.fogColor.GetColor().getGreen() / 255.0f);
        event.getFogColors().setBlue(module.fogColor.GetColor().getBlue() / 255.0f);
    }
}
