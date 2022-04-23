package dev.zprestige.fire.module.movement.antivoid;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.util.impl.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;

public class TickListener extends EventListener<TickEvent, AntiVoid> {

    public TickListener(final AntiVoid antiVoid){
        super(TickEvent.class, antiVoid);
    }

    @Override
    public void invoke(final Object object){
        if (!module.alreadyInVoid && mc.player.posY <= 0.0f){
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
            module.alreadyInVoid = true;
            if (module.placeBlock.GetSwitch() && BlockUtil.getState(module.pos).equals(Blocks.AIR)){
                final int slot = Main.inventoryManager.getBlockSlot(Blocks.OBSIDIAN);
                if (slot != -1) {
                    Main.interactionManager.placeBlockWithSwitch(module.pos, module.rotate.GetSwitch(), module.packet.GetSwitch(), module.strict.GetSwitch(), slot);
                }
            }
        }
        if (module.alreadyInVoid){
            if (mc.player.posY > 0.0f){
                module.alreadyInVoid = false;
                module.pos = BlockUtil.getPosition();
            }
        }
    }
}
