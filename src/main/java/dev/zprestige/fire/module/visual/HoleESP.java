package dev.zprestige.fire.module.visual;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.manager.HoleManager;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.AnimationUtil;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

@Descriptor(description = "Draws holes you can walk into and not take crystal damage in")
public class HoleESP extends Module {
    public final ComboBox animation = Menu.ComboBox("Animation", "None", new String[]{
            "None",
            "Fade",
            "Grow",
            "Pop"
    });
    protected final Slider fadeAmount = Menu.Slider("Fade Amount", 30.0f, 0.1f, 50.0f);
    public final Slider animationSpeed = Menu.Slider("Animation Speed", 1.0f, 1.0f, 10.0f).visibility(z -> animation.GetCombo().equals("Grow") || animation.GetCombo().equals("Pop"));
    public final Slider startY = Menu.Slider("Start Y", 1.0f, 0.1f, 10.0f).visibility(z -> animation.GetCombo().equals("Pop"));
    public final Slider height = Menu.Slider("Height", 1.0f, 0.0f, 2.0f);
    public final Switch bedrockBox = Menu.Switch("Bedrock Box", false);
    public final ColorBox bedrockBoxColor = Menu.Color("Bedrock Box Color", Color.GREEN).visibility(z -> bedrockBox.GetSwitch());
    public final Switch bedrockOutline = Menu.Switch("Bedrock Outline", false);
    public final ColorBox bedrockOutlineColor = Menu.Color("Bedrock Outline Color", Color.GREEN).visibility(z -> bedrockOutline.GetSwitch());
    public final Switch obsidianBox = Menu.Switch("Obsidian Box", false);
    public final ColorBox obsidianBoxColor = Menu.Color("Obsidian Box Color", Color.RED).visibility(z -> obsidianBox.GetSwitch());
    public final Switch obsidianOutline = Menu.Switch("Obsidian Outline", false);
    public final ColorBox obsidianOutlineColor = Menu.Color("Obsidian Outline Color", Color.RED).visibility(z -> obsidianOutline.GetSwitch());
    public final Slider lineWidth = Menu.Slider("Line Width", 1.0f, 0.1f, 5.0f).visibility(z -> obsidianOutline.GetSwitch());
    protected final ICamera camera = new Frustum();
    protected final ArrayList<AnimatingHole> animatingHoles = new ArrayList<>();

    @RegisterListener
    public void onFrame3D(final FrameEvent.FrameEvent3D event) {
        camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        Main.holeManager.getHoles().stream().filter(holePos -> !contains(holePos.getPos())).map(AnimatingHole::new).forEach(animatingHoles::add);
        new ArrayList<>(animatingHoles).forEach(animatingHole -> {
            if (!holeManagerContains(animatingHole.pos.getPos())) {
                animatingHole.remove = true;
            }
            if (animatingHole.canRemove()) {
                animatingHoles.remove(animatingHole);
            } else {
                animatingHole.update();
            }
        });
    }

    protected boolean holeManagerContains(final BlockPos pos) {
        return Main.holeManager.getHoles().stream().anyMatch(holePos -> holePos.getPos().equals(pos));
    }

    protected boolean contains(final BlockPos pos) {
        return animatingHoles.stream().anyMatch(animatingHole -> animatingHole.pos.getPos().equals(pos));
    }

