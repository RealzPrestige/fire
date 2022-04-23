package dev.zprestige.fire.manager.playermanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    protected static final Minecraft mc = Main.mc;
    protected List<Player> players = new ArrayList<>();

    public PlayerManager() {
        Main.newBus.registerListeners(new EventListener[]{
                new TickListener()
        });
    }

    public Player getPlayerByEntityID(final int entityId) {
        return players.stream().filter(player -> player.getEntityPlayer().entityId == entityId).findFirst().orElse(null);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public static class Player {
        protected final EntityPlayer entityPlayer;

        public Player(EntityPlayer entityPlayer) {
            this.entityPlayer = entityPlayer;
        }

        public EntityPlayer getEntityPlayer() {
            return entityPlayer;
        }

        public BlockPos getPosition() {
            return new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY), Math.floor(entityPlayer.posZ));
        }

        public double getHealth() {
            return entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount();
        }

        public double getDistance() {
            return mc.player.getDistance(entityPlayer);
        }

        public double getDistanceToPos(final BlockPos pos) {
            return entityPlayer.getDistanceSq(pos);
        }

        public String getName() {
            return entityPlayer.getName();
        }

        public AxisAlignedBB getBoundingBox() {
            return entityPlayer.getEntityBoundingBox();
        }
    }
}
