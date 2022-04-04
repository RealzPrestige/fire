package dev.zprestige.fire.module.player;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.PacketEvent;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class Criticals extends Module {
    public final Slider offset = Menu.Slider("Offset", 0.1f, 0.1f, 1.0f);
    public final Switch allowMoving = Menu.Switch("Allow Moving", false);

    @RegisterListener
    public void onPacketSend(final PacketEvent.PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction().equals(CPacketUseEntity.Action.ATTACK) && packet.getEntityFromWorld(mc.world) instanceof EntityLivingBase && mc.player.onGround && !mc.player.isInWater() && !mc.player.isInLava() && !mc.player.isInWeb) {
                if ((!allowMoving.GetSwitch() && EntityUtil.isMoving())) {
                    return;
                }
                sendPacket(offset.GetSlider());
                sendPacket(0.0f);
            }
        }
    }

    protected void sendPacket(final float offset){
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset, mc.player.posZ, false));
    }
}
