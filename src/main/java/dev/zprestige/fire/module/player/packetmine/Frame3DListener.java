package dev.zprestige.fire.module.player.packetmine;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.FrameEvent;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;

import java.awt.*;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, PacketMine> {

    public Frame3DListener(final PacketMine packetMine) {
        super(FrameEvent.FrameEvent3D.class, packetMine);
    }

    @Override
    public void invoke(final Object object) {
        if (mc.player != null && mc.world != null && module.render.GetSwitch() && module.activePos != null) {
            final float factor = (Minecraft.getDebugFPS() / 20.0f);
            final float updateValue = module.updateValue(module.activePos);
            if (module.size < 1.0f) {
                module.size += updateValue / factor;
            }
            final AxisAlignedBB bb = new AxisAlignedBB(module.activePos);
            Color color = module.inactiveColor.GetColor();
            switch (module.colorMode.GetCombo()) {
                case "Static":
                    if (module.size > 0.95f) {
                        color = module.activeColor.GetColor();
                    }
                    break;
                case "Fade":
                    final Color active = module.activeColor.GetColor();
                    final float fac = updateValue * module.size;
                    module.col[0] = module.updateColor(module.col[0], active.getRed(), fac);
                    module.col[1] = module.updateColor(module.col[1], active.getGreen(), fac);
                    module.col[2] = module.updateColor(module.col[2], active.getBlue(), fac);
                    module.col[3] = module.updateColor(module.col[3], active.getAlpha(), fac);
                    color = new Color(module.col[0] / 255.0f, module.col[1] / 255.0f, module.col[2] / 255.0f, module.col[3] / 255.0f);
                    break;
            }
            switch (module.renderMode.GetCombo()) {
                case "Shrink":
                    final AxisAlignedBB bb1 = bb.shrink(module.size / 2.0f);
                    RenderUtil.drawBoxWithHeight(bb1, color, 1);
                    RenderUtil.drawBlockOutlineBBWithHeight(bb1, color, module.outlineWidth.GetSlider(), 1);
                    break;
                case "Grow":
                    final AxisAlignedBB bb2 = bb.shrink(0.5f - (module.size / 2.0f));
                    RenderUtil.drawBoxWithHeight(bb2, color, 1);
                    RenderUtil.drawBlockOutlineBBWithHeight(bb2, color, module.outlineWidth.GetSlider(), 1);
                    break;
                case "Height":
                    RenderUtil.drawBoxWithHeight(bb, color, module.size);
                    RenderUtil.drawBlockOutlineBBWithHeight(bb, color, module.outlineWidth.GetSlider(), module.size);
                    break;
                case "Fade":
                    final ColorBox colorBox;
                    if (module.size > 0.9f) {
                        if (module.changed) {
                            Main.fadeManager.removePosition(module.activePos);
                            module.changed = false;
                        }
                        colorBox = module.activeColor;
                    } else {
                        module.changed = true;
                        colorBox = module.inactiveColor;
                    }
                    Main.fadeManager.createFadePosition(module.activePos, colorBox.GetColor(), colorBox.GetColor(), true, true, module.outlineWidth.GetSlider(), 50, colorBox.GetColor().getAlpha());
                    break;
            }
            if (module.silentSwitch.GetCombo().equals("Auto") && module.size >= 1.0f) {
                module.attemptBreak(module.activePos, module.facing);
            }
        }
    }
}
