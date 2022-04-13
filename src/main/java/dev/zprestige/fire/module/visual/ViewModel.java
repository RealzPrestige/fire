package dev.zprestige.fire.module.visual;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.RenderItemEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@Descriptor(description = "Changes the way your items in your hands looks")
public class ViewModel extends Module {
    public final Switch removeSway = Menu.Switch("Remove Sway", false).panel("Other");
    public final ColorBox color = Menu.Color("Item Colors", Color.WHITE).panel("Other");

    public final Switch mainhand = Menu.Switch("Mainhand", false).panel("Mainhand");
    public final Switch mainhandTranslation = Menu.Switch("Mainhand Translation", false).visibility(z -> mainhand.GetSwitch()).panel("Mainhand");
    public final Slider mainhandX = Menu.Slider("Mainhand X", 0.0f, -10.0f, 10.0f).visibility(z -> mainhand.GetSwitch() && mainhandTranslation.GetSwitch()).panel("Mainhand");
    public final Slider mainhandY = Menu.Slider("Mainhand Y", 0.0f, -10.0f, 10.0f).visibility(z -> mainhand.GetSwitch() && mainhandTranslation.GetSwitch()).panel("Mainhand");
    public final Slider mainhandZ = Menu.Slider("Mainhand Z", 0.0f, -10.0f, 10.0f).visibility(z -> mainhand.GetSwitch() && mainhandTranslation.GetSwitch()).panel("Mainhand");
    public final Switch mainhandScaling = Menu.Switch("Mainhand Scaling", false).panel("Mainhand").visibility(z -> mainhand.GetSwitch());
    public final Slider mainhandScaleX = Menu.Slider("Mainhand Scale X", 0.0f, -10.0f, 10.0f).visibility(z -> mainhand.GetSwitch() && mainhandScaling.GetSwitch()).panel("Mainhand");
    public final Slider mainhandScaleY = Menu.Slider("Mainhand Scale Y", 0.0f, -10.0f, 10.0f).visibility(z -> mainhand.GetSwitch() && mainhandScaling.GetSwitch()).panel("Mainhand");
    public final Slider mainhandScaleZ = Menu.Slider("Mainhand Scale Z", 0.0f, -10.0f, 10.0f).visibility(z -> mainhand.GetSwitch() && mainhandScaling.GetSwitch()).panel("Mainhand");
    public final Switch mainhandRotation = Menu.Switch("Mainhand Rotation", false).panel("Mainhand").visibility(z -> mainhand.GetSwitch());
    public final Slider mainhandRotationX = Menu.Slider("Mainhand Rotation X", 0.0f, 0.0f, 10.0f).visibility(z -> mainhand.GetSwitch() && mainhandRotation.GetSwitch()).panel("Mainhand");
    public final Slider mainhandRotationY = Menu.Slider("Mainhand Rotation Y", 0.0f, 0.0f, 10.0f).visibility(z -> mainhand.GetSwitch() && mainhandRotation.GetSwitch()).panel("Mainhand");
    public final Slider mainhandRotationZ = Menu.Slider("Mainhand Rotation Z", 0.0f, 0.0f, 10.0f).visibility(z -> mainhand.GetSwitch() && mainhandRotation.GetSwitch()).panel("Mainhand");

