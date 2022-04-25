package dev.zprestige.fire.manager.interactionmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class InteractionManager {
    protected final Minecraft mc = Main.mc;

    public final List<Block> sneakBlocks = Arrays.asList(
            Blocks.ENDER_CHEST,
            Blocks.ANVIL,
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.CRAFTING_TABLE,
            Blocks.BREWING_STAND,
            Blocks.HOPPER,
            Blocks.DROPPER,
            Blocks.DISPENSER,
            Blocks.TRAPDOOR,
            Blocks.ENCHANTING_TABLE
    );

    public final List<Block> shulkers = Arrays.asList(
            Blocks.WHITE_SHULKER_BOX,
            Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX,
            Blocks.LIGHT_BLUE_SHULKER_BOX,
            Blocks.YELLOW_SHULKER_BOX,
            Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX,
            Blocks.SILVER_SHULKER_BOX,
            Blocks.CYAN_SHULKER_BOX,
            Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX,
            Blocks.BROWN_SHULKER_BOX,
            Blocks.GREEN_SHULKER_BOX,
            Blocks.RED_SHULKER_BOX,
            Blocks.BLACK_SHULKER_BOX
    );

    public void placeBlock(final BlockPos pos, final boolean rotate, final boolean packet, final boolean strict) {
        placeBlock(pos, rotate, packet, strict, false, false);
    }

    public void placeBlock(final BlockPos pos, final boolean rotate, final boolean packet, final boolean strict, final boolean excludeBottomEnumFacing, final boolean ignoreEntities) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            final BlockPos directionOffset = pos.offset(enumFacing);
            if (excludeBottomEnumFacing && directionOffset.equals(pos.down())) {
                continue;
            }
            if (!ignoreEntities) {
                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
                    if (!(entity instanceof EntityItem || entity instanceof EntityEnderCrystal)) {
                        return;
                    }
                }
            }
            if (strict && !getVisibleSides(directionOffset).contains(enumFacing.getOpposite()) || mc.world.getBlockState(directionOffset).getMaterial().isReplaceable()) {
                continue;
            }
            final boolean sprint = mc.player.isSprinting();
            if (sprint) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                mc.player.setSprinting(false);
            }
            final Block block = mc.world.getBlockState(directionOffset).getBlock();
            boolean sneak = sneakBlocks.contains(block) || shulkers.contains(block) && !mc.player.isSneaking();
            if (excludeBottomEnumFacing) {
                sneak = false;
            }
            if (sneak) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.player.setSneaking(true);
            }
            final Vec3d vec = new Vec3d(directionOffset).addVector(0.5, 0.5 + (excludeBottomEnumFacing ? 1 : 0.0), 0.5).add(new Vec3d(enumFacing.getOpposite().getDirectionVec()).scale(0.5));
            if (rotate) {
                final double[] rotations = BlockUtil.calculateAngle(vec);
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation((float) rotations[0], (float) rotations[1], mc.player.onGround));
            }
            EnumActionResult clickBlock = null;
            if (packet) {
                final EnumFacing facing = getFirstEnumFacing(pos);
                final EnumFacing opposite = facing.getOpposite();
                final BlockPos closest = pos.offset(facing);
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(closest, opposite, EnumHand.MAIN_HAND, (float) (vec.x - closest.getX()), (float) (vec.y - closest.getY()), (float) (vec.z - closest.getZ())));
            } else {
                clickBlock = mc.playerController.processRightClickBlock(mc.player, mc.world, directionOffset, enumFacing.getOpposite(), vec, EnumHand.MAIN_HAND);
            }
            if (packet || clickBlock != EnumActionResult.FAIL) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.rightClickDelayTimer = 4;
                return;
            }
            if (sneak) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                mc.player.setSneaking(false);
            }
            if (sprint) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                mc.player.setSprinting(true);
            }
        }
    }

    public EnumFacing getFirstEnumFacing(final BlockPos pos) {
        return getEnumFacingSides(pos).stream().findFirst().orElse(null);
    }

    public ArrayList<EnumFacing> getEnumFacingSides(final BlockPos pos) {
        final ArrayList<EnumFacing> sides = new ArrayList<>();
        Arrays.stream(EnumFacing.values()).forEach(side -> {
            final BlockPos pos1 = pos.offset(side);
            if (mc.world.getBlockState(pos1).getBlock().canCollideCheck(mc.world.getBlockState(pos1), false)) {
                if (!mc.world.getBlockState(pos1).getMaterial().isReplaceable()) {
                    sides.add(side);
                }
            }
        });
        return sides;
    }

    public ArrayList<EnumFacing> getVisibleSides(final BlockPos pos) {
        final ArrayList<EnumFacing> sides = new ArrayList<>();
        final Vec3d vec = new Vec3d(pos).addVector(0.5, 0.5, 0.5);
        final Vec3d eye = mc.player.getPositionEyes(1);
        final double facingX = eye.x - vec.x;
        final double facingY = eye.y - vec.y;
        final double facingZ = eye.z - vec.z;
        if (facingX < -0.5) {
            sides.add(EnumFacing.WEST);
        } else if (facingX > 0.5) {
            sides.add(EnumFacing.EAST);
        } else if (isVisible(pos)) {
            sides.add(EnumFacing.WEST);
            sides.add(EnumFacing.EAST);
        }
        if (facingY < -0.5) {
            sides.add(EnumFacing.DOWN);
        } else if (facingY > 0.5) {
            sides.add(EnumFacing.UP);
        } else {
            sides.add(EnumFacing.DOWN);
            sides.add(EnumFacing.UP);
        }
        if (facingZ < -0.5) {
            sides.add(EnumFacing.NORTH);
        } else if (facingZ > 0.5) {
            sides.add(EnumFacing.SOUTH);
        } else if (isVisible(pos)) {
            sides.add(EnumFacing.NORTH);
            sides.add(EnumFacing.SOUTH);
        }
        return sides;
    }

    protected boolean isVisible(final BlockPos pos) {
        return !mc.world.getBlockState(pos).isFullBlock() || !mc.world.isAirBlock(pos);
    }

    public void placeBlockWithSwitch(final BlockPos pos, final boolean rotate, final boolean packet, final boolean strict, final int slot) {
        int currentItem = mc.player.inventory.currentItem;
        Main.inventoryManager.switchToSlot(slot);
        placeBlock(pos, rotate, packet, strict);
        mc.player.inventory.currentItem = currentItem;
        mc.playerController.updateController();
    }

    public void placeBlockWithSwitchIgnoringEntities(final BlockPos pos, final boolean rotate, final boolean packet, final boolean strict, final int slot) {
        int currentItem = mc.player.inventory.currentItem;
        Main.inventoryManager.switchToSlot(slot);
        placeBlock(pos, rotate, packet, strict, false, true);
        mc.player.inventory.currentItem = currentItem;
        mc.playerController.updateController();
    }

    public void placeBlock(final BlockPos pos, final boolean rotate, final boolean packet, final boolean strict, final Timer timer) {
        placeBlock(pos, rotate, packet, strict);
        timer.syncTime();
    }

    public void placeBlockWithSwitch(final BlockPos pos, final boolean rotate, final boolean packet, final boolean strict, final int slot, final Timer timer) {
        placeBlockWithSwitch(pos, rotate, packet, strict, slot);
        timer.syncTime();
    }

    public void interactBlock(final BlockPos pos, final EnumFacing enumFacing) {
        mc.playerController.onPlayerDamageBlock(pos, enumFacing);
    }

    public EnumFacing closestEnumFacing(final BlockPos pos) {
        final TreeMap<Double, EnumFacing> enumFacingTreeMap = Main.interactionManager.getVisibleSides(pos).stream().collect(Collectors.toMap(enumFacing -> getDistanceToFace(pos, enumFacing), enumFacing -> enumFacing, (a, b) -> b, TreeMap::new));
        return enumFacingTreeMap.firstEntry().getValue();
    }

    protected double getDistanceToFace(final BlockPos pos, final EnumFacing enumFacing) {
        Vec3i vec3i = new Vec3i(0.5, 0.5, 0.5);
        switch (enumFacing) {
            case NORTH:
                vec3i = new Vec3i(0.5, 0.5, -1.5);
                break;
            case EAST:
                vec3i = new Vec3i(1.5, 0.5, 0.5);
                break;
            case SOUTH:
                vec3i = new Vec3i(0.5, 0.5, 1.5);
                break;
            case WEST:
                vec3i = new Vec3i(-1.5, 0.5, 0.5);
                break;
            case UP:
                vec3i = new Vec3i(0.5, 1.5, 0.5);
                break;
            case DOWN:
                vec3i = new Vec3i(0.5, -1.5, 0.5);
                break;
        }
        return mc.player.getDistanceSq(pos.add(vec3i));
    }

    public void attemptBreak(final BlockPos pos, final EnumFacing enumFacing) {
        final int slot = Main.inventoryManager.getItemFromHotbar(Items.DIAMOND_PICKAXE);
        if (slot != -1) {
            if (mc.player != null && mc.player.connection != null) {
                final int currentItem = mc.player.inventory.currentItem;
                Main.inventoryManager.switchToSlot(slot);
                try {
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, enumFacing));
                } catch (Exception ignored){
                }
                Main.inventoryManager.switchBack(currentItem);
            }
        }
    }

    public void initiateBreaking(final BlockPos pos, final EnumFacing enumFacing, final boolean swing) {
        if (mc.player.connection != null) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, enumFacing));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, enumFacing));
        }
        if (swing) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public void abort(final BlockPos pos, final EnumFacing enumFacing) {
        if (mc.player.connection != null) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, enumFacing));
        }
        mc.playerController.isHittingBlock = false;
        mc.playerController.curBlockDamageMP = 0.0f;
        mc.world.sendBlockBreakProgress(mc.player.getEntityId(), pos, -1);
        mc.player.resetCooldown();
    }

}
