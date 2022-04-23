package dev.zprestige.fire.module.visual.crosshair;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.gui.ScaledResolution;

public class Frame2DListener extends EventListener<Frame2DListener, Crosshair> {

    public Frame2DListener(final Crosshair crosshair) {
        super(Frame2DListener.class, crosshair);
    }

    @Override
    public void invoke(final Object object) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        final float centerX = scaledResolution.getScaledWidth() / 2.0f, centerY = scaledResolution.getScaledHeight() / 2.0f;
        final float l = module.length.GetSlider();
        final float t = module.thickness.GetSlider();
        final float g = module.gap.GetSlider();
        final int c = module.color.GetColor().getRGB();
        RenderUtil.drawRect(new Vector2D(centerX - l - g, centerY - t), new Vector2D(centerX - g, centerY + t), c);
        RenderUtil.drawRect(new Vector2D(centerX + g, centerY - t), new Vector2D(centerX + g + l, centerY + t), c);
        RenderUtil.drawRect(new Vector2D(centerX - t, centerY - g - l), new Vector2D(centerX + t, centerY - g), c);
        RenderUtil.drawRect(new Vector2D(centerX - t, centerY + g), new Vector2D(centerX + t, centerY + g + l), c);
    }
}
