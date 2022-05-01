package dev.zprestige.fire.module.visual.crosshair;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.RenderOverlayEvent;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class RenderOverlayListener extends EventListener<RenderOverlayEvent, Crosshair> {

    public RenderOverlayListener(final Crosshair crosshair) {
        super(RenderOverlayEvent.class, crosshair);
    }

    @Override
    public void invoke(final Object object) {
        final RenderOverlayEvent event = (RenderOverlayEvent) object;
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.CROSSHAIRS)) {
            final ScaledResolution scaledResolution = new ScaledResolution(mc);
            final float centerX = scaledResolution.getScaledWidth() / 2.0f, centerY = scaledResolution.getScaledHeight() / 2.0f;
            final float l = module.length.GetSlider();
            final float t = module.thickness.GetSlider();
            final float g = module.gap.GetSlider();
            final int c = module.color.GetColor().getRGB();
            RenderUtil.drawRect(centerX - l - g, centerY - t, centerX - g, centerY + t, c);
            RenderUtil.drawRect(centerX + g, centerY - t, centerX + g + l, centerY + t, c);
            RenderUtil.drawRect(centerX - t, centerY - g - l, centerX + t, centerY - g, c);
            RenderUtil.drawRect(centerX - t, centerY + g, centerX + t, centerY + g + l, c);
            event.setCancelled();
        }
    }
}
