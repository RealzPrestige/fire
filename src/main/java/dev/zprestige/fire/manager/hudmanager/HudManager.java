package dev.zprestige.fire.manager.hudmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.ClassFinder;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class HudManager {
    protected final Minecraft mc = Main.mc;
    protected ArrayList<HudComponent> hudComponents;

    public HudManager() {
        hudComponents = ClassFinder.hudComponentArrayList();
        Main.eventBus.registerListeners(new EventListener[]{
                new Frame2DListener()
        });
    }

    public ArrayList<HudComponent> getHudComponents() {
        return hudComponents;
    }

}
