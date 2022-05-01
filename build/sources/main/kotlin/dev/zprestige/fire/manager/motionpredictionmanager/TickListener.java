package dev.zprestige.fire.manager.motionpredictionmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TickListener extends EventListener<TickEvent, Object> {

    public TickListener() {
        super(TickEvent.class);
    }

    @Override
    public void invoke(final Object object) {
        Main.motionPredictionManager.players = mc.world.playerEntities.stream().map(MotionPredictionManager.Player::new).collect(Collectors.toCollection(ArrayList::new));
    }
}
