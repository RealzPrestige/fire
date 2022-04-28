package dev.zprestige.fire.module.combat.autotrap;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class TickListener extends EventListener<TickEvent, AutoTrap> {

    public TickListener(final AutoTrap autoTrap) {
        super(TickEvent.class, autoTrap);
    }

    @Override
    public void invoke(final Object object) {
        final PlayerManager.Player entityPlayer = EntityUtil.getClosestTarget(EntityUtil.TargetPriority.Range, module.placeRange.GetSlider() - 1.0f);
        if (entityPlayer == null || !BlockUtil.isPlayerSafe(entityPlayer) || module.multiTaskValid() || (module.disableOnSelfMove.GetSwitch() && !BlockUtil.getPosition().equals(module.pos))) {
            module.disableModule();
            return;
        }
        final BlockPos pos = entityPlayer.getPosition();
        final ArrayList<AutoTrap.Position> positions = module.getPossiblePlacePositions(pos);
        final int obsidian = Main.inventoryManager.getBlockFromHotbar(Blocks.OBSIDIAN);
        if (obsidian == -1) {
            module.disableModule();
        }
        switch (module.mode.GetCombo()) {
            case "Instant":
                int i = 0;
                for (final AutoTrap.Position position : positions) {
                    if (i > module.blocksPerTick.GetSlider() || Main.inventoryManager.getBlockFromHotbar(Blocks.OBSIDIAN) == -1 || (module.preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations())) {
                        return;
                    }
                    Main.interactionManager.placeBlockWithSwitch(position.getPos(), module.rotate.GetSwitch(), module.packet.GetSwitch(), module.strict.GetSwitch(), obsidian);
                    module.addFade(position.getPos());
                    i++;
                }
                break;
            case "Tick":
                for (final AutoTrap.Position position : positions) {
                    if (module.preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations()) {
                        return;
                    }
                    Main.interactionManager.placeBlockWithSwitch(position.getPos(), module.rotate.GetSwitch(), module.packet.GetSwitch(), module.strict.GetSwitch(), obsidian);
                    module.addFade(position.getPos());
                    return;
                }
                break;
        }
    }
}
