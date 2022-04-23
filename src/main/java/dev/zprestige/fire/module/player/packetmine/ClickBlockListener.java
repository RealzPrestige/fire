package dev.zprestige.fire.module.player.packetmine;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.BlockInteractEvent;

public class ClickBlockListener extends EventListener<BlockInteractEvent.ClickBlock, PacketMine> {

    public ClickBlockListener(final PacketMine packetMine) {
        super(BlockInteractEvent.ClickBlock.class, packetMine);
    }

    @Override
    public void invoke(final Object object) {
        if (mc.playerController.curBlockDamageMP > 0.0f) {
            mc.playerController.isHittingBlock = true;
        }
        if (module.activePos != null && module.silentSwitch.GetCombo().equals("Clicked")) {
            module.attemptBreak(module.activePos, module.facing);
        }
    }
}
