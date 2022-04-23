package dev.zprestige.fire.util.impl;

import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockUtil implements Utils {

    public static EnumOffset getVisibleEnumFacing(final BlockPos pos) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            final BlockPos pos1 = pos.offset(enumFacing);
            for (int x = 0; x <= 10; x++) {
                final float xOffset = x / 10.0f;
                for (int y = 0; y <= 10; y++) {
                    final float yOffset = y / 10.0f;
                    for (int z = 0; z <= 10; z++) {
                        final float zOffset = z / 10.0f;
                        if (mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos1.getX() + xOffset, pos1.getY() + yOffset, pos.getZ() + zOffset), false, true, false) != null) {
                            return new EnumOffset(enumFacing, xOffset, yOffset, zOffset);
                        }
                    }
                }
            }
        }
        return null;
    }

    public static class EnumOffset {
        protected final EnumFacing enumFacing;
        protected final float x, y, z;

        public EnumOffset(final EnumFacing enumFacing, final float x, final float y, final float z) {
            this.enumFacing = enumFacing;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }
    }

    public static boolean isNotVisible(BlockPos position, double offset) {
        if (offset > 50 || offset < -50) {
            return false;
        }
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(position.getX() + 0.5, position.getY() + offset, position.getZ() + 0.5), false, true, false) != null;
    }

    public static float calculateEntityDamage(final EntityEnderCrystal crystal, final PlayerManager.Player player) {
        return calculatePosDamage(crystal.posX, crystal.posY, crystal.posZ, player.getEntityPlayer());
    }

    public static float calculateEntityDamage(final EntityEnderCrystal crystal, final EntityPlayer entityPlayer) {
        return calculatePosDamage(crystal.posX, crystal.posY, crystal.posZ, entityPlayer);
    }

    public static float calculatePosDamage(final BlockPos position, final EntityPlayer entityPlayer) {
        return calculatePosDamage(position.getX() + 0.5, position.getY() + 1.0, position.getZ() + 0.5, entityPlayer);
    }

    public static float calculatePosDamage(final BlockPos position, final PlayerManager.Player player) {
        return calculatePosDamage(position.getX() + 0.5, position.getY() + 1.0, position.getZ() + 0.5, player.getEntityPlayer());
    }

    @SuppressWarnings("ConstantConditions")
    public static float calculatePosDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleSize = 12.0F;
        final double size = entity.getDistance(posX, posY, posZ) / (double) doubleSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double value = (1.0D - size) * blockDensity;
        final float damage = (float) ((int) ((value * value + value) / 2.0D * 7.0D * (double) doubleSize + 1.0D));
        double finalDamage = 1.0D;

        if (entity instanceof EntityLivingBase) {
            finalDamage = getBlastReduction((EntityLivingBase) entity, getMultipliedDamage(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0F, false, true));
        }

        return (float) finalDamage;
    }

    private static float getMultipliedDamage(final float damage) {
        return damage * (mc.world.getDifficulty().getDifficultyId() == 0 ? 0.0F : (mc.world.getDifficulty().getDifficultyId() == 2 ? 1.0F : (mc.world.getDifficulty().getDifficultyId() == 1 ? 0.5F : 1.5F)));
    }

    public static float getBlastReduction(final EntityLivingBase entity, final float damageI, final Explosion explosion) {
        float damage = damageI;
        final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
        damage = CombatRules.getDamageAfterAbsorb(damage, entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        int k = 0;
        try {
            k = EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(), ds);
        } catch (Exception ignored) {
        }
        damage = damage * (1.0F - MathHelper.clamp(k, 0.0F, 20.0F) / 25.0F);

        if (entity.isPotionActive(MobEffects.RESISTANCE)) {
            damage = damage - (damage / 4);
        }

        return damage;
    }

    public static List<BlockPos> getCrystallableBlocks(final double range, final boolean onePointThirteen) {
        return getBlocksInRadius(range).stream().filter(pos -> canPosBeCrystalled(pos, onePointThirteen)).collect(Collectors.toList());
    }

    public static List<BlockPos> getBlocksInRadius(final double range) {
        final List<BlockPos> posses = new ArrayList<>();
        if (mc.player == null) {
            return posses;
        }
        final float xRange = Math.round(range);
        final float yRange = Math.round(range);
        final float zRange = Math.round(range);
        float x = -xRange;
        while (x <= xRange) {
            float y = -yRange;
            while (y <= yRange) {
                float z = -zRange;
                while (z <= zRange) {
                    final BlockPos position = mc.player.getPosition().add(x, y, z);
                    if (mc.player.getDistance(position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5) <= range) {
                        posses.add(position);
                    }
                    z++;
                }
                y++;
            }
            x++;
        }
        return posses;
    }

    public static boolean canPosBeCrystalled(final BlockPos pos, final boolean onePointThirteen) {
        return (getState(pos).equals(Blocks.OBSIDIAN) || getState(pos).equals(Blocks.BEDROCK)) && getState(pos.up()).equals(Blocks.AIR) && (getState(pos.up().up()).equals(Blocks.AIR) || onePointThirteen);
    }

    public static boolean canPosBeCrystalledSoon(final BlockPos pos, final boolean onePointThirteen) {
        return (getState(pos).equals(Blocks.OBSIDIAN) || getState(pos).equals(Blocks.BEDROCK)) && (getState(pos.up().up()).equals(Blocks.AIR) || onePointThirteen);
    }

    public static Vec3d getPositionEyes() {
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

    public static Block getState(final BlockPos pos) {
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
