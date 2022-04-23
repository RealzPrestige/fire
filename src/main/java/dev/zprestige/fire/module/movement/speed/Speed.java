package dev.zprestige.fire.module.movement.speed;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Key;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

@Descriptor(description = "Speeds up and controls moving")
public class Speed extends Module {
    public final ComboBox speedMode = Menu.ComboBox("Speed Mode", "Strafe", new String[]{"OnGround", "Strafe"});
    public final ComboBox dataMode = Menu.ComboBox("Data Mode", "Mode", new String[]{"Mode", "Factor"});
    public final Key switchKey = Menu.Key("Switch Key", Keyboard.KEY_NONE);
    public final Switch strict = Menu.Switch("Strict", false);
    public final Switch liquids = Menu.Switch("Liquids", false);
    public final Switch useTimer = Menu.Switch("Use Timer", false);
    public final Slider timerAmount = Menu.Slider("Timer Amount", 1.0f, 0.9f, 2.0f).visibility(z -> useTimer.GetSwitch());
    public final Switch velocityBoost = Menu.Switch("Velocity Boost", false);
    public final Slider boostAmplifier = Menu.Slider("Velocity Boost Amplifier", 10.0f, 1.0f, 20.0f).visibility(z -> velocityBoost.GetSwitch());
    public final Slider strafeFactor = Menu.Slider("Strafe Factor", 1.0f, 0.1f, 3.0f).visibility(z -> !strict.GetSwitch());
    protected double previousDistance, motionSpeed;
    protected float lastHealth;
    protected int currentState = 1;
    protected HashMap<Long, Float> damageMap = new HashMap<>();

    public Speed() {
        eventListeners = new EventListener[]{
                new KeyListener(this),
                new MoveListener(this),
                new TickListener(this)
        };
    }

    @Override
    public void onDisable() {
        Main.tickManager.syncTimer();
    }

    protected void sendSwitchMessage() {
        Main.notificationManager.addNotifications("Speed mode switched to " + speedMode.GetCombo() + ".");
        Main.chatManager.sendRemovableMessage(ChatFormatting.WHITE + "" + ChatFormatting.BOLD + "Speed" + ChatFormatting.RESET + " mode switched to " + Main.chatManager.prefixColor + speedMode.GetCombo() + ChatFormatting.RESET + ".", 1);
    }

    @Override
    public String getData() {
        switch (dataMode.GetCombo()) {
            case "Factor":
                return strafeFactor.GetSlider() + "";
            case "Mode":
                return speedMode.GetCombo();
        }
        return "";
    }

}
