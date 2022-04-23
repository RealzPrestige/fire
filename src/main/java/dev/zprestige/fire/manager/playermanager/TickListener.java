package dev.zprestige.fire.manager.playermanager;

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
        Main.threadManager.run(() -> {
            if (mc.player != null && mc.world != null) {
                Main.playerManager.players = mc.world.playerEntities.stream().filter(entityPlayer -> !entityPlayer.equals(mc.player)).map(PlayerManager.Player::new).collect(Collectors.toCollection(ArrayList::new));
            }
        });
    }
}
