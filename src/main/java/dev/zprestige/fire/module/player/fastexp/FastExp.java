package dev.zprestige.fire.module.player.fastexp;

import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Key;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import org.lwjgl.input.Keyboard;

@Descriptor(description = "Speeds up throwing Exp")
public class FastExp extends Module {
    public final ComboBox mode = Menu.ComboBox("Mode", "Packet", new String[]{
            "Vanilla",
            "Packet"
    });
    public final ComboBox activateMode = Menu.ComboBox("Activate Mode", "RightClick", new String[]{
            "RightClick",
            "MiddleClick",
            "Custom"
    }).visibility(z -> mode.GetCombo().equals("Packet"));
    public final Key customKey = Menu.Key("Custom Key", Keyboard.KEY_NONE).visibility(z -> mode.GetCombo().equals("Packet") && activateMode.GetCombo().equals("Custom"));
    public final Slider packets = Menu.Slider("Packets", 2, 1, 10).visibility(z -> mode.GetCombo().equals("Packet"));
    public final Switch handOnly = Menu.Switch("Hand Only", false).visibility(z -> mode.GetCombo().equals("Packet"));

    public FastExp() {
        eventListeners = new EventListener[]{
                new TickListener(this)
        };
    }
}