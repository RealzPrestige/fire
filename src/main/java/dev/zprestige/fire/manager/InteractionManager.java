package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        for (EnumFacing direction : EnumFacing.values()) {
            final BlockPos directionOffset = pos.offset(direction);
            for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
                if (!(entity instanceof EntityItem)) {
                    return;
                }
            }
            if (strict && !getVisibleSides(directionOffset).contains(direction.getOpposite()) || mc.world.getBlockState(directionOffset).getMaterial().isReplaceable()) {
                continue;
            }
            final boolean sprint = mc.player.isSprinting();
            if (sprint) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                mc.player.setSprinting(false);
            }
            final Block block = mc.world.getBlockState(directionOffset).getBlock();
            final boolean sneak = sneakBlocks.contains(block) || shulkers.contains(block) && !mc.player.isSneaking();
            if (sneak) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.player.setSneaking(true);
            }
            final Vec3d vec = new Vec3d(directionOffset).addVector(0.5, 0.5, 0.5).add(new Vec3d(direction.getOpposite().getDirectionVec()).scale(0.5));
            if (rotate) {
                final double[] rotations = BlockUtil.calculateAngle(vec);
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation((float) rotations[0], (float) rotations[1], mc.player.onGround));
            }
            if (sneak) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                mc.player.setSneaking(false);
            }
            if (sprint) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                mc.player.setSprinting(true);
            }
            EnumActionResult clickBlock = null;

            if (packet){
                float f = (float) (vec.x - (double) pos.getX());
                float f1 = (float) (vec.y - (double) pos.getY());
                float f2 = (float) (vec.z - (double) pos.getZ());
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, EnumHand.MAIN_HAND, f, f1, f2));
            } else {
                clickBlock = mc.playerController.processRightClickBlock(mc.player, mc.world, directionOffset, direction.getOpposite(), vec, EnumHand.MAIN_HAND);
            }
            if (packet || clickBlock != EnumActionResult.FAIL) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.rightClickDelayTimer = 4;
                return;
            }
        }
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

    protected boolean isVisible(final BlockPos pos){
        return !mc.world.getBlockState(pos).isFullBlock() || !mc.world.isAirBlock(pos);
    }

    public void placeBlockWithSwitch(final BlockPos pos, final boolean rotate, final boolean packet, final boolean strict, final int slot){
        int currentItem = mc.player.inventory.currentItem;
        Main.inventoryManager.switchToSlot(slot);
        placeBlock(pos, rotate, packet, strict);
        mc.player.inventory.currentItem = currentItem;
        mc.playerController.updateController();
    }

    public void placeBlock(final BlockPos pos, final boolean rotate, final boolean packet, final boolean strict, final Timer timer){
        placeBlock(pos, rotate, packet, strict);
        timer.syncTime();
    }

    public void placeBlockWithSwitch(final BlockPos pos, final boolean rotate, final boolean packet, final boolean strict, final int slot, final Timer timer){
        placeBlockWithSwitch(pos, rotate, packet, strict, slot);
        timer.syncTime();
    }
}
