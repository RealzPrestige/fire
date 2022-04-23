package dev.zprestige.fire.module.combat.holefiller;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.manager.holemanager.HoleManager;
import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class TickListener extends EventListener<TickEvent, HoleFiller> {

    public TickListener(final HoleFiller holeFiller){
        super(TickEvent.class, holeFiller);
    }

    @Override
    public void invoke(final Object object){
        final int slot = module.slot();
        if (slot == -1 || (!module.whileMoving.GetSwitch() && EntityUtil.isMoving()) || (module.selfSafe.GetSwitch() && !BlockUtil.isPlayerSafe(new PlayerManager.Player(mc.player))) || (!module.multiTask.GetSwitch() && mc.player.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) && mc.gameSettings.keyBindUseItem.isKeyDown())) {
            return;
        }
        switch (module.mode.GetCombo()) {
            case "Normal":
                int bpt = 0;
                for (final HoleManager.HolePos pos : Main.holeManager.getHoles()) {
                    if (pos.isDouble() && !module.doubles.GetSwitch()){
                        continue;
                    }
                    final BlockPos pos1 = pos.getPos();
                    if (bpt <= module.blocksPerTick.GetSlider() && module.inRange(pos1) &&module. checkRotations()) {
                        module.placeBlock(pos1, slot);
                        if (module.timing.GetCombo().equals("Normal")) {
                            return;
                        }
                        bpt++;
                    }
                }
                break;
            case "Smart":
                final PlayerManager.Player player = EntityUtil.getClosestTarget(module.targetPriority(module.targetPriority.GetCombo()), module.targetRange.GetSlider());
                if (player == null || player.getEntityPlayer() == null || player.getEntityPlayer().isDead || (module.enemyUnsafe.GetSwitch() && !BlockUtil.isPlayerSafe(player))) {
                    return;
                }
                final double[] position = new double[]{player.getEntityPlayer().posX, player.getEntityPlayer().posY, player.getEntityPlayer().posZ};
                final double[] next = Main.motionPredictionManager.getPredictedPosByPlayer(player.getEntityPlayer(), module.predictMotionFactor.GetSlider());
                if (module.predictMotion.GetSwitch()) {
                    player.getEntityPlayer().setPosition(next[0], next[1], next[2]);
                }
                switch (module.smartMode.GetCombo()) {
                    case "Closest":
                        final BlockPos closest = module.closestHole(player);
                        if (closest != null && module.checkRotations()) {
                            module.placeBlock(closest, slot);
                        }
                        break;
                    case "InRange":
                        final ArrayList<BlockPos> possesInRange = module.possesInRange(player);
                        int bpt1 = 0;
                        for (final BlockPos pos : possesInRange) {
                            if (bpt1 <= module.blocksPerTick.GetSlider() && module.inRange(pos) && module.checkRotations()) {
                                module.placeBlock(pos, slot);
                                if (module.timing.GetCombo().equals("Normal")) {
                                    return;
                                }
                                bpt1++;
                            }
                        }
                        break;
                }
                player.getEntityPlayer().setPosition(position[0], position[1], position[2]);
                break;
        }
    }
}
