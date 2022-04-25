package dev.zprestige.fire.module.visual.viewmodel;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.RenderItemEvent;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class RenderItemOffhandListener extends EventListener<RenderItemEvent.Offhand, ViewModel> {

    public RenderItemOffhandListener(final ViewModel viewModel) {
        super(RenderItemEvent.Offhand.class, viewModel);
    }

    @Override
    public void invoke(final Object object) {
        final RenderItemEvent.Offhand event = (RenderItemEvent.Offhand) object;
        if (module.nullCheck()) {
            if (module.offhand.GetSwitch() && event.getEntityLivingBase().equals(mc.player) && mc.player.getHeldItemOffhand().equals(event.getStack())) {
                if (module.offhandTranslation.GetSwitch()) {
                    GL11.glTranslated(module.offhandX.GetSlider() / 40.0f, module.offhandY.GetSlider() / 40.0f, module.offhandZ.GetSlider() / 40.0f);
                }
                if (module.offhandScaling.GetSwitch()) {
                    GlStateManager.scale((module.offhandScaleX.GetSlider() / 10.0f) + 1.0f, (module.offhandScaleY.GetSlider() / 10.0f) + 1.0f, (module.offhandScaleZ.GetSlider() / 10.0f) + 1.0f);
                }
                if (module.offhandRotation.GetSwitch()) {
                    GlStateManager.rotate(module.offhandRotationX.GetSlider() * 36.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(module.offhandRotationY.GetSlider() * 36.0f, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(module.offhandRotationZ.GetSlider() * 36.0f, 0.0f, 0.0f, 1.0f);
                }
            }
        }
    }
}
