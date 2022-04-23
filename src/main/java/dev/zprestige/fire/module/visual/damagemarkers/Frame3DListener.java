package dev.zprestige.fire.module.visual.damagemarkers;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.FrameEvent;

import java.util.ArrayList;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, DamageMarkers> {

    public Frame3DListener(final DamageMarkers damageMarkers){
        super(FrameEvent.FrameEvent3D.class, damageMarkers);
    }

    @Override
    public void invoke(final Object object){
        for (final DamageMarkers.DamageMarker marker : new ArrayList<>(module.damageMarkers)) {
            if (marker.getAlpha() < 30.0) {
                module.damageMarkers.remove(marker);
                continue;
            }
            marker.setAlpha(marker.getAlpha() - (marker.getAlpha() / (101.0f - module.fadeSpeed.GetSlider())));
            marker.setY(marker.getY() + 0.005);
            marker.render();
        }
    }
}
