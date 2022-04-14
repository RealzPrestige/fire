package dev.zprestige.fire.module.misc;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
@Descriptor(description = "Removes interpolation between ticks")
public class NoInterpolation extends Module {
    public final Slider delay = Menu.Slider("Delay", 1.0f, 1.0f, 20.0f);
    public final Switch sneak = Menu.Switch("Sneak", false);
    public final Switch cancelAnimations = Menu.Switch("Cancel Animations", false);
    protected ArrayList<NoInterpolatedPlayer> noInterpolatedPlayers = new ArrayList<>();
    protected int i;

    @RegisterListener
    public void onTick(final TickEvent event){
        i++;
    }
    @RegisterListener
    public void onFrame3D(final FrameEvent.FrameEvent3D event) {
        if (i >= delay.GetSlider()) {
            noInterpolatedPlayers.forEach(noInterpolatedPlayer -> {
                noInterpolatedPlayer.setPos();
                fixPos(noInterpolatedPlayer.getEntityPlayer());
            });
            i = 0;
        }
        mc.world.playerEntities.stream().filter(entityPlayer -> !entityPlayer.equals(mc.player) && !contains(entityPlayer)).forEach(entityPlayer -> noInterpolatedPlayers.add(new NoInterpolatedPlayer(entityPlayer)));
        new ArrayList<>(noInterpolatedPlayers).forEach(noInterpolatedPlayer -> {
            final EntityPlayer entityPlayer = noInterpolatedPlayer.getEntityPlayer();
            if (entityPlayer.isDead){
                noInterpolatedPlayers.remove(noInterpolatedPlayer);
                return;
            }
            noInterpolatedPlayer.updatePosition();
            fixPos(noInterpolatedPlayer.getEntityPlayer());
            if (sneak.GetSwitch()) {
                entityPlayer.setSneaking(true);
            }
            if (cancelAnimations.GetSwitch()) {
                cancelAnimations(entityPlayer);
            }
        });
    }

    protected void fixPos(final EntityPlayer entityPlayer){
        entityPlayer.prevPosX = entityPlayer.posX;
        entityPlayer.prevPosY = entityPlayer.posY;
        entityPlayer.prevPosZ = entityPlayer.posZ;
        entityPlayer.prevChasingPosX = entityPlayer.posX;
        entityPlayer.prevChasingPosY = entityPlayer.posY;
        entityPlayer.prevChasingPosZ = entityPlayer.posZ;
    }

    protected void cancelAnimations(final EntityPlayer entityPlayer){
        entityPlayer.limbSwing = 0;
        entityPlayer.limbSwingAmount = 0;
        entityPlayer.prevLimbSwingAmount = 0;
        entityPlayer.rotationPitch = 0;
        entityPlayer.prevRotationPitch = 0;
    }
    
    protected boolean contains(final EntityPlayer entityPlayer){
        return noInterpolatedPlayers.stream().anyMatch(noInterpolatedPlayer -> noInterpolatedPlayer.getEntityPlayer().equals(entityPlayer));
    }

    protected static class NoInterpolatedPlayer {
        protected final EntityPlayer entityPlayer;
        protected double x, y, z;

        public NoInterpolatedPlayer(final EntityPlayer entityPlayer) {
            this.entityPlayer = entityPlayer;
            setPos();
        }

        public void updatePosition() {
            entityPlayer.setPosition(x, y, z);
        }

        public void setPos() {
            this.x = entityPlayer.posX;
            this.y = entityPlayer.posY;
            this.z = entityPlayer.posZ;
        }

        public EntityPlayer getEntityPlayer() {
            return entityPlayer;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }
    }
}
