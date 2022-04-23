package dev.zprestige.fire.module.combat.autocrystal;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.FrameEvent;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ConcurrentModificationException;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, AutoCrystal> {
    
    public Frame3DListener(final AutoCrystal autoCrystal){
        super(FrameEvent.FrameEvent3D.class, autoCrystal);
    }

    @Override
    public void invoke(final Object object) {
        final FrameEvent.FrameEvent3D event = (FrameEvent.FrameEvent3D) object;
        final long currentTime = System.currentTimeMillis();
        try {
            module.crystalsPerSecond.removeIf(l -> l + 1000L < currentTime);
        } catch (ConcurrentModificationException ignored) {
        }
        if (module.predictMotionVisualize.GetSwitch() && module.entityOtherPlayerMP != null) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 0.2f);
            RenderUtil.renderEntity(module.entityOtherPlayerMP, event.getPartialTicks());
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
        switch (module.animation.GetCombo()) {
            case "Interpolate":
                if (module.bb != null) {
                    if (module.pos != null) {
                        AxisAlignedBB cc = new AxisAlignedBB(module.pos);
                        if (!module.bb.equals(cc)) {
                            module.bb = module.bb.offset((module.pos.getX() - module.bb.minX) * (module.interpolateSpeed.GetSlider() / 1000f), (module.pos.getY() - module.bb.minY) * (module.interpolateSpeed.GetSlider() / 1000f), (module.pos.getZ() - module.bb.minZ) * (module.interpolateSpeed.GetSlider() / 1000f));
                        }
                        if (module.box.GetSwitch()) {
                            RenderUtil.drawBBBoxWithHeight(module.bb, module.boxColor.GetColor(), 1);
                        }
                        if (module.outline.GetSwitch()) {
                            RenderUtil.drawBlockOutlineBBWithHeight(module.bb, module.boxColor.GetColor(), module.outlineWidth.GetSlider(), 1);
                        }
                        if (module.renderCPS.GetSwitch()) {
                            RenderUtil.draw3DText(module.crystalsPerSecond.size() + "", module.bb.minX + 0.5f - mc.getRenderManager().renderPosX, module.bb.minY - mc.getRenderManager().renderPosY, module.bb.minZ + 0.5f - mc.getRenderManager().renderPosZ, 5, 255, 255, 255, 255);
                        }
                    }

                }
                break;
            case "None":
                if (module.pos != null) {
                    if (module.box.GetSwitch()) {
                        RenderUtil.drawBox(module.pos, module.boxColor.GetColor());
                    }
                    if (module.outline.GetSwitch()) {
                        RenderUtil.drawOutline(module.pos, module.outlineColor.GetColor(), module.outlineWidth.GetSlider());
                    }
                    if (module.renderCPS.GetSwitch()) {
                        RenderUtil.draw3DText(module.crystalsPerSecond.size() + "", module.pos.getX() + 0.5f - mc.getRenderManager().renderPosX, module.pos.getY() - mc.getRenderManager().renderPosY, module.pos.getZ() + 0.5f - mc.getRenderManager().renderPosZ, 5, 255, 255, 255, 255);
                    }
                }
                break;
            case "Fade":
            case "Shrink":
                if (module.pos != null && module.renderCPS.GetSwitch()) {
                    RenderUtil.draw3DText(module.crystalsPerSecond.size() + "", module.pos.getX() + 0.5f - mc.getRenderManager().renderPosX, module.pos.getY() - mc.getRenderManager().renderPosY, module.pos.getZ() + 0.5f - mc.getRenderManager().renderPosZ, 5, 255, 255, 255, 255);
                }
                break;
        }
    }
}
