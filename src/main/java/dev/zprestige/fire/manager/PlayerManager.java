package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlayerManager extends RegisteredClass {
    protected final Minecraft mc = Main.mc;
    protected ArrayList<Player> players = new ArrayList<>();

    @RegisterListener
    protected void onTick(TickEvent tickEvent) {
        Main.threadManager.run(() -> players = mc.world.playerEntities.stream().map(Player::new).collect(Collectors.toCollection(ArrayList::new)));
    }

    public Player getPlayerByEntityID(final int entityId){
        return players.stream().filter(player -> player.getEntityPlayer().entityId == entityId).findFirst().orElse(null);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public class Player {
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

        public double[] getCoordinates(){
            return new double[]{Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY), Math.floor(entityPlayer.posZ)};
        }

        public double getHealth() {
            return entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount();
        }

        public double getDistance() {
            return mc.player.getDistance(entityPlayer);
        }

        public double getDistanceToPos(final BlockPos pos){
            return entityPlayer.getDistanceSq(pos);
        }

        public String getName(){
            return entityPlayer.getName();
        }

        public AxisAlignedBB getBoundingBox(){
            return entityPlayer.getEntityBoundingBox();
        }
    }
}
