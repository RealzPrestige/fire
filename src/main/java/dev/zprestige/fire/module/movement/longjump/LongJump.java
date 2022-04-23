package dev.zprestige.fire.module.movement.longjump;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.PacketEvent;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

import java.util.Objects;

@Descriptor(description = "Allows you to jump further than usual")
public class LongJump extends Module {
    public final Slider factor = Menu.Slider("Factor", 5.0f, 0.1f, 20.0f);
    public final Slider accelerationFactor = Menu.Slider("Acceleration Factor", 2.0f, 0.1f, 10.0f);
    public final Slider verticalFactor = Menu.Slider("Vertical Factor", 4.0f, 0.1f, 6.0f);
    public final Switch useTimer = Menu.Switch("Use Timer", false);
    public final Slider timerAmount = Menu.Slider("Timer Amount", 1.0f, 0.9f, 2.0f).visibility(z -> useTimer.GetSwitch());
    public final Switch liquids = Menu.Switch("Liquids", false);
    public final Switch disableOnLag = Menu.Switch("Disable On Lag", false);
    protected double previousDistance, motionSpeed;
    protected int currentState = 1;

    public LongJump(){
        eventListeners = new EventListener[]{
                new MoveListener(this),
                new PacketReceiveListener(this),
                new TickListener(this)
        };
    }

    @Override
    public void onDisable() {
        Main.tickManager.syncTimer();
    }
}
