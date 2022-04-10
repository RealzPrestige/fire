package dev.zprestige.fire.module.player;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.BlockInteractEvent;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;

import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.BlockUtil;

import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

@Descriptor(description = "Assists and renders when you mine blocks")
public class PacketMine extends Module {
    public final Switch render = Menu.Switch("Render", false);
    public final ComboBox renderMode = Menu.ComboBox("Render Mode", "Fade", new String[]{
            "Fade",
            "Grow",
            "Shrink",
            "Height"
    }).visibility(z -> render.GetSwitch());
    public final ComboBox colorMode = Menu.ComboBox("Color Mode", "Fade", new String[]{
            "Static",
            "Fade"
    }).visibility(z -> render.GetSwitch());
    public final ColorBox inactiveColor = Menu.Color("Inactive Color", Color.RED).visibility(z -> render.GetSwitch());
    public final ColorBox activeColor = Menu.Color("Active Color", Color.GREEN).visibility(z -> render.GetSwitch());
    public final Slider outlineWidth = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f).visibility(z -> render.GetSwitch());
    protected float size;
    protected boolean changed;
    protected EnumFacing facing;
    protected BlockPos activePos;
    protected float[] col = new float[]{};

    @RegisterListener
    public void onTick(final TickEvent event) {
        if (activePos != null) {
            if (BlockUtil.getState(activePos).equals(Blocks.AIR)) {
                end();
            }
        }
    }

    @RegisterListener
    public void onFrame3D(FrameEvent.FrameEvent3D event) {
        if (render.GetSwitch() && activePos != null) {
            final float factor = (Minecraft.getDebugFPS() / 20.0f);
            final float updateValue = updateValue(activePos);
            if (size < 1.0f) {
                size += updateValue / factor;
            }
            final AxisAlignedBB bb = new AxisAlignedBB(activePos);
            Color color = inactiveColor.GetColor();
            switch (colorMode.GetCombo()) {
                case "Static":
                    if (size > 0.95f) {
                        color = activeColor.GetColor();
                    }
                    break;
                case "Fade":
                    final Color active = activeColor.GetColor();
                    final float fac = updateValue * size;
                    col[0] = updateColor(col[0], active.getRed(), fac);
                    col[1] = updateColor(col[1], active.getGreen(), fac);
                    col[2] = updateColor(col[2], active.getBlue(), fac);
                    col[3] = updateColor(col[3], active.getAlpha(), fac);
                    color = new Color(col[0] / 255.0f,  col[1] / 255.0f, col[2] / 255.0f, col[3] / 255.0f);
                    break;
            }
            switch (renderMode.GetCombo()) {
                case "Shrink":
                    final AxisAlignedBB bb1 = bb.shrink(size / 2.0f);
                    RenderUtil.drawBoxWithHeight(bb1, color, 1);
                    RenderUtil.drawBlockOutlineBBWithHeight(bb1, color, outlineWidth.GetSlider(), 1);
                    break;
                case "Grow":
                    final AxisAlignedBB bb2 = bb.shrink(0.5f - (size / 2.0f));
                    RenderUtil.drawBoxWithHeight(bb2, color, 1);
                    RenderUtil.drawBlockOutlineBBWithHeight(bb2, color, outlineWidth.GetSlider(), 1);
                    break;
                case "Height":
                    RenderUtil.drawBoxWithHeight(bb, color, size);
                    RenderUtil.drawBlockOutlineBBWithHeight(bb, color, outlineWidth.GetSlider(), size);
                    break;
                case "Fade":
                    final ColorBox colorBox;
                    if (size > 0.9f) {
                        if (changed) {
                            Main.fadeManager.removePosition(activePos);
                            changed = false;
                        }
                        colorBox = activeColor;
                    } else {
                        changed = true;
                        colorBox = inactiveColor;
                    }
                    Main.fadeManager.createFadePosition(activePos, colorBox.GetColor(), colorBox.GetColor(), true, true, outlineWidth.GetSlider(), 50, colorBox.GetColor().getAlpha());
                    break;
            }
        }
    }

    @RegisterListener
    public void onClickBlock(final BlockInteractEvent.ClickBlock event) {
        if (mc.playerController.curBlockDamageMP > 0.1f) {
            mc.playerController.isHittingBlock = true;
        }
        if (activePos != null) {
            Main.interactionManager.attemptBreak(activePos, facing);
        }
    }

    @RegisterListener
    public void onDamageBlock(final BlockInteractEvent.DamageBlock event) {
        if (activePos != null) {
            abort(activePos, facing);
        }
        if (!BlockUtil.getState(event.getPos()).equals(Blocks.BEDROCK)) {
            initiateBreaking(event.getPos(), event.getFacing());
            event.setCancelled(true);
        }
    }

    protected float updateValue(final BlockPos activePos) {
        if (BlockUtil.getState(activePos).equals(Blocks.OBSIDIAN)) {
            return 0.025f;
        } else if (BlockUtil.getState(activePos).equals(Blocks.ENDER_CHEST)) {
            return 0.05f;
        } else {
            return 0.5f;
        }
    }

    protected float updateColor(final float input, final float target, final float factor) {
        if (input > target) {
            return input - (input * factor) / 10;
        }
        if (input < target) {
            return input + (((target - input) * factor)) / 10;
        }
        return input;
    }

    protected void initiateBreaking(final BlockPos pos, final EnumFacing enumFacing) {
        Main.interactionManager.initiateBreaking(pos, enumFacing);
        activePos = pos;
        col = new float[]{inactiveColor.GetColor().getRed(), inactiveColor.GetColor().getGreen(), inactiveColor.GetColor().getBlue(), inactiveColor.GetColor().getAlpha()};
        facing = enumFacing;
    }

    protected void abort(final BlockPos pos, final EnumFacing enumFacing) {
        Main.interactionManager.abort(pos, enumFacing);
        end();
    }

    protected void end() {
        activePos = null;
        facing = null;
        size = 0.0f;
    }

}
