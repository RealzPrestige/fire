package dev.zprestige.fire.module.combat.autocrystal;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.PacketEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.BlockPos;

import java.util.ConcurrentModificationException;

public class PacketSendListener extends EventListener<PacketEvent.PacketSendEvent, AutoCrystal> {

    public PacketSendListener(final AutoCrystal autoCrystal) {
        super(PacketEvent.PacketSendEvent.class, autoCrystal);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketSendEvent event = (PacketEvent.PacketSendEvent) object;
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            if (module.predict.GetCombo().equals("Ultra") || module.predict.GetCombo().equals("UltraChain")) {
                final BlockPos eventPos = ((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getPos();
                EntityEnderCrystal entityEnderCrystal = null;
                try {
                    for (Entity entity : mc.world.loadedEntityList) {
                        if (entity instanceof EntityEnderCrystal && entity.getDistanceSq(eventPos.up()) < 1.0f) {
                            entityEnderCrystal = (EntityEnderCrystal) entity;
                        }
                    }
                } catch (ConcurrentModificationException ignored) {
                }
                if (entityEnderCrystal != null) {
                    module.explodeCrystal(entityEnderCrystal, null);
                    if (module.predict.GetCombo().equals("UltraChain")) {
                        module.placeCrystal(eventPos, null);
                    }
                }
            }
        }
    }
}
