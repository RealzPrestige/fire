package dev.zprestige.fire.module.misc.autorespawn;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import net.minecraft.client.gui.GuiGameOver;

public class TickListener extends EventListener<TickEvent, AutoRespawn> {

    public TickListener(final AutoRespawn autoRespawn){
        super(TickEvent.class, autoRespawn);
    }

    @Override
    public void invoke(final Object object){
        if (mc.currentScreen instanceof GuiGameOver){
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
        }
    }
}
