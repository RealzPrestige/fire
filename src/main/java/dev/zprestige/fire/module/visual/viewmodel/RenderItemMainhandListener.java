package dev.zprestige.fire.module.visual.viewmodel;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.RenderItemEvent;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class RenderItemMainhandListener extends EventListener<RenderItemEvent.MainHand, ViewModel> {

    public RenderItemMainhandListener(final ViewModel viewModel){
        super(RenderItemEvent.MainHand.class, viewModel);
    }

    @Override
    public void invoke(final Object object){
        final RenderItemEvent.MainHand event = (RenderItemEvent.MainHand) object;
        if (module.mainhand.GetSwitch() && event.getEntityLivingBase().equals(mc.player) && mc.player.getHeldItemMainhand().equals(event.getStack())) {
            if (module.mainhandTranslation.GetSwitch()) {
                GL11.glTranslated(module.mainhandX.GetSlider() / 40.0f, module.mainhandY.GetSlider() / 40.0f, module.mainhandZ.GetSlider() / 40.0f);
            }
            if (module.mainhandScaling.GetSwitch()) {
                GlStateManager.scale((module.mainhandScaleX.GetSlider() / 10.0f) + 1.0f, (module.mainhandScaleY.GetSlider() / 10.0f) + 1.0f, (module.mainhandScaleZ.GetSlider() / 10.0f) + 1.0f);
            }
            if (module.mainhandRotation.GetSwitch()) {
                GlStateManager.rotate(module.mainhandRotationX.GetSlider() * 36.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(module.mainhandRotationY.GetSlider() * 36.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(module.mainhandRotationZ.GetSlider() * 36.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }
}
