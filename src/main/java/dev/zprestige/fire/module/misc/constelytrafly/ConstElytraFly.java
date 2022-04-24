package dev.zprestige.fire.module.misc.constelytrafly;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.util.impl.Timer;

@Descriptor(description = "Assists elytraflying on const because its aids")
public class ConstElytraFly extends Module {
    public final Slider offGroundTakeOff = Menu.Slider("Off Ground Take Off", 2000.0f, 0.1f, 10000.0f);
    protected final Timer offGroundTimer = new Timer(), jumpTimer = new Timer();

    public ConstElytraFly(){
        eventListeners = new EventListener[]{
                new PacketReceiveListener(this),
                new TickListener(this)
        };
    }

    @Override
    public void onDisable(){
        Main.tickManager.syncTimer();
    }
}
