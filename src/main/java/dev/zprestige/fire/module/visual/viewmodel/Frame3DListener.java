package dev.zprestige.fire.module.visual.viewmodel;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.FrameEvent;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, ViewModel> {

    public Frame3DListener(final ViewModel viewModel) {
        super(FrameEvent.FrameEvent3D.class, viewModel);
    }

    @Override
    public void invoke(final Object object) {
        if (mc.player.ticksExisted >= 20 && module.removeSway.GetSwitch()) {
            mc.player.renderArmYaw -= (mc.player.rotationYaw - mc.player.renderArmYaw) * 0.5f;
            mc.player.renderArmPitch -= (mc.player.rotationPitch - mc.player.renderArmPitch) * 0.5f;
        }
    }
}
