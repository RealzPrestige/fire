package dev.zprestige.fire.module.client.clickgui;


import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.ui.menu.dropdown.MenuScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@Descriptor(description = "Opens Fire's clickgui")
public class ClickGui extends Module {
    public static ClickGui Instance;
    public final ColorBox color = Menu.Color("Color", new Color(0xD79A5D));
    public final ColorBox backgroundColor = Menu.Color("Background Color", new Color(0xB3362F2F));
    public final Slider animationSpeed = Menu.Slider("AnimationSpeed", 1.0f, 1.0f, 25.0f);
    public final Switch blur = Menu.Switch("Blur", true);

    public ClickGui() {
        setKeybind(Keyboard.KEY_RSHIFT);
        Instance = this;
        eventListeners = new EventListener[]{
                new TickListener(this)
        };
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new MenuScreen());
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen instanceof MenuScreen) {
            mc.currentScreen = null;
        }
    }
}
