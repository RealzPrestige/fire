package dev.zprestige.fire.module.visual;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.manager.HoleManager;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.util.math.AxisAlignedBB;

import java.awt.*;
import java.util.Objects;

@Descriptor(description = "Draws holes you can walk into and not take crystal damage in")
public class HoleESP extends Module {
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

    @RegisterListener
    public void onFrame3D(final FrameEvent.FrameEvent3D event) {
        camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        for (final HoleManager.HolePos holePos : Main.holeManager.getHoles()) {
            final AxisAlignedBB bb = new AxisAlignedBB(holePos.getPos());
            if (camera.isBoundingBoxInFrustum(bb.grow(2.0))) {
                switch (holePos.getHoleType()) {
                    case Bedrock:
                        if (bedrockBox.GetSwitch()) {
                            RenderUtil.drawBoxWithHeight(bb, bedrockBoxColor.GetColor(), height.GetSlider());
                        }
                        if (bedrockOutline.GetSwitch()) {
                            RenderUtil.drawBlockOutlineBBWithHeight(bb, bedrockOutlineColor.GetColor(), lineWidth.GetSlider(), height.GetSlider());
                        }
                        break;
                    case Obsidian:
                        if (obsidianBox.GetSwitch()) {
                            RenderUtil.drawBoxWithHeight(bb, obsidianBoxColor.GetColor(), height.GetSlider());
                        }
                        if (obsidianOutline.GetSwitch()) {
                            RenderUtil.drawBlockOutlineBBWithHeight(bb, obsidianOutlineColor.GetColor(), lineWidth.GetSlider(), height.GetSlider());
                        }
                        break;
                    case DoubleBedrockNorth:
                        if (bedrockBox.GetSwitch()) {
                            RenderUtil.drawCustomBB(bedrockBoxColor.GetColor(), bb.minX, bb.minY, bb.minZ - 1, bb.maxX, bb.maxY - 1 + height.GetSlider(), bb.maxZ);
                        }
                        if (bedrockOutline.GetSwitch()) {
                            RenderUtil.drawBlockOutlineBBWithHeight(new AxisAlignedBB(bb.minX, bb.minY, bb.minZ - 1, bb.maxX, bb.maxY, bb.maxZ), bedrockOutlineColor.GetColor(), lineWidth.GetSlider(), height.GetSlider());
                        }
                        break;
                    case DoubleObsidianNorth:
                        if (obsidianBox.GetSwitch()) {
                            RenderUtil.drawCustomBB(obsidianBoxColor.GetColor(), bb.minX, bb.minY, bb.minZ - 1, bb.maxX, bb.maxY - 1 + height.GetSlider(), bb.maxZ);
                        }
                        if (obsidianOutline.GetSwitch()) {
                            RenderUtil.drawBlockOutlineBBWithHeight(new AxisAlignedBB(bb.minX, bb.minY, bb.minZ - 1, bb.maxX, bb.maxY, bb.maxZ), obsidianOutlineColor.GetColor(), lineWidth.GetSlider(), height.GetSlider());
                        }
                        break;
                    case DoubleBedrockWest:
                        if (bedrockBox.GetSwitch()) {
                            RenderUtil.drawCustomBB(bedrockBoxColor.GetColor(), bb.minX - 1, bb.minY, bb.minZ, bb.maxX, bb.maxY - 1 + height.GetSlider(), bb.maxZ);
                        }
                        if (bedrockOutline.GetSwitch()) {
                            RenderUtil.drawBlockOutlineBBWithHeight(new AxisAlignedBB(bb.minX - 1, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ), bedrockOutlineColor.GetColor(), lineWidth.GetSlider(), height.GetSlider());
                        }
                        break;
                    case DoubleObsidianWest:
                        if (obsidianBox.GetSwitch()) {
                            RenderUtil.drawCustomBB(obsidianBoxColor.GetColor(), bb.minX - 1, bb.minY, bb.minZ, bb.maxX, bb.maxY - 1 + height.GetSlider(), bb.maxZ);
                        }
                        if (obsidianOutline.GetSwitch()) {
                            RenderUtil.drawBlockOutlineBBWithHeight(new AxisAlignedBB(bb.minX - 1, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ), obsidianOutlineColor.GetColor(), lineWidth.GetSlider(), height.GetSlider());
                        }
                        break;
                }
            }
        }
    }
}