    protected void renderHole(final HoleManager.HolePos holePos, final AxisAlignedBB bb, boolean box, boolean outline, final Color boxColor, final Color outlineColor) {
        if (camera.isBoundingBoxInFrustum(bb.grow(2.0))) {
            if (holePos.isDouble()) {
                if (holePos.isWestDouble()) {
                    if (box) {
                        RenderUtil.drawCustomBB(boxColor, bb.minX - 1, bb.minY, bb.minZ, bb.maxX, bb.maxY - 1 + height.GetSlider(), bb.maxZ);
                    }
                    if (outline) {
                        RenderUtil.drawBlockOutlineBBWithHeight(new AxisAlignedBB(bb.minX - 1, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ), outlineColor, lineWidth.GetSlider(), height.GetSlider());
                    }
                } else {
                    if (box) {
                        RenderUtil.drawCustomBB(boxColor, bb.minX, bb.minY, bb.minZ - 1, bb.maxX, bb.maxY - 1 + height.GetSlider(), bb.maxZ);
                    }
                    if (outline) {
                        RenderUtil.drawBlockOutlineBBWithHeight(new AxisAlignedBB(bb.minX, bb.minY, bb.minZ - 1, bb.maxX, bb.maxY, bb.maxZ), outlineColor, lineWidth.GetSlider(), height.GetSlider());
                    }
                }
            } else {
                if (box) {
                    RenderUtil.drawBoxWithHeight(bb, boxColor, height.GetSlider());
                }
                if (outline) {
                    RenderUtil.drawBlockOutlineBBWithHeight(bb, outlineColor, lineWidth.GetSlider(), height.GetSlider());
                }
            }
        }
    }

    protected class AnimatingHole {
        protected final HoleManager.HolePos pos;
        protected AxisAlignedBB bb;
        protected float scale, y;
        protected boolean remove;

        public AnimatingHole(final HoleManager.HolePos pos) {
            this.pos = pos;
            this.bb = new AxisAlignedBB(pos.getPos());
            this.scale = animation.GetCombo().equals("Grow") ? 0.5f : 1.0f;
            this.y = animation.GetCombo().equals("Pop") ? startY.GetSlider() / 10.0f : 0.0f;
            remove = false;
        }

        protected void update() {
            AxisAlignedBB bb = this.bb;
            boolean box = pos.isBedrock() ? bedrockBox.GetSwitch() : obsidianBox.GetSwitch();
            boolean outline = pos.isBedrock() ? bedrockOutline.GetSwitch() : obsidianOutline.GetSwitch();
            Color color = pos.isBedrock() ? bedrockBoxColor.GetColor() : obsidianBoxColor.GetColor();
            Color outlineColor = pos.isBedrock() ? bedrockOutlineColor.GetColor() : obsidianOutlineColor.GetColor();
            switch (animation.GetCombo()) {
                case "Fade":
                    final int alpha = (int) Math.min(color.getAlpha() / (Math.max(1.0, mc.player.getDistanceSq(pos.getPos()) / fadeAmount.GetSlider())), color.getAlpha());
                    color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                    outlineColor = new Color(outlineColor.getRed(), outlineColor.getGreen(), outlineColor.getBlue(), alpha);
                    break;
                case "Grow":
                    if (remove) {
                        scale = AnimationUtil.decreaseNumber(scale, 0.5f, ((scale - 0.5f) / Minecraft.getDebugFPS()) * animationSpeed.GetSlider());
                    } else {
                        scale = AnimationUtil.increaseNumber(scale, 1.0f, ((1.0f - scale) / Minecraft.getDebugFPS()) * animationSpeed.GetSlider());
                    }
                    bb = bb.shrink(scale);
                    break;
                case "Pop":
                    if (remove) {
                        y = AnimationUtil.increaseNumber(y, startY.GetSlider() / 10.0f, ((startY.GetSlider() / 10.0f - y) / Minecraft.getDebugFPS()) * animationSpeed.GetSlider());
                    } else {
                        y = AnimationUtil.decreaseNumber(y, 0.0f, (y / Minecraft.getDebugFPS()) * animationSpeed.GetSlider());
                    }
                    bb = new AxisAlignedBB(bb.minX, bb.minY - y, bb.minZ, bb.maxX, bb.maxY - y, bb.maxZ);
                    break;
            }
            renderHole(pos, bb, box, outline, color, outlineColor);
        }

        protected boolean canRemove() {
            if (remove) {
                switch (animation.GetCombo()) {
                    case "None":
                    case "Fade":
                        return true;
                    case "Grow":
                        return scale <= 0.52f;
                    case "Pop":
                        return y + 0.05f >= (startY.GetSlider() / 10.0f);
                }
            }
            return false;
        }
    }
}
