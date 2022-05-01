package dev.zprestige.fire.manager.shrinkmanager;

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
        final HashMap<ShrinkManager.ShrinkPosition, Float> shrinkPositions1 = new HashMap<>(Main.shrinkManager.shrinkPositions);
        for (Map.Entry<ShrinkManager.ShrinkPosition, Float> entry : shrinkPositions1.entrySet()) {
            final ShrinkManager.ShrinkPosition shrinkPosition = entry.getKey();
            final float shrink = entry.getValue();
            final float newShrink = shrink + (shrink / (102.0f - shrinkPosition.speed));
            if (entry.getValue() >= 0.4) {
                Main.shrinkManager.shrinkPositions.remove(shrinkPosition);
                continue;
            }
            Main.shrinkManager.shrinkPositions.put(shrinkPosition, newShrink);
            shrinkPosition.render(newShrink);
        }
    }
}
