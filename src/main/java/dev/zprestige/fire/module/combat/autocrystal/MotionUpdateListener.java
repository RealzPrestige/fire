package dev.zprestige.fire.module.combat.autocrystal;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.bus.Stage;
import dev.zprestige.fire.event.impl.MotionUpdateEvent;
import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.module.misc.automine.AutoMine;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.init.Items;

public class MotionUpdateListener extends EventListener<MotionUpdateEvent, AutoCrystal> {

    public MotionUpdateListener(final AutoCrystal autoCrystal) {
        super(MotionUpdateEvent.class, autoCrystal);
    }

    @Override
    public void invoke(final Object object) {
        final MotionUpdateEvent event = (MotionUpdateEvent) object;
        if (event.getStage().equals(Stage.Pre)) {
            if (!module.multiTask.GetSwitch() && mc.player.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                return;
            }
            PlayerManager.Player player = EntityUtil.getClosestTarget(module.targetPriority(module.targetPriority.GetCombo()), module.targetRange.GetSlider());
            if (player != null) {
                final double[] position = new double[]{player.getEntityPlayer().posX, player.getEntityPlayer().posY, player.getEntityPlayer().posZ};
                final double[] next = Main.motionPredictionManager.getPredictedPosByPlayer(player.getEntityPlayer(), module.predictMotionFactor.GetSlider());
                if (module.predictMotion.GetSwitch()) {
                    player.getEntityPlayer().setPosition(next[0], next[1], next[2]);
                }
                if (module.autoMineTargetPrefer.GetSwitch()) {
                    final PlayerManager.Player autoMineTarget = ((AutoMine) Main.moduleManager.getModuleByClass(AutoMine.class)).getTarget();
                    if (autoMineTarget != null) {
                        player = autoMineTarget;
                    }
                }
                module.pos = null;
                if (player.getHealth() > 0.0f) {
                    module.performAutoCrystal(player, event);
                }
                if (module.predictMotionVisualize.GetSwitch()) {
                    module.setupEntity(player, next);
                }
                player.getEntityPlayer().setPosition(position[0], position[1], position[2]);
            }
            module.handleSwitch();
        }
    }
}
