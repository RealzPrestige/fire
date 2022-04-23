package dev.zprestige.fire.manager.fademanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.FrameEvent;

import java.util.HashMap;
import java.util.Map;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, Object> {

    public Frame3DListener() {
        super(FrameEvent.FrameEvent3D.class);
    }

    @Override
    public void invoke(final Object object) {
        final HashMap<FadeManager.FadePosition, Float> fadePositions1 = new HashMap<>(Main.fadeManager.fadePositions);
        for (Map.Entry<FadeManager.FadePosition, Float> entry : fadePositions1.entrySet()) {
            final FadeManager.FadePosition fadePosition = entry.getKey();
            final float alpha = entry.getValue();
            final float newAlpha = alpha - (alpha / (102.0f - fadePosition.speed));
            if (entry.getValue() <= 10) {
                Main.fadeManager.fadePositions.remove(fadePosition);
                continue;
            }
            Main.fadeManager.fadePositions.put(fadePosition, newAlpha);
            fadePosition.render(newAlpha);
        }
    }
}
