package dev.zprestige.fire.module.player.packetmine;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.PacketEvent;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;

public class PacketSendListener extends EventListener<PacketEvent.PacketSendEvent, PacketMine> {

    public PacketSendListener(final PacketMine packetMine) {
        super(PacketEvent.PacketSendEvent.class, packetMine);
    }

    @Override
    public void invoke(final Object object) {
        final PacketEvent.PacketSendEvent event = (PacketEvent.PacketSendEvent) object;
        if (!module.nullCheck()){
            return;
        }
        if (module.abortOnSwitch.GetSwitch() && module.activePos != null && module.facing != null && event.getPacket() instanceof CPacketHeldItemChange) {
            final CPacketHeldItemChange packet = (CPacketHeldItemChange) event.getPacket();
            if (!mc.player.inventory.getStackInSlot(packet.getSlotId()).getItem().equals(Items.DIAMOND_PICKAXE)) {
                module.abortWithoutEnding(module.activePos, module.facing);
                module.initiateBreaking(module.activePos, module.facing);
                module.col = new float[]{module.inactiveColor.GetColor().getRed(), module.inactiveColor.GetColor().getGreen(), module.inactiveColor.GetColor().getBlue(), module.inactiveColor.GetColor().getAlpha()};
                module.size = 0.0f;
            }
        }
        if (event.getPacket() instanceof CPacketPlayerDigging && module.shouldCancel) {
            final CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();
            if (packet.getAction().equals(CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                event.setCancelled();
            }
        }
    }
}
