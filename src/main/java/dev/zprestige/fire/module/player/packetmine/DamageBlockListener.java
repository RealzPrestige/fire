package dev.zprestige.fire.module.player.packetmine;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.BlockInteractEvent;
import dev.zprestige.fire.util.impl.BlockUtil;
import net.minecraft.init.Blocks;

public class DamageBlockListener extends EventListener<BlockInteractEvent.DamageBlock, PacketMine> {

    public DamageBlockListener(final PacketMine packetMine){
        super(BlockInteractEvent.DamageBlock.class, packetMine);
    }

    @Override
    public void invoke(final Object object){
        final BlockInteractEvent.DamageBlock event = (BlockInteractEvent.DamageBlock) object;
        if (module.activePos != null) {
            module.abort(module.activePos, module.facing);
        }
        if (!BlockUtil.getState(event.getPos()).equals(Blocks.BEDROCK)) {
            module.initiateBreaking(event.getPos(), event.getFacing());
            event.setCancelled();
        }
    }
}
