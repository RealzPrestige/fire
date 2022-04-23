package dev.zprestige.fire.module.combat.autocrystal;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.PacketEvent;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.SoundCategory;

import java.util.ConcurrentModificationException;
import java.util.Objects;

public class PacketReceiveListener extends EventListener<PacketEvent.PacketReceiveEvent, AutoCrystal> {

    public PacketReceiveListener(final AutoCrystal autoCrystal){
        super(PacketEvent.PacketReceiveEvent.class, autoCrystal);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketReceiveEvent event = (PacketEvent.PacketReceiveEvent) object;
        try {
            if (module.predict.GetCombo().equals("Normal") && event.getPacket() instanceof SPacketSpawnObject && ((SPacketSpawnObject) event.getPacket()).getType() == 51 && mc.world.getEntityByID(((SPacketSpawnObject) event.getPacket()).getEntityID()) instanceof EntityEnderCrystal) {
                final EntityEnderCrystal entityEnderCrystal = (EntityEnderCrystal) mc.world.getEntityByID(((SPacketSpawnObject) event.getPacket()).getEntityID());
                module.explodeCrystal(entityEnderCrystal, null);
            }
        } catch (ConcurrentModificationException ignored) {
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            try {
                if (packet.getCategory().equals(SoundCategory.BLOCKS) && packet.getSound().equals(SoundEvents.ENTITY_GENERIC_EXPLODE)) {
                    mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal && entity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= module.explodeRange.getValue()).forEach(entity -> {
                        if (module.setDead.GetCombo().equals("Safe")) {
                            Objects.requireNonNull(mc.world.getEntityByID(entity.getEntityId())).setDead();
                            mc.world.removeEntityFromWorld(entity.entityId);
                        }
                        if (module.attackedCrystals.contains(entity)) {
                            module.crystalsPerSecond.add(System.currentTimeMillis());
                        }
                    });
                }
            } catch (Exception ignored) {
            }
        }
    }
}
