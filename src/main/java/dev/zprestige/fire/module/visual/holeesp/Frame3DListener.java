package dev.zprestige.fire.module.visual.holeesp;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.FrameEvent;

import java.util.ArrayList;
import java.util.Objects;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, HoleESP> {

    public Frame3DListener(final HoleESP holeESP) {
        super(FrameEvent.FrameEvent3D.class, holeESP);
    }

    @Override
    public void invoke(final Object object) {
        module.camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        Main.holeManager.getHoles().stream().filter(holePos -> !module.contains(holePos.getPos())).forEach(holePos -> module.animatingHoles.add(new HoleESP.AnimatingHole(holePos, module)));
        new ArrayList<>(module.animatingHoles).forEach(animatingHole -> {
            if (!module.holeManagerContains(animatingHole.pos.getPos())) {
                animatingHole.remove = true;
            }
            if (animatingHole.canRemove()) {
                module.animatingHoles.remove(animatingHole);
            } else {
                animatingHole.update();
            }
        });
    }
}
