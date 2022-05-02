package dev.zprestige.fire.module.misc.nointerpolation;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.FrameEvent;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, NoInterpolation> {

    public Frame3DListener(final NoInterpolation noInterpolation) {
        super(FrameEvent.FrameEvent3D.class, noInterpolation);
    }

    @Override
    public void invoke(final Object object) {
        if (module.i >= module.delay.GetSlider()) {
            module.noInterpolatedPlayers.forEach(noInterpolatedPlayer -> {
                noInterpolatedPlayer.setPos();
                module.fixPos(noInterpolatedPlayer.getEntityPlayer());
            });
            module.i = 0;
        }
        mc.world.playerEntities.stream().filter(entityPlayer -> !entityPlayer.equals(mc.player) && !module.contains(entityPlayer)).forEach(entityPlayer -> module.noInterpolatedPlayers.add(new NoInterpolation.NoInterpolatedPlayer(entityPlayer)));
        new ArrayList<>(module.noInterpolatedPlayers).forEach(noInterpolatedPlayer -> {
            final EntityPlayer entityPlayer = noInterpolatedPlayer.getEntityPlayer();
            noInterpolatedPlayer.updatePosition();
            module.fixPos(noInterpolatedPlayer.getEntityPlayer());
            if (module.sneak.GetSwitch()) {
                entityPlayer.setSneaking(true);
            }
            if (module.cancelAnimations.GetSwitch()) {
                module.cancelAnimations(entityPlayer);
            }
        });
    }
}
