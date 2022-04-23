package dev.zprestige.fire.module.visual.viewtweaks;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.FogDensityEvent;

public class FogDensityListener extends EventListener<FogDensityEvent, ViewTweaks> {

    public FogDensityListener(final ViewTweaks viewTweaks) {
        super(FogDensityEvent.class, viewTweaks);
    }

    @Override
    public void invoke(final Object object) {
        final FogDensityEvent event = (FogDensityEvent) object;
        event.setDensity(module.fogDensity.GetSlider() / 1000.0f);
    }
}
