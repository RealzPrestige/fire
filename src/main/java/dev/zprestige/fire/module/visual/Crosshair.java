package dev.zprestige.fire.module.visual;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.RenderOverlayEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
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

    @RegisterListener
    public void onRenderOverlay(final RenderOverlayEvent event) {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.CROSSHAIRS)) {
            event.setCancelled(true);
        }
    }

    @RegisterListener
    public void onFrame2D(final FrameEvent.FrameEvent2D event) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        final float centerX = scaledResolution.getScaledWidth() / 2.0f, centerY = scaledResolution.getScaledHeight() / 2.0f;
        final float l = length.GetSlider();
        final float t = thickness.GetSlider();
        final float g = gap.GetSlider();
        final int c = color.GetColor().getRGB();
        RenderUtil.drawRect(new Vector2D(centerX - l - g, centerY - t), new Vector2D(centerX - g, centerY + t), c);
        RenderUtil.drawRect(new Vector2D(centerX + g, centerY - t), new Vector2D(centerX + g + l, centerY + t), c);
        RenderUtil.drawRect(new Vector2D(centerX - t, centerY - g - l), new Vector2D(centerX + t, centerY - g), c);
        RenderUtil.drawRect(new Vector2D(centerX - t, centerY + g), new Vector2D(centerX + t, centerY + g + l), c);
    }
}
