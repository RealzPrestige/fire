package dev.zprestige.fire.ui.hudeditor.components.impl.fps;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;
import net.minecraft.client.Minecraft;

public class TickListener extends EventListener<TickEvent, Fps> {

    public TickListener(final Fps fps){
        super(TickEvent.class, fps);
    }

    @Override
    public void invoke(final Object object){
        if (module.timer.getTime(1000)) {
            module.timer.syncTime();
            if (module.avarageFrames.length - 1 >= 0)
                System.arraycopy(module.avarageFrames, 1, module.avarageFrames, 0, module.avarageFrames.length - 1);
            module.avarageFrames[module.avarageFrames.length - 1] = Minecraft.getDebugFPS();
        }
    }
}
