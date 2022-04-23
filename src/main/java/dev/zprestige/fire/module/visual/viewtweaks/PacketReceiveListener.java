package dev.zprestige.fire.module.visual.viewtweaks;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.PacketEvent;
import net.minecraft.network.play.server.SPacketEffect;

public class PacketReceiveListener extends EventListener<PacketEvent.PacketReceiveEvent, ViewTweaks> {

    public PacketReceiveListener(final ViewTweaks viewTweaks) {
        super(PacketEvent.PacketReceiveEvent.class, viewTweaks);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketReceiveEvent event = (PacketEvent.PacketReceiveEvent) object;
        if (module.removeSPacketEffects.GetSwitch() && event.getPacket() instanceof SPacketEffect) {
            event.setCancelled();
        }
    }
}
