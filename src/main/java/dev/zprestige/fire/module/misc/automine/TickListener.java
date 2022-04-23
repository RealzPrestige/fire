package dev.zprestige.fire.module.misc.automine;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.module.combat.autocrystal.AutoCrystal;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.EntityUtil;

public class TickListener extends EventListener<TickEvent, AutoMine> {

    public TickListener(final AutoMine autoMine){
        super(TickEvent.class, autoMine);
    }

    @Override
    public void invoke(final Object object){
        final PlayerManager.Player player = EntityUtil.getClosestTarget(EntityUtil.TargetPriority.Health, module.targetRange.GetSlider());
        if (player != null && BlockUtil.isPlayerSafe(player)) {
            if (module.started) {
                if (module.timer.getTime(2000)) {
                    final AutoCrystal autoCrystal = (AutoCrystal) Main.moduleManager.getModuleByClass(AutoCrystal.class);
                    if (module.damage.GetSwitch() && BlockUtil.canPosBeCrystalled(module.interactedPos, module.onePointThirteen.GetSwitch()) && autoCrystal.isEnabled()){
                        autoCrystal.placeCrystal(module.interactedPos, null);
                    }
                    Main.interactionManager.attemptBreak(module.interactedPos, module.interactedFace);
                    module.interactedFace = null;
                    module.interactedPos = null;
                    module.timer.syncTime();
                    module.started = false;
                    module.target = null;
                }
            } else {
                for (int i = 0; i < 3; i++){
                    if (module.perform(module.getPriority(i), player)){
                        return;
                    }
                }
            }
        }
    }
}
