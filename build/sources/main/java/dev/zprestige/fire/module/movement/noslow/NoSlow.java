package dev.zprestige.fire.module.movement.noslow;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;

@Descriptor(description = "Prevents slow down on items")
public class NoSlow extends Module {
    public final Switch items = Menu.Switch("Items", true);
    public final Switch ncpStrict = Menu.Switch("Ncp Strict", false).visibility(z -> items.GetSwitch());
    public final Switch webs = Menu.Switch("Webs", false);
    public final ComboBox mode = Menu.ComboBox("Mode", "Vanilla", new String[]{
            "Vanilla",
            "Ncp",
            "Fast",
            "Factor",
            "Timer"
    }).visibility(z -> webs.GetSwitch());
    public final Slider fastFactor = Menu.Slider("Fast Factor", 1.1f, 0.1f, 5.0f).visibility(z -> webs.GetSwitch() && mode.GetCombo().equals("Fast"));
    public final Slider factor = Menu.Slider("Factor", 1.0f, 0.1f, 5.0f).visibility(z -> webs.GetSwitch() && mode.GetCombo().equals("Factor"));
    public final Slider verticalFactor = Menu.Slider("Vertical Factor", 1.0f, 0.1f, 10.0f).visibility(z -> webs.GetSwitch() && mode.GetCombo().equals("Factor"));
    public final Slider timer = Menu.Slider("Timer", 1.0f, 0.1f, 20.0f).visibility(z -> webs.GetSwitch() && mode.GetCombo().equals("Timer"));
    protected boolean sneaked, timered;

    public NoSlow() {
        eventListeners = new EventListener[]{
                new EntityUseItemListener(this),
                new ItemInputUpdateListener(this),
                new MoveListener(this),
                new TickListener(this)
        };
    }

    protected boolean webbed(){
        return mc.player.isInWeb;
    }
    public boolean slowed() {
        return items.GetSwitch() && mc.player.isHandActive() && !mc.player.isRiding() && !mc.player.isElytraFlying();
    }
}
