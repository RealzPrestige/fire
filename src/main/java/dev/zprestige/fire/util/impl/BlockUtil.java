package dev.zprestige.fire.util.impl;

import dev.zprestige.fire.manager.PlayerManager;
import dev.zprestige.fire.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BlockUtil implements Utils {

    public static Vec3d getPositionEyes(){
        return mc.player.getPositionEyes(1);
    }

    public static double[] calculateAngle(Vec3d to) {
        final Vec3d eye = getPositionEyes();
        double yaw = Math.toDegrees(Math.atan2(to.subtract(eye).z, to.subtract(eye).x)) - 90;
        double pitch = Math.toDegrees(-Math.atan2(to.subtract(eye).y, Math.hypot(to.subtract(eye).x, to.subtract(eye).z)));
        return new double[]{MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }

    public static BlockPos getPosition() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static Block getState(final BlockPos pos){
        return mc.world.getBlockState(pos).getBlock();
    }

    public static boolean isPlayerSafe(final PlayerManager.Player player) {
        final BlockPos pos = player.getPosition();
        if (isNotIntersecting(player)) {
            return isBedrockOrObsidianOrEchest(pos.north()) && isBedrockOrObsidianOrEchest(pos.east()) && isBedrockOrObsidianOrEchest(pos.south()) && isBedrockOrObsidianOrEchest(pos.west()) && isBedrockOrObsidianOrEchest(pos.down());
        } else {
            return isIntersectingSafe(player);
        }
    }

    public static boolean isNotIntersecting(final PlayerManager.Player player) {
        final BlockPos pos = player.getPosition();
        final AxisAlignedBB bb = player.getBoundingBox();
        return (!air(pos.north()) || !bb.intersects(new AxisAlignedBB(pos.north()))) && (!air(pos.east()) || !bb.intersects(new AxisAlignedBB(pos.east()))) && (!air(pos.south()) || !bb.intersects(new AxisAlignedBB(pos.south()))) && (!air(pos.west()) || !bb.intersects(new AxisAlignedBB(pos.west())));
    }

    public static boolean isIntersectingSafe(final PlayerManager.Player player) {
        final BlockPos pos = player.getPosition();
        final AxisAlignedBB bb = player.getBoundingBox();
        if (air(pos.north()) && bb.intersects(new AxisAlignedBB(pos.north()))) {
            final BlockPos pos1 = pos.north();
            if (!isBedrockOrObsidianOrEchest(pos1.north()) || !isBedrockOrObsidianOrEchest(pos1.east()) || !isBedrockOrObsidianOrEchest(pos1.west()) || !isBedrockOrObsidianOrEchest(pos1.down()))
                return false;
        }
        if (air(pos.east()) && bb.intersects(new AxisAlignedBB(pos.east()))) {
            final BlockPos pos1 = pos.east();
            if (!isBedrockOrObsidianOrEchest(pos1.north()) || !isBedrockOrObsidianOrEchest(pos1.east()) || !isBedrockOrObsidianOrEchest(pos1.south()) || !isBedrockOrObsidianOrEchest(pos1.down()))
                return false;
        }
        if (air(pos.south()) && bb.intersects(new AxisAlignedBB(pos.south()))) {
            final BlockPos pos1 = pos.south();
            if (!isBedrockOrObsidianOrEchest(pos1.east()) || !isBedrockOrObsidianOrEchest(pos1.south()) || !isBedrockOrObsidianOrEchest(pos1.west()) || !isBedrockOrObsidianOrEchest(pos1.down()))
                return false;
        }
        if (air(pos.west()) && bb.intersects(new AxisAlignedBB(pos.west()))) {
            final BlockPos pos1 = pos.west();
            return isBedrockOrObsidianOrEchest(pos1.north()) && isBedrockOrObsidianOrEchest(pos1.south()) && isBedrockOrObsidianOrEchest(pos1.west()) && isBedrockOrObsidianOrEchest(pos1.down());
        }
        return true;
    }

    public static boolean isBedrockOrObsidianOrEchest(final BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(pos).getBlock().equals(Blocks.ENDER_CHEST);
    }

    public static boolean air(final BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR);
    }

}