    public final Switch offhand = Menu.Switch("Offhand", false).panel("Offhand");
    public final Switch offhandTranslation = Menu.Switch("Offhand Translation", false).visibility(z -> offhand.GetSwitch()).panel("Offhand");
    public final Slider offhandX = Menu.Slider("Offhand X", 0.0f, -10.0f, 10.0f).visibility(z -> offhand.GetSwitch() && offhandTranslation.GetSwitch()).panel("Offhand");
    public final Slider offhandY = Menu.Slider("Offhand Y", 0.0f, -10.0f, 10.0f).visibility(z -> offhand.GetSwitch() && offhandTranslation.GetSwitch()).panel("Offhand");
    public final Slider offhandZ = Menu.Slider("Offhand Z", 0.0f, -10.0f, 10.0f).visibility(z -> offhand.GetSwitch() && offhandTranslation.GetSwitch()).panel("Offhand");
    public final Switch offhandScaling = Menu.Switch("Offhand Scaling", false).panel("Offhand").visibility(z -> offhand.GetSwitch());
    public final Slider offhandScaleX = Menu.Slider("Offhand Scale X", 0.0f, -10.0f, 10.0f).visibility(z -> offhand.GetSwitch() && offhandScaling.GetSwitch()).panel("Offhand");
    public final Slider offhandScaleY = Menu.Slider("Offhand Scale Y", 0.0f, -10.0f, 10.0f).visibility(z -> offhand.GetSwitch() && offhandScaling.GetSwitch()).panel("Offhand");
    public final Slider offhandScaleZ = Menu.Slider("Offhand Scale Z", 0.0f, -10.0f, 10.0f).visibility(z -> offhand.GetSwitch() && offhandScaling.GetSwitch()).panel("Offhand");
    public final Switch offhandRotation = Menu.Switch("Offhand Rotation", false).panel("Offhand").visibility(z -> offhand.GetSwitch());
    public final Slider offhandRotationX = Menu.Slider("Offhand Rotation X", 0.0f, 0.0f, 10.0f).visibility(z -> offhand.GetSwitch() && offhandRotation.GetSwitch()).panel("Offhand");
    public final Slider offhandRotationY = Menu.Slider("Offhand Rotation Y", 0.0f, 0.0f, 10.0f).visibility(z -> offhand.GetSwitch() && offhandRotation.GetSwitch()).panel("Offhand");
    public final Slider offhandRotationZ = Menu.Slider("Offhand Rotation Z", 0.0f, 0.0f, 10.0f).visibility(z -> offhand.GetSwitch() && offhandRotation.GetSwitch()).panel("Offhand");

    @RegisterListener
    public void onFrame3D(final FrameEvent.FrameEvent3D event) {
        if (mc.player.ticksExisted >= 20 && removeSway.GetSwitch()) {
            mc.player.renderArmYaw -= (mc.player.rotationYaw - mc.player.renderArmYaw) * 0.5f;
            mc.player.renderArmPitch -= (mc.player.rotationPitch - mc.player.renderArmPitch) * 0.5f;
        }
    }

    @RegisterListener
    public void onRenderMainhand(final RenderItemEvent.MainHand event) {
        if (mainhand.GetSwitch() && event.getEntityLivingBase().equals(mc.player) && mc.player.getHeldItemMainhand().equals(event.getStack())) {
            if (mainhandTranslation.GetSwitch()) {
                GL11.glTranslated(mainhandX.GetSlider() / 40.0f, mainhandY.GetSlider() / 40.0f, mainhandZ.GetSlider() / 40.0f);
            }
            if (mainhandScaling.GetSwitch()) {
                GlStateManager.scale((mainhandScaleX.GetSlider() / 10.0f) + 1.0f, (mainhandScaleY.GetSlider() / 10.0f) + 1.0f, (mainhandScaleZ.GetSlider() / 10.0f) + 1.0f);
            }
            if (mainhandRotation.GetSwitch()) {
                GlStateManager.rotate(mainhandRotationX.GetSlider() * 36.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(mainhandRotationY.GetSlider() * 36.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(mainhandRotationZ.GetSlider() * 36.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }

    @RegisterListener
    public void onRenderOffhand(final RenderItemEvent.Offhand event) {
        if (offhand.GetSwitch() && event.getEntityLivingBase().equals(mc.player) && mc.player.getHeldItemOffhand().equals(event.getStack())) {
            if (offhandTranslation.GetSwitch()) {
                GL11.glTranslated(offhandX.GetSlider() / 40.0f, offhandY.GetSlider() / 40.0f, offhandZ.GetSlider() / 40.0f);
            }
            if (offhandScaling.GetSwitch()) {
                GlStateManager.scale((offhandScaleX.GetSlider() / 10.0f) + 1.0f, (offhandScaleY.GetSlider() / 10.0f) + 1.0f, (offhandScaleZ.GetSlider() / 10.0f) + 1.0f);
            }
            if (offhandRotation.GetSwitch()) {
                GlStateManager.rotate(offhandRotationX.GetSlider() * 36.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(offhandRotationY.GetSlider() * 36.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(offhandRotationZ.GetSlider() * 36.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }
}
