package dev.zprestige.fire.module.player.freecam;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;

@Descriptor(description = "hacker")
public class Freecam extends Module {
    public final Slider speed = Menu.Slider("Speed", 1.0f, 0.1f, 10.0f);
    protected EntityOtherPlayerMP entity;
    protected float[] rots, pos;
    protected Entity ridingEntity;

    public Freecam() {
        eventListeners = new EventListener[]{
                new PacketSendListener(this),
                new TickListener(this)
        };
    }

    @Override
    public void onEnable() {
        final Entity entity = mc.player.getRidingEntity();
        if (entity != null) {
            ridingEntity = entity;
            mc.player.dismountRidingEntity();
        } else {
            ridingEntity = null;
        }
        rots = new float[]{
                mc.player.rotationYaw,
                mc.player.rotationPitch
        };
        pos = new float[]{
                (float) mc.player.posX,
                (float) mc.player.posY,
                (float) mc.player.posZ
        };
        spawnEntity();
    }

    @Override
    public void onDisable() {
        mc.player.rotationYaw = rots[0];
        mc.player.rotationPitch = rots[1];
        if (ridingEntity != null) {
            mc.player.startRiding(ridingEntity, true);
        }
        mc.player.setPosition(pos[0], pos[1], pos[2]);
        if (entity != null) {
            mc.world.removeEntity(entity);
        }
    }

    protected void spawnEntity() {
        final EntityOtherPlayerMP entity = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        entity.copyLocationAndAnglesFrom(mc.player);
        entity.rotationYawHead = mc.player.rotationYawHead;
        entity.prevRotationYawHead = mc.player.rotationYawHead;
        entity.rotationYaw = mc.player.rotationYaw;
        entity.prevRotationYaw = mc.player.rotationYaw;
        entity.rotationPitch = mc.player.rotationPitch;
        entity.prevRotationPitch = mc.player.rotationPitch;
        entity.cameraYaw = mc.player.rotationYaw;
        entity.cameraPitch = mc.player.rotationPitch;
        entity.limbSwing = mc.player.limbSwing;
        this.entity = entity;
    }


}