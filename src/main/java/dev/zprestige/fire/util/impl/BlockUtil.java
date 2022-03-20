package dev.zprestige.fire.util.impl;

import dev.zprestige.fire.util.Utils;
import net.minecraft.block.Block;
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
}
