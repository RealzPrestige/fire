package dev.zprestige.fire.module.visual.blockhighlight;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;

import java.awt.*;

@Descriptor(description = "Renders on the block hovered with mouse")
public class BlockHighlight extends Module {
    public final Slider fadeSpeed = Menu.Slider("Fade Speed", 25.0f, 0.1f, 100.0f);
    public final Switch box = Menu.Switch("Box", false);
    public final ColorBox boxColor = Menu.Color("Box Color", new Color(255, 255, 255, 120)).visibility(z -> box.GetSwitch());
    public final Switch outline = Menu.Switch("Outline", false);
    public final ColorBox outlineColor = Menu.Color("Outline Color", new Color(255, 255, 255, 255)).visibility(z -> outline.GetSwitch());
    public final Slider outlineWidth = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f).visibility(z -> outline.GetSwitch());

    public BlockHighlight() {
        eventListeners = new EventListener[]{
                new Frame3DListener(this)
        };
    }
}
