package dev.zprestige.fire.module.visual.viewtweaks;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.FrameEvent;
import net.minecraft.client.settings.GameSettings;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, ViewTweaks> {

    public Frame3DListener(final ViewTweaks viewTweaks){
        super(FrameEvent.FrameEvent3D.class, viewTweaks);
    }

    @Override
    public void invoke(final Object object){
        mc.world.setTotalWorldTime((long) module.time.GetSlider());
        switch (module.weather.GetCombo()) {
            case "Clear":
                mc.world.setRainStrength(0);
                break;
            case "Rain":
                mc.world.setRainStrength(1);
                break;
            case "Thunder":
                mc.world.setRainStrength(2);
                break;
        }
        if (module.removeSwitchAnimation.GetSwitch()) {
            if (mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
                mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
                mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItemMainhand();
            }
            if (mc.entityRenderer.itemRenderer.prevEquippedProgressOffHand >= 0.9) {
                mc.entityRenderer.itemRenderer.equippedProgressOffHand = 1.0f;
                mc.entityRenderer.itemRenderer.itemStackOffHand = mc.player.getHeldItemOffhand();
            }
        }
        if (module.removeSwing.GetSwitch()) {
            mc.player.isSwingInProgress = false;
            mc.player.swingProgressInt = 0;
            mc.player.swingProgress = 0.0f;
            mc.player.prevSwingProgress = 0.0f;
        }
        if (module.fullBright.GetSwitch() && mc.gameSettings.gammaSetting != 1000) {
            mc.gameSettings.gammaSetting = 1000;
        }
        mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, module.fov.GetSlider());
    }
}
