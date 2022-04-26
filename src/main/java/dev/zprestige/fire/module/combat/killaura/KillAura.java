package dev.zprestige.fire.module.combat.killaura;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.EntityUtil;
import dev.zprestige.fire.util.impl.Timer;

import java.util.Arrays;

@Descriptor(description = "Attacks players")
public class KillAura extends Module {
    public final ComboBox targetPriority = Menu.ComboBox("Target Priority", "Range", new String[]{
            "Range",
            "UnSafe",
            "Health",
            "Fov",
    });
    public final Slider range = Menu.Slider("Range", 5.0f, 0.1f, 6.0f);
    public final Slider wallRange = Menu.Slider("Wall Range", 4.0f, 0.1f, 6.0f);
    public final Switch delay = Menu.Switch("Delay", true);
    public final Switch packet = Menu.Switch("Packet", true);
    public final Switch rotate = Menu.Switch("Rotate", false);
    public final Switch swordOnly = Menu.Switch("Sword Only", true);
    public final Switch autoSwitch = Menu.Switch("Auto Switch", false);
    protected final Timer timer = new Timer();

    public KillAura(){
        eventListeners= new EventListener[]{
                new MotionUpdateListener(this)
        };
    }

    protected EntityUtil.TargetPriority targetPriority(final String string) {
        return Arrays.stream(EntityUtil.TargetPriority.values()).filter(targetPriority1 -> targetPriority1.toString().equals(string)).findFirst().orElse(null);
    }
}
