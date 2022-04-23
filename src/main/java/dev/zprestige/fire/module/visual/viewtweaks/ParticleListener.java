package dev.zprestige.fire.module.visual.viewtweaks;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.ParticleEvent;

public class ParticleListener extends EventListener<ParticleEvent, ViewTweaks> {

    public ParticleListener(final ViewTweaks viewTweaks){
        super(ParticleEvent.class, viewTweaks);
    }

    @Override
    public void invoke(final Object object){
        final ParticleEvent event = (ParticleEvent) object;
        if (module.removeParticles.GetSwitch()) {
            event.setCancelled();
        }
    }
}
