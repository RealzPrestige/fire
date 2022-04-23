package dev.zprestige.fire.module.player.criticals;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.PacketEvent;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketUseEntity;

public class PacketSendListener extends EventListener<PacketEvent.PacketSendEvent, Criticals> {

    public PacketSendListener(final Criticals criticals) {
        super(PacketEvent.PacketSendEvent.class, criticals);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketSendEvent event = (PacketEvent.PacketSendEvent) object;
        if (event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction().equals(CPacketUseEntity.Action.ATTACK) && packet.getEntityFromWorld(mc.world) instanceof EntityLivingBase && mc.player.onGround && !mc.player.isInWater() && !mc.player.isInLava() && !mc.player.isInWeb) {
                if ((!module.allowMoving.GetSwitch() && EntityUtil.isMoving())) {
                    return;
                }
                module.sendPacket(module.offset.GetSlider());
                module.sendPacket(0.0f);
            }
        }
    }
}
