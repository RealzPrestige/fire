package dev.zprestige.fire.module.combat;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.manager.HoleManager;
import dev.zprestige.fire.manager.PlayerManager;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.EntityUtil;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Descriptor(description = "Fills holes in your area")
public class HoleFiller extends Module {
    public final ComboBox mode = Menu.ComboBox("Mode", "Normal", new String[]{
            "Normal",
            "Smart"
    }).panel("Targeting");
    public final ComboBox smartMode = Menu.ComboBox("Smart Mode", "Closest", new String[]{
            "Closest",
            "InRange"
    }).visibility(z -> mode.GetCombo().equals("Smart")).panel("Targeting");
    public final ComboBox targetPriority = Menu.ComboBox("Target Priority", "UnSafe", new String[]{
            "Range",
            "UnSafe",
            "Health",
            "Fov",
    }).visibility(z -> mode.GetCombo().equals("Smart")).panel("Targeting");
    public final ComboBox timing = Menu.ComboBox("Timing", "Instant", new String[]{
            "Instant",
            "Tick"
    }).panel("Timing");
    public final ComboBox block = Menu.ComboBox("Block", "Obsidian", new String[]{
            "Obsidian",
            "Enderchests",
            "Fallback",
            "Webs"
    });
    public final Slider blocksPerTick = Menu.Slider("Blocks Per Tick", 8.0f, 1.0f, 20.0f).visibility(z -> timing.GetCombo().equals("Instant")).panel("Timing");
    public final Slider placeRange = Menu.Slider("Place Range", 5.0f, 0.1f, 6.0f).panel("Ranges");
    public final Slider targetRange = Menu.Slider("Target Range", 9.0f, 0.1f, 15.0f).visibility(z -> mode.GetCombo().equals("Smart")).panel("Ranges");
    public final Slider smartRange = Menu.Slider("Smart Range", 3.0f, 0.1f, 6.0f).visibility(z -> mode.GetCombo().equals("Smart")).panel("Ranges");
    public final Switch excludeTargetY = Menu.Switch("Exclude Target Y", true).visibility(z -> mode.GetCombo().equals("Smart")).panel("Ranges");
    public final Switch antiSelf = Menu.Switch("Anti Self", true).panel("Ranges");
    public final Slider minSelfRange = Menu.Slider("Min Self Range", 3.0f, 0.1f, 6.0f).panel("Ranges");
    public final Switch predictMotion = Menu.Switch("Predict Motion", false).panel("Placing");
    public final Slider predictMotionFactor = Menu.Slider("Predict Motion Factor", 2.0f, 1.0f, 20.0f).visibility(z -> predictMotion.getValue()).panel("Placing");
    public final Switch multiTask = Menu.Switch("Multi Task", true).panel("Placing");
    public final Switch packet = Menu.Switch("Packet", true).panel("Placing");
    public final Switch rotate = Menu.Switch("Rotate", false).panel("Placing");
    public final Switch preventRotationRubberband = Menu.Switch("Prevent Rotation Rubberband", false).visibility(z -> rotate.GetSwitch()).panel("Placing");
    public final Switch strict = Menu.Switch("Strict", false).panel("Placing");
    public final Switch doubles = Menu.Switch("Doubles", true).panel("Placing");
    public final Switch whileMoving = Menu.Switch("While Moving", false).panel("Other");
    public final Switch enemyUnsafe = Menu.Switch("Enemy Unsafe", true).visibility(z -> mode.GetCombo().equals("Smart")).panel("Other");
    public final Switch selfSafe = Menu.Switch("Self Safe", true).panel("Other");
    public final Switch render = Menu.Switch("Render", false).panel("Rendering");
    public final ComboBox animation = Menu.ComboBox("Animation", "Fade", new String[]{
            "None",
            "Fade",
            "Shrink"
    }).visibility(z -> render.GetSwitch()).panel("Rendering");
    public final Slider fadeSpeed = Menu.Slider("Fade Speed", 25.0f, 0.1f, 100.0f).visibility(z -> render.GetSwitch() && animation.GetCombo().equals("Fade")).panel("Rendering");
    public final Slider shrinkSpeed = Menu.Slider("Shrink Speed", 25.0f, 0.1f, 100.0f).visibility(z -> render.GetSwitch() && animation.GetCombo().equals("Shrink")).panel("Rendering");
    public final Switch box = Menu.Switch("Box", false).visibility(z -> render.GetSwitch()).panel("Rendering");
    public final ColorBox boxColor = Menu.Color("Box Color", new Color(255, 255, 255, 120)).visibility(z -> box.GetSwitch() && render.GetSwitch()).panel("Rendering");
    public final Switch outline = Menu.Switch("Outline", false).visibility(z -> render.GetSwitch()).panel("Rendering");
    public final ColorBox outlineColor = Menu.Color("Outline Color", new Color(255, 255, 255, 255)).visibility(z -> render.GetSwitch() && outline.GetSwitch()).panel("Rendering");
    public final Slider outlineWidth = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f).visibility(z -> render.GetSwitch() && outline.GetSwitch()).panel("Rendering");
    protected BlockPos renderPos;

    @RegisterListener
    public void onTick(final TickEvent event) {
        final int slot = slot();
        if (slot == -1 || (!whileMoving.GetSwitch() && EntityUtil.isMoving()) || (selfSafe.GetSwitch() && !BlockUtil.isPlayerSafe(new PlayerManager.Player(mc.player))) || (!multiTask.GetSwitch() && mc.player.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) && mc.gameSettings.keyBindUseItem.isKeyDown())) {
            return;
        }
        switch (mode.GetCombo()) {
            case "Normal":
                int bpt = 0;
                for (final HoleManager.HolePos pos : Main.holeManager.getHoles()) {
                    if (pos.isDouble() && !doubles.GetSwitch()){
                        continue;
                    }
                    final BlockPos pos1 = pos.getPos();
                    if (bpt <= blocksPerTick.GetSlider() && inRange(pos1) && checkRotations()) {
                        placeBlock(pos1, slot);
                        if (timing.GetCombo().equals("Normal")) {
                            return;
                        }
                        bpt++;
                    }
                }
                break;
            case "Smart":
                final PlayerManager.Player player = EntityUtil.getClosestTarget(targetPriority(targetPriority.GetCombo()), targetRange.GetSlider());
                if (player == null || player.getEntityPlayer() == null || player.getEntityPlayer().isDead || (enemyUnsafe.GetSwitch() && !BlockUtil.isPlayerSafe(player))) {
                    return;
                }
                final double[] position = new double[]{player.getEntityPlayer().posX, player.getEntityPlayer().posY, player.getEntityPlayer().posZ};
                final double[] next = Main.motionPredictionManager.getPredictedPosByPlayer(player.getEntityPlayer(), predictMotionFactor.GetSlider());
                if (predictMotion.GetSwitch()) {
                    player.getEntityPlayer().setPosition(next[0], next[1], next[2]);
                }
                switch (smartMode.GetCombo()) {
                    case "Closest":
                        final BlockPos closest = closestHole(player);
                        if (closest != null && checkRotations()) {
                            placeBlock(closest, slot);
                        }
                        break;
                    case "InRange":
                        final ArrayList<BlockPos> possesInRange = possesInRange(player);
                        int bpt1 = 0;
                        for (final BlockPos pos : possesInRange) {
                            if (bpt1 <= blocksPerTick.GetSlider() && inRange(pos) && checkRotations()) {
                                placeBlock(pos, slot);
                                if (timing.GetCombo().equals("Normal")) {
                                    return;
                                }
                                bpt1++;
                            }
                        }
                        break;
                }
                player.getEntityPlayer().setPosition(position[0], position[1], position[2]);
                break;
        }
    }

    @RegisterListener
    public void onFrame3D(final FrameEvent.FrameEvent3D event){
        if (render.GetSwitch() && renderPos != null){
            if (box.GetSwitch()){
                RenderUtil.drawBox(renderPos, boxColor.GetColor());
            }
            if (outline.GetSwitch()){
                RenderUtil.drawOutline(renderPos, outlineColor.GetColor(), outlineWidth.GetSlider());
            }
        }
    }

    protected void placeBlock(final BlockPos pos, final int slot) {
      if (slot != -1) {
          Main.interactionManager.placeBlockWithSwitch(pos, rotate.GetSwitch(), packet.GetSwitch(), strict.GetSwitch(), slot);
          if (render.GetSwitch()) {
              switch (animation.GetCombo()) {
                  case "None":
                      renderPos = pos;
                      break;
                  case "Fade":
                      Main.fadeManager.createFadePosition(pos, boxColor.GetColor(), outlineColor.GetColor(), box.GetSwitch(), outline.GetSwitch(), outlineWidth.GetSlider(), fadeSpeed.GetSlider(), boxColor.GetColor().getAlpha());
                      break;
                  case "Shrink":
                      Main.shrinkManager.createShrinkPosition(pos, boxColor.GetColor(), outlineColor.GetColor(), box.GetSwitch(), outline.GetSwitch(), outlineWidth.GetSlider(), shrinkSpeed.GetSlider());
                      break;
              }
          }
      }
    }

    protected boolean inRange(final BlockPos pos) {
        final double dist = mc.player.getDistanceSq(pos) / 2.0f;
        return mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).isEmpty() && dist <= placeRange.GetSlider() * 2 && ((!antiSelf.GetSwitch() || dist > minSelfRange.GetSlider() * 2) || BlockUtil.isPlayerSafe(new PlayerManager.Player(mc.player)));
    }

    protected boolean inSmartRange(final PlayerManager.Player player, final BlockPos pos) {
        return (excludeTargetY.GetSwitch() ? player.getDistanceToPos(new BlockPos(pos.getX(), player.getPosition().getY(), pos.getZ())) : player.getDistanceToPos(pos)) / 2.0f <= smartRange.GetSlider() * 2;
    }

    protected boolean checkRotations() {
        return !preventRotationRubberband.GetSwitch() || !Main.rotationManager.maxRotations();
    }

    protected BlockPos closestHole(final PlayerManager.Player player) {
        return Main.holeManager.getHoles().stream().filter(holePos -> !holePos.isDouble() || doubles.GetSwitch()).map(HoleManager.HolePos::getPos).filter(pos -> inRange(pos) && inSmartRange(player, pos)).collect(Collectors.toMap(player::getDistanceToPos, pos -> pos, (a, b) -> b, TreeMap::new)).firstEntry().getValue();
    }

    protected ArrayList<BlockPos> possesInRange(final PlayerManager.Player player) {
        final ArrayList<BlockPos> posses = new ArrayList<>();
        Main.holeManager.getHoles().stream().filter(holePos -> !holePos.isDouble() || doubles.GetSwitch()).map(HoleManager.HolePos::getPos).filter(pos -> inRange(pos) && inSmartRange(player, pos)).forEach(pos -> pos.add(pos));
        return posses;
    }

    protected EntityUtil.TargetPriority targetPriority(final String string) {
        return Arrays.stream(EntityUtil.TargetPriority.values()).filter(targetPriority1 -> targetPriority1.toString().equals(string)).findFirst().orElse(null);
    }

    protected int slot() {
        switch (block.GetCombo()) {
            case "Obsidian":
                return Main.inventoryManager.getBlockFromHotbar(Blocks.OBSIDIAN);
            case "Enderchests":
                return Main.inventoryManager.getBlockFromHotbar(Blocks.ENDER_CHEST);
            case "Fallback":
                final int slot = Main.inventoryManager.getBlockFromHotbar(Blocks.OBSIDIAN);
                if (slot == -1) {
                    return Main.inventoryManager.getBlockFromHotbar(Blocks.ENDER_CHEST);
                }
                return slot;
            case "Webs":
                return Main.inventoryManager.getBlockFromHotbar(Blocks.WEB);
        }
        return -1;
    }
}
