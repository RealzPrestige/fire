package dev.zprestige.fire.module.misc.automine

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import dev.zprestige.fire.module.combat.autocrystal.AutoCrystal
import dev.zprestige.fire.util.impl.BlockUtil
import dev.zprestige.fire.util.impl.EntityUtil

class TickListener(autoMine: AutoMine) : EventListener<TickEvent, AutoMine>(TickEvent::class.java, autoMine) {

    override fun invoke(e: Any) {
        val player = EntityUtil.getClosestTarget(EntityUtil.TargetPriority.Health, module.targetRange.GetSlider())
        if (player != null && BlockUtil.isPlayerSafe(player)) {
            if (module.started) {
                if (module.timer.getTime(2000)) {
                    val autoCrystal = Main.moduleManager.getModuleByClass(AutoCrystal::class.java) as AutoCrystal
                    if (module.damage.GetSwitch() && BlockUtil.canPosBeCrystalled(module.interactedPos,
                            module.onePointThirteen.GetSwitch()
                        ) && autoCrystal.isEnabled
                    ) {
                        autoCrystal.placeCrystal(module.interactedPos, null)
                    }
                    Main.interactionManager.attemptBreak(module.interactedPos, module.interactedFace)
                    module.interactedFace = null
                    module.interactedPos = null
                    module.timer.syncTime()
                    module.started = false
                    module.target = null
                }
            } else {
                for (i in 0..2) {
                    if (module.perform(module.getPriority(i), player)) {
                        return
                    }
                }
            }
        }
    }
}