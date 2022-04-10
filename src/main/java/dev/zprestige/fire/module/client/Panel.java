package dev.zprestige.fire.module.client;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;

import java.awt.*;

@Descriptor(description = "Opens Fire's csgo like clickgui")
public class Panel extends Module {
    public final ColorBox color = Menu.Color("Color", new Color(0x501BE5));

    @RegisterListener
    public void onTick(final TickEvent event){
        if (!(mc.currentScreen instanceof PanelScreen)){
            disableModule();
        }
    }

    @Override
    public void onEnable(){
        mc.displayGuiScreen(new PanelScreen());
    }

    @Override
    public void onDisable(){
        mc.displayGuiScreen(null);
    }
}
