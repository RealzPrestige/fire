package dev.zprestige.fire.module.client.holemanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.FrameEvent;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, HoleManager> {

    public Frame3DListener(final HoleManager holeManager) {
        super(FrameEvent.FrameEvent3D.class, holeManager);
    }

    @Override
    public void invoke(final Object object) {
        if (module.lastRange != module.range.GetSlider()) {
            Main.holeManager.getHoles().clear();
        }
        Main.holeManager.loadHoles(module.range.GetSlider());
        module.lastRange = module.range.GetSlider();
    }
}
