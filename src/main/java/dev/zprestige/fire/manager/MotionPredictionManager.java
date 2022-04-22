package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MotionPredictionManager extends RegisteredClass {
    protected final Minecraft mc = Main.mc;
    protected ArrayList<Player> players = new ArrayList<>();

    public MotionPredictionManager(){
        super(true, false);
    }
    @RegisterListener
    public void onTick(final TickEvent  event){
        this.players = mc.world.playerEntities.stream().map(Player::new).collect(Collectors.toCollection(ArrayList::new));
    }

    public double[] getPredictedPosByPlayer(final EntityPlayer entityPlayer, final float factor){
        for (Player player : players) {
            if (player.getEntityPlayer().equals(entityPlayer)) {
                return player.correctNextTickPos(factor);
            }
        }
        return new double[]{entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ};
    }

    protected static class Player {
        protected final EntityPlayer entityPlayer;

        public Player(final EntityPlayer entityPlayer){
            this.entityPlayer = entityPlayer;
        }

        public EntityPlayer getEntityPlayer() {
            return entityPlayer;
        }

        public BlockPos flooredPos(){
            return EntityUtil.getPlayerPos(entityPlayer);
        }

        public boolean isOnGround(){
            final BlockPos flooredPos = flooredPos();
            return !BlockUtil.getState(flooredPos.down()).equals(Blocks.AIR) && entityPlayer.getEntityBoundingBox().intersects(new AxisAlignedBB(flooredPos).grow(0.01));
        }

        public double[] getNextTickPos(final float factor){
            double x = entityPlayer.posX + (entityPlayer.posX- entityPlayer.prevPosX) * factor;
            double y = entityPlayer.posY + (!isOnGround() ? (entityPlayer.posY - entityPlayer.prevPosY)  : 0.0);
            double z = entityPlayer.posZ + (entityPlayer.posZ- entityPlayer.prevPosZ) * factor;
            return new double[]{x, y, z};
        }

        public double[] correctNextTickPos(final float factor){
            final double[] next = getNextTickPos(factor);
            final BlockPos pos = new BlockPos(next[0], next[1], next[2]);
            return new double[]{next[0], next[1] + (!BlockUtil.getState(pos).equals(Blocks.AIR) ? IntStream.range(1, 5).filter(i -> !BlockUtil.getState(pos.up(i)).equals(Blocks.AIR)).asDoubleStream().sum() : 0), next[2]};
        }
    }

}
