package dev.zprestige.fire.module.player.packetmine;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;
import dev.zprestige.fire.util.impl.BlockUtil;
import net.minecraft.init.Blocks;

public class TickListener extends EventListener<TickEvent, PacketMine> {

    public TickListener(final PacketMine packetMine){
        super(TickEvent.class, packetMine);
    }

    @Override
    public void invoke(final Object object){
        if (module.activePos != null) {
            if (BlockUtil.getState(module.activePos).equals(Blocks.AIR)) {
                module.end();
            }
        }
        if (module.attemptingReBreak && !BlockUtil.getState(module.prevPos).equals(Blocks.AIR)) {
            module.initiateBreaking(module.prevPos, module.prevFace);
            module.attemptingReBreak = false;
            module.size = 0.0f;
        }
    }
}
