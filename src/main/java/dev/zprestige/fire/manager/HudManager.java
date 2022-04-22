package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.ui.hudeditor.components.HudComponent;
import dev.zprestige.fire.util.impl.ClassFinder;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class HudManager extends RegisteredClass {
    protected final Minecraft mc = Main.mc;
    protected ArrayList<HudComponent> hudComponents;

    public HudManager() {
        super(true, false);
        hudComponents = ClassFinder.hudComponentArrayList();
    }
    @RegisterListener
    public void onFrame2D(FrameEvent.FrameEvent2D event) {
        if (Main.listener.checkNull()) {
            hudComponents.stream().filter(HudComponent::isEnabled).forEach(HudComponent::render);
        }
    }

    public ArrayList<HudComponent> getHudComponents() {
        return hudComponents;
    }

}
