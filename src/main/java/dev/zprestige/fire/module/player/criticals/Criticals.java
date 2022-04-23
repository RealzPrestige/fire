package dev.zprestige.fire.module.player.criticals;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

@Descriptor(description = "Turns hits into critical hits")
public class Criticals extends Module {
    public final Slider offset = Menu.Slider("Offset", 0.1f, 0.1f, 1.0f);
    public final Switch allowMoving = Menu.Switch("Allow Moving", false);

    public Criticals() {
        eventListeners = new EventListener[]{
                new PacketSendListener(this)
        };
    }

    protected void sendPacket(final float offset) {
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset, mc.player.posZ, false));
    }
}
