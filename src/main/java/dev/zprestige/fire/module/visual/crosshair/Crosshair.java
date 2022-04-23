package dev.zprestige.fire.module.visual.crosshair;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;

@Descriptor(description = "Renders a crosshair on the screen")
public class Crosshair extends Module {
    public final Slider gap = Menu.Slider("Gap", 1.0f, 0.1f, 10.0f);
    public final Slider length = Menu.Slider("Length", 1.0f, 0.3f, 10.0f);
    public final Slider thickness = Menu.Slider("Thickness", 1.0f, 0.3f, 10.0f);
    public final ColorBox color = Menu.Color("Color", Color.WHITE);

    public Crosshair() {
        eventListeners = new EventListener[]{
                new Frame2DListener(this),
                new RenderOverlayListener(this)
        };
    }
}
