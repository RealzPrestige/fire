package dev.zprestige.fire.module.combat;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.manager.PlayerManager;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;

public class AutoTrap extends Module {
    public final ComboBox placeMode = Menu.ComboBox("Place Mode", "Default", new String[]{
            "Default",
            "DoubleTop"
    });
    public final ComboBox mode = Menu.ComboBox("Mode", "Instant", new String[]{
            "Instant",
            "Tick"
    });
    public final Slider blocksPerTick = Menu.Slider("Blocks Per Tick", 10.0f, 1.0f, 20.0f).visibility(z -> mode.GetCombo().equals("Instant"));
    public final Slider placeRange = Menu.Slider("Place Range", 5.0f, 0.1f, 6.0f);
    public final Switch multiTask = Menu.Switch("Multi Task", true);
    public final Switch liquids = Menu.Switch("Liquids", false);
    public final Switch packet = Menu.Switch("Packet", true);
    public final Switch rotate = Menu.Switch("Rotate", false);
    public final Switch strict = Menu.Switch("Strict", false);
    public final Switch render = Menu.Switch("Render", false);
    public final Slider fadeSpeed = Menu.Slider("Fade Speed", 25.0f, 0.1f, 100.0f).visibility(z -> render.GetSwitch());
    public final Switch box = Menu.Switch("Box", false).visibility(z -> render.GetSwitch());
    public final ColorBox boxColor = Menu.Color("Box Color", new Color(255, 255, 255, 120)).visibility(z -> render.GetSwitch() && box.GetSwitch());
    public final Switch outline = Menu.Switch("Outline", false).visibility(z -> render.GetSwitch());
    public final ColorBox outlineColor = Menu.Color("Outline Color", new Color(255, 255, 255, 255)).visibility(z -> render.GetSwitch() && outline.GetSwitch());
    public final Slider outlineWidth = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f).visibility(z -> render.GetSwitch() && outline.GetSwitch());
    protected final Vec3i[] surroundings = new Vec3i[]{
            new Vec3i(0, 0, -1),
            new Vec3i(0, 0, 1),
            new Vec3i(-1, 0, 0),
            new Vec3i(1, 0, 0),
            new Vec3i(0, 1, 0),
            new Vec3i(0, -1, 0),
    };
    protected final Vec3i[] list = new Vec3i[]{
            new Vec3i(0, 1, -1),
            new Vec3i(0, 1, 1),
            new Vec3i(-1, 1, 0),
            new Vec3i(1, 1, 0),
    };
    protected final Vec3i[] topSupport = new Vec3i[]{
            new Vec3i(0, 2, -1),
            new Vec3i(0, 2, 1),
            new Vec3i(-1, 2, 0),
            new Vec3i(1, 2, 0),
    };
    protected final Vec3i top = new Vec3i(0, 2, 0);

    @RegisterListener
    public void onTick(final TickEvent event) {
        final PlayerManager.Player entityPlayer = EntityUtil.getClosestTarget(EntityUtil.TargetPriority.Range, placeRange.GetSlider() - 1.0f);
        if (entityPlayer == null || !BlockUtil.isPlayerSafe(entityPlayer) || multiTaskValid()) {
            return;
        }
        final BlockPos pos = entityPlayer.getPosition();
        final ArrayList<Position> positions = getPossiblePlacePositions(pos);
        final int obsidian = Main.inventoryManager.getBlockFromHotbar(Blocks.OBSIDIAN);
        if (obsidian == -1) {
            disableModule();
        }
        switch (mode.GetCombo()) {
            case "Instant":
                int i = 0;
                for (final Position position : positions) {
                    if (i > blocksPerTick.GetSlider() || Main.inventoryManager.getBlockFromHotbar(Blocks.OBSIDIAN) == -1) {
                        return;
                    }
                    Main.interactionManager.placeBlockWithSwitch(position.getPos(), rotate.GetSwitch(), packet.GetSwitch(), strict.GetSwitch(), obsidian);
                    addFade(position.getPos());
                    i++;
                }
                break;
            case "Tick":
                for (final Position position : positions) {
                    Main.interactionManager.placeBlockWithSwitch(position.getPos(), rotate.GetSwitch(), packet.GetSwitch(), strict.GetSwitch(), obsidian);
                    addFade(position.getPos());
                    return;
                }
                break;
        }
    }

    protected boolean multiTaskValid(){
        return !multiTask.GetSwitch() && mc.player.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) && mc.gameSettings.keyBindUseItem.isKeyDown();
    }

    protected void addFade(final BlockPos pos) {
        if (render.GetSwitch()) {
            Main.fadeManager.createFadePosition(pos, boxColor.GetColor(), outlineColor.GetColor(), box.GetSwitch(), outline.GetSwitch(), outlineWidth.GetSlider(), fadeSpeed.GetSlider(), boxColor.GetColor().getAlpha());
        }
    }

    protected ArrayList<Position> getPossiblePlacePositions(final BlockPos pos) {
        final ArrayList<Position> posses = new ArrayList<>();
        for (final Vec3i vec3i : list) {
            final BlockPos pos1 = pos.add(vec3i);
            if (canPlace(pos1) && containsNoEntities(pos1) && valid(pos1)) {
                final Position position = new Position(pos1);
                posses.add(position);
            }
        }
        if (needsTopSupport(pos)) {
            final TreeMap<Double, BlockPos> supports = new TreeMap<>();
            for (final Vec3i vec3i : topSupport) {
                final BlockPos pos1 = pos.add(vec3i);
                if (canPlace(pos1) && containsNoEntities(pos1) && valid(pos1)) {
                    supports.put(mc.player.getDistanceSq(pos1), pos1);
                }
            }
            if (!supports.isEmpty()) {
                final Position position = new Position(supports.firstEntry().getValue());
                posses.add(position);
            }
        }
        final BlockPos topPos = pos.add(top);
        if (canPlace(topPos) && containsNoEntities(topPos) && valid(topPos)) {
            final Position position = new Position(topPos);
            posses.add(position);
        }
        if (placeMode.GetCombo().equals("DoubleTop")) {
            final BlockPos doubleTopPos = pos.add(top).up();
            if (canPlace(doubleTopPos) && containsNoEntities(doubleTopPos) && valid(doubleTopPos)) {
                final Position position = new Position(doubleTopPos);
                posses.add(position);
            }
        }
        posses.sort(Position::getDistance);
        return posses;
    }

    protected boolean needsTopSupport(final BlockPos pos) {
        return !canPlace(pos.up().up());
    }

    protected boolean valid(final BlockPos pos) {
        return (!liquids.GetSwitch() ? BlockUtil.getState(pos).equals(Blocks.AIR) : mc.world.getBlockState(pos).getMaterial().isReplaceable()) && mc.player.getDistanceSq(pos) / 2 <= placeRange.GetSlider() * 2;
    }

    protected boolean canPlace(final BlockPos pos) {
        return Arrays.stream(surroundings).map(pos::add).anyMatch(pos1 -> !mc.world.getBlockState(pos1).getMaterial().isReplaceable());
    }

    protected boolean containsNoEntities(final BlockPos pos) {
        final AxisAlignedBB bb = new AxisAlignedBB(pos);
        return mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal || entity instanceof EntityPlayer).noneMatch(entity -> Objects.requireNonNull(entity.getEntityBoundingBox()).intersects(bb));
    }

    protected class Position {
        protected BlockPos pos;

        public Position(final BlockPos pos) {
            this.pos = pos;
        }

        public int getDistance(Position position) {
            return (int) mc.player.getDistanceSq(position.getPos());
        }

        public BlockPos getPos() {
            return pos;
        }
    }

}
