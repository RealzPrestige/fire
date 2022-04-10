package dev.zprestige.fire.util.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.manager.PlayerManager;
import dev.zprestige.fire.util.Utils;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class EntityUtil implements Utils {

    public static float[] getSpeed(double speed) {
        float moveForward = mc.player.movementInput.moveForward;
        float moveStrafe = mc.player.movementInput.moveStrafe;
        float rotationYaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f)
                rotationYaw +=  (moveForward > 0.0f ? -45 : 45);
            else if (moveStrafe < 0.0f)
                rotationYaw +=  (moveForward > 0.0f ? 45 : -45);
            moveStrafe = 0.0f;
            if (moveForward > 0.0f)
                moveForward = 1.0f;
            else if (moveForward < 0.0f)
                moveForward = -1.0f;
        }
        final float posX = (float) (moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) +  moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw)));
        final float posZ = (float) (moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) -  moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw)));
        return new float[]{posX, posZ};
    }

    public static BlockPos getPlayerPos(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }
    public static boolean isMoving() {
        return mc.player.moveForward != 0.0 || mc.player.moveStrafing != 0.0 || mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown();
    }
    public static double getBaseMotionSpeed() {
        double event = 0.272;
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            int var3 = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            event *= 1.0 + 0.2 * var3;
        }
        return event;
    }

    public static double getMaxSpeed() {
        double maxModifier = 0.2873;
        if (mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(1)))) {
            maxModifier *= 1.0 + 0.2 * (Objects.requireNonNull(mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier() + 1);
        }
        return maxModifier;
    }
    public static PlayerManager.Player getClosestTarget(TargetPriority targetPriority, float range) {
        final TreeMap<Double, PlayerManager.Player> entityPlayerFloatTreeMap = new TreeMap<>();
        final TreeMap<Double, PlayerManager.Player> entityPlayerFloatTreeMap2 = new TreeMap<>();
        final TreeMap<Integer, Boolean> entityPlayerFloatTreeMap3 = new TreeMap<>();
        final TreeMap<Integer, Boolean> entityPlayerFloatTreeMap4 = new TreeMap<>();
        for (PlayerManager.Player player : Main.playerManager.getPlayers()) {
            if (player.getEntityPlayer().isDead || player.getEntityPlayer().equals(mc.player) || Main.friendManager.isFriend(player.getName()))
                continue;
            final double distance = player.getDistance();
            if (distance < range) {
                final int entityId = player.getEntityPlayer().entityId;
                entityPlayerFloatTreeMap.put(distance, player);
                entityPlayerFloatTreeMap2.put(player.getHealth(), player);
                entityPlayerFloatTreeMap3.put(entityId, BlockUtil.isPlayerSafe(player));
                final ICamera camera = new Frustum();
                final Entity renderViewEntity = mc.getRenderViewEntity();
                camera.setPosition(Objects.requireNonNull(renderViewEntity).posX, renderViewEntity.posY, renderViewEntity.posZ);
                entityPlayerFloatTreeMap4.put(entityId, camera.isBoundingBoxInFrustum(player.getBoundingBox()));
            }
        }
        switch (targetPriority) {
            case Range:
                if (!entityPlayerFloatTreeMap.isEmpty()) {
                    return entityPlayerFloatTreeMap.firstEntry().getValue();
                }
                break;
            case Health:
                if (!entityPlayerFloatTreeMap2.isEmpty()) {
                    return entityPlayerFloatTreeMap2.firstEntry().getValue();
                }
                break;
            case UnSafe:
                final TreeMap<Double, PlayerManager.Player> entityPlayerTreeMap = new TreeMap<>();
                for (Map.Entry<Integer, Boolean> entry : entityPlayerFloatTreeMap3.entrySet()) {
                    PlayerManager.Player player = Main.playerManager.getPlayerByEntityID(entry.getKey());
                    if (player != null) {
                        entityPlayerTreeMap.put(player.getDistance(), player);
                    }
                }
                if (!entityPlayerTreeMap.isEmpty()) {
                    return entityPlayerTreeMap.firstEntry().getValue();
                } else if (!entityPlayerFloatTreeMap.isEmpty()) {
                    return entityPlayerFloatTreeMap.firstEntry().getValue();
                }
                break;
            case Fov:
                final TreeMap<Double, PlayerManager.Player> entityPlayerTreeMap2 = new TreeMap<>();
                for (Map.Entry<Integer, Boolean> entry : entityPlayerFloatTreeMap4.entrySet()) {
                    PlayerManager.Player player = Main.playerManager.getPlayerByEntityID(entry.getKey());
                    if (player != null) {
                        entityPlayerTreeMap2.put(player.getDistance(), player);
                    }
                }

                if (!entityPlayerTreeMap2.isEmpty()) {
                    return entityPlayerTreeMap2.firstEntry().getValue();
                } else if (!entityPlayerFloatTreeMap.isEmpty()) {
                    return entityPlayerFloatTreeMap.firstEntry().getValue();
                }
                break;
        }
        return null;
    }

    public enum TargetPriority {
        Range,
        UnSafe,
        Health,
        Fov
    }

}
