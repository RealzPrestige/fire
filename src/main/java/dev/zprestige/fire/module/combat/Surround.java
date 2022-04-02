package dev.zprestige.fire.module.combat;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Surround extends Module {
    public final ComboBox mode = Menu.ComboBox("Mode", "Instant", new String[]{
            "Instant",
            "Tick"
    });
    public final Slider blocksPerTick = Menu.Slider("Blocks Per Tick", 10.0f, 1.0f, 20.0f).visibility(z -> mode.GetCombo().equals("Instant"));
    public final ComboBox item = Menu.ComboBox("Item", "Fallback", new String[]{
            "Fallback",
            "Obsidian",
            "Echest",
            "Webs (OP)"
    });
    public final Switch smartPriority = Menu.Switch("Smart Priority", true);
    public final Switch onePointThirteen = Menu.Switch("One Point Thirteen", true).visibility(z -> smartPriority.GetSwitch());
    public final Switch multiTask = Menu.Switch("Multi Task", true);
    public final Switch extend = Menu.Switch("Extend", true);
    public final Switch packet = Menu.Switch("Packet", true);
    public final Switch center = Menu.Switch("Center", false);
    public final Switch rotate = Menu.Switch("Rotate", false);
    public final Switch preventRotationRubberband = Menu.Switch("Prevent Rotation Rubberband", false).visibility(z -> rotate.GetSwitch());
    public final Switch strict = Menu.Switch("Strict", false);
    public final Switch render = Menu.Switch("Render", false);
    public final Slider fadeSpeed = Menu.Slider("Fade Speed", 25.0f, 0.1f, 100.0f).visibility(z -> render.GetSwitch());
    public final Switch box = Menu.Switch("Box", false).visibility(z -> render.GetSwitch());
    public final ColorBox boxColor = Menu.Color("Box Color", new Color(255, 255, 255, 120)).visibility(z -> render.GetSwitch() && box.GetSwitch());
    public final Switch outline = Menu.Switch("Outline", false).visibility(z -> render.GetSwitch());
    public final ColorBox outlineColor = Menu.Color("Outline Color", new Color(255, 255, 255, 255)).visibility(z -> render.GetSwitch() && outline.GetSwitch());
    public final Slider outlineWidth = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f).visibility(z -> render.GetSwitch() && outline.GetSwitch());
    protected BlockPos lastPos = null;
    protected final Vec3i[] offsets = new Vec3i[]{
            new Vec3i(0, -1, 0),
            new Vec3i(0, -1, -1),
            new Vec3i(0, -1, 1),
            new Vec3i(-1, -1, 0),
            new Vec3i(1, -1, 0),
            new Vec3i(0, 0, -1),
            new Vec3i(0, 0, 1),
            new Vec3i(-1, 0, 0),
            new Vec3i(1, 0, 0)
    };

    @Override
    public void onDisable() {
        lastPos = null;
    }

    @Override
    public void onEnable() {
        if (center.GetSwitch()) {
            moveToCenter();
        }
    }

    protected Vec3d getCenter(double posX, double posY, double posZ) {
        double x = Math.floor(posX) + 0.5;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5;
        return new Vec3d(x, y, z);
    }

    protected void moveToCenter() {
        if (mc.player.onGround) {
            final Vec3d center = getCenter(mc.player.posX, mc.player.posY, mc.player.posZ);
            if (mc.player.getDistanceSq(new BlockPos(center.x, center.y, center.z)) > 0.1f) {
                mc.player.setPosition(center.x, center.y, center.z);
            }
        }
    }

    @RegisterListener
    public void onTick(final TickEvent event) {
        final BlockPos pos = BlockUtil.getPosition();
        if (!mc.player.onGround || mc.player.stepHeight > 0.6f || (lastPos != null && pos.getY() > lastPos.getY())) {
            disableModule();
            return;
        }
        if (!multiTask.GetSwitch() && mc.player.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            return;
        }
        lastPos = pos;
        int blocks = 0;
        switch (mode.GetCombo()) {
            case "Instant":
                ArrayList<Position> offs = getOffsets(pos);
                if (smartPriority.GetSwitch()){
                    offs = offs.stream().sorted(Comparator.comparing(Position::getPriority).reversed()).collect(Collectors.toCollection(ArrayList::new));
                }
                for (final Position position : offs) {
                    if (blocks > blocksPerTick.GetSlider() || (preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations())) {
                        return;
                    }
                    final BlockPos pos1 = position.getPos();
                    if (mc.world.getBlockState(pos1).getMaterial().isReplaceable() && (!extend.GetSwitch() || !mc.player.getEntityBoundingBox().intersects(new AxisAlignedBB(pos1))) && isntIntersectingWithPlayer(pos1)) {
                        final int slot = getSlotByItem();
                        if (slot != -1) {
                            Main.interactionManager.placeBlockWithSwitch(pos1, rotate.GetSwitch(), packet.GetSwitch(), strict.GetSwitch(), slot);
                            addFade(pos1);
                            blocks++;
                        } else {
                            disableModule();
                        }
                    }
                }
                if (extend.GetSwitch()) {
                    ArrayList<Position> positions = extendedPosses();
                    if (smartPriority.GetSwitch()){
                        positions = positions.stream().sorted(Comparator.comparing(Position::getPriority).reversed()).collect(Collectors.toCollection(ArrayList::new));
                    }
                    for (final Position position : positions) {
                        if (blocks > blocksPerTick.GetSlider() || (preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations())) {
                            return;
                        }
                        final BlockPos pos1 = position.getPos();
                        if (mc.world.getBlockState(pos1).getMaterial().isReplaceable() && !mc.player.getEntityBoundingBox().intersects(new AxisAlignedBB(pos1)) && isntIntersectingWithPlayer(pos1)) {
                            final int slot = getSlotByItem();
                            if (slot != -1) {
                                if ((preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations())) {
                                    return;
                                }
                                Main.interactionManager.placeBlockWithSwitch(pos1, rotate.GetSwitch(), packet.GetSwitch(), strict.GetSwitch(), slot);
                                addFade(pos1);
                                blocks++;
                            } else {
                                disableModule();
                            }

                        }
                    }
                }
                break;
            case "Tick":
                ArrayList<Position> offs1 = getOffsets(pos);
                if (smartPriority.GetSwitch()){
                    offs1 = offs1.stream().sorted(Comparator.comparing(Position::getPriority).reversed()).collect(Collectors.toCollection(ArrayList::new));
                }
                for (final Position position : offs1) {
                    if (preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations()) {
                        return;
                    }
                    final BlockPos pos1 = position.getPos();
                    if (mc.world.getBlockState(pos1).getMaterial().isReplaceable() && (!extend.GetSwitch() || !mc.player.getEntityBoundingBox().intersects(new AxisAlignedBB(pos1))) && isntIntersectingWithPlayer(pos1)) {
                        final int slot = getSlotByItem();
                        if (slot != -1) {
                            Main.interactionManager.placeBlockWithSwitch(pos1, rotate.GetSwitch(), packet.GetSwitch(), strict.GetSwitch(), slot);
                            addFade(pos1);
                            return;
                        } else {
                            disableModule();
                        }
                    }
                }
                if (extend.GetSwitch()) {
                    ArrayList<Position> positions = extendedPosses();
                    if (smartPriority.GetSwitch()){
                        positions = positions.stream().sorted(Comparator.comparing(Position::getPriority).reversed()).collect(Collectors.toCollection(ArrayList::new));
                    }
                    for (final Position position : positions) {
                        if (preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations()) {
                            return;
                        }
                        final BlockPos pos1 = position.getPos();
                        if (mc.world.getBlockState(pos1).getMaterial().isReplaceable() && !mc.player.getEntityBoundingBox().intersects(new AxisAlignedBB(pos1)) && isntIntersectingWithPlayer(pos1)) {
                            final int slot = getSlotByItem();
                            if (slot != -1) {
                                Main.interactionManager.placeBlockWithSwitch(pos1, rotate.GetSwitch(), packet.GetSwitch(), strict.GetSwitch(), slot);
                                addFade(pos1);
                                return;
                            } else {
                                disableModule();
                            }
                        }
                    }
                }
                break;
        }
    }

    protected boolean isntIntersectingWithPlayer(final BlockPos pos) {
        return mc.world.playerEntities.stream().noneMatch(entityPlayer -> entityPlayer.getEntityBoundingBox().shrink(0.1).intersects(new AxisAlignedBB(pos)));
    }

    protected void addFade(final BlockPos pos) {
        if (render.GetSwitch()) {
            Main.fadeManager.createFadePosition(pos, boxColor.GetColor(), outlineColor.GetColor(), box.GetSwitch(), outline.GetSwitch(), outlineWidth.GetSlider(), fadeSpeed.GetSlider(), boxColor.GetColor().getAlpha());
        }
    }

    protected ArrayList<Position> getOffsets(final BlockPos pos){
        final ArrayList<Position> positions = new ArrayList<>();
        for (final Vec3i vec3i : offsets){
            final BlockPos pos1 = pos.add(vec3i);
            final boolean crystallable = BlockUtil.canPosBeCrystalled(pos1.down(), onePointThirteen.GetSwitch());
            positions.add(new Position(pos1, crystallable ? Priority.HIGH : Priority.NORMAL));
        }
        return positions;
    }

    protected ArrayList<Position> extendedPosses() {
        final BlockPos pos = BlockUtil.getPosition();
        final ArrayList<Position> extensions = new ArrayList<>();
        Arrays.stream(offsets).map(pos::add).filter(pos1 -> mc.world.getBlockState(pos1).getMaterial().isReplaceable()).forEach(pos1 -> {
            final AxisAlignedBB bb = new AxisAlignedBB(pos1);
            if (mc.player.getEntityBoundingBox().intersects(bb) && BlockUtil.getState(pos1).equals(Blocks.AIR)) {
                Arrays.stream(offsets).map(pos1::add).forEach(pos2 -> {
                    final boolean crystallable = BlockUtil.canPosBeCrystalled(pos2.down(), onePointThirteen.GetSwitch());
                    extensions.add(new Position(pos2, crystallable ? Priority.HIGH : Priority.NORMAL));
                });
            }
        });
        return extensions;
    }

    protected int getSlotByItem() {
        final int slot = Main.inventoryManager.getBlockFromHotbar(Blocks.OBSIDIAN);
        final int slot2 = Main.inventoryManager.getBlockFromHotbar(Blocks.ENDER_CHEST);
        final int slot3 = Main.inventoryManager.getBlockFromHotbar(Blocks.WEB);
        switch (item.GetCombo()) {
            case "Fallback":
                if (slot != -1) {
                    return slot;
                } else {
                    if (slot2 != -1) {
                        return slot2;
                    }
                }
                break;
            case "Obsidian":
                if (slot != -1) {
                    return slot;
                }
                break;
            case "Echest":
                if (slot2 != -1) {
                    return slot2;
                }
                break;
            case "Webs (OP)":
                if (slot3 != -1) {
                    return slot3;
                }
                break;
        }
        return -1;
    }

    protected static class Position {
        protected static BlockPos pos;
        protected static Priority priority;

        public Position(final BlockPos pos, Priority priority) {
            Position.pos = pos;
            Position.priority = priority;
        }

        public BlockPos getPos() {
            return pos;
        }

        public static int getPriority(final Position position) {
            return priority.getPriority();
        }
    }

    protected enum Priority {
        HIGH(1000),
        NORMAL(0);

        private final int priority;

        Priority(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return this.priority;
        }
    }
}
