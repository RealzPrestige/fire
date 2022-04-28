package dev.zprestige.fire.module.player.packetmine;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.*;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@Descriptor(description = "Assists and renders when you mine blocks")
public class PacketMine extends Module {
    public final Slider range = Menu.Slider("Range", 5.0f, 0.1f, 10.0f);
    public final ComboBox silentSwitch = Menu.ComboBox("Silent On Finish", "None", new String[]{
            "None",
            "Clicked"
    });
    public final Switch instant = Menu.Switch("Instant", false);
    public final Key instantKey = Menu.Key("Instant Key", Keyboard.KEY_NONE).visibility(z -> instant.GetSwitch());
    public final Slider instantTiming = Menu.Slider("Instant Timing", 100.0f, 0.1f, 500.0f).visibility(z -> instant.GetSwitch());
    public final Switch instantSilentSwitch = Menu.Switch("Instant Silent Switch", false).visibility(z -> instant.GetSwitch());
    public final Switch instantPlaceCrystal = Menu.Switch("Instant Place Crystal", false).visibility(z -> instant.GetSwitch());
    public final Switch rotate = Menu.Switch("Rotate", false);
    public final Switch abortOnSwitch = Menu.Switch("Abort On Switch", false);
    public final Switch reBreak = Menu.Switch("ReBreak", false);
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
    protected boolean changed, attemptingReBreak, shouldCancel;
    protected EnumFacing facing, prevFace, minedFace;
    protected BlockPos activePos, prevPos, minedPos;
    protected final Timer timer = new Timer();
    protected float[] col = new float[]{};
    protected float size;

    public PacketMine() {
        eventListeners = new EventListener[]{
                new ClickBlockListener(this),
                new DamageBlockListener(this),
                new Frame3DListener(this),
                new PacketSendListener(this),
                new TickListener(this)
        };
    }

    protected float safety(final float input) {
        return Math.min(1.0f, Math.max(0.0f, input));
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

    public void attemptBreak(final BlockPos pos, final EnumFacing enumFacing) {
        if (rotate.GetSwitch()) {
            Main.rotationManager.facePos(pos);
        }
        Main.interactionManager.attemptBreak(pos, enumFacing);
        prevPos = pos;
        prevFace = enumFacing;
        if (reBreak.GetSwitch() && BlockUtil.getState(pos).equals(Blocks.AIR)) {
            attemptingReBreak = true;
        }
    }

    protected void initiateBreaking(final BlockPos pos, final EnumFacing enumFacing) {
        if (rotate.GetSwitch()) {
            Main.rotationManager.facePos(pos);
        }
        Main.interactionManager.initiateBreaking(pos, enumFacing, true);
        activePos = pos;
        col = new float[]{inactiveColor.GetColor().getRed(), inactiveColor.GetColor().getGreen(), inactiveColor.GetColor().getBlue(), inactiveColor.GetColor().getAlpha()};
        facing = enumFacing;
    }

    protected void abort(final BlockPos pos, final EnumFacing enumFacing) {
        if (rotate.GetSwitch()) {
            Main.rotationManager.facePos(pos);
        }
        Main.interactionManager.abort(pos, enumFacing);
        end();
    }

    protected void abortWithoutEnding(final BlockPos pos, final EnumFacing enumFacing) {
        if (rotate.GetSwitch()) {
            Main.rotationManager.facePos(pos);
        }
        Main.interactionManager.abort(pos, enumFacing);
    }

    protected void end() {
        minedPos = activePos;
        minedFace = facing;
        activePos = null;
        facing = null;
        size = 0.0f;
    }

}
