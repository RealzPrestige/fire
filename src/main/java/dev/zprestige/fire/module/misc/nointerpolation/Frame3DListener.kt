package dev.zprestige.fire.module.misc.nointerpolation

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.FrameEvent.FrameEvent3D
import dev.zprestige.fire.module.misc.nointerpolation.NoInterpolation.NoInterpolatedPlayer
import net.minecraft.entity.player.EntityPlayer
import java.util.function.Consumer

class Frame3DListener(noInterpolation: NoInterpolation) : EventListener<FrameEvent3D, NoInterpolation>(
    FrameEvent3D::class.java, noInterpolation
) {
    override fun invoke(`object`: Any) {
        if (module.i >= module.delay.GetSlider()) {
            module.noInterpolatedPlayers.forEach(Consumer { noInterpolatedPlayer: NoInterpolatedPlayer ->
                noInterpolatedPlayer.setPos()
                module.fixPos(noInterpolatedPlayer.entityPlayer)
            })
            module.i = 0
        }
        mc.world.playerEntities.stream().filter { entityPlayer: EntityPlayer ->
            entityPlayer != mc.player && !module.contains(entityPlayer)
        }.forEach { entityPlayer: EntityPlayer? ->
            module.noInterpolatedPlayers.add(NoInterpolatedPlayer(
                entityPlayer!!
            )
            )
        }
        ArrayList(module.noInterpolatedPlayers).forEach(Consumer forEach@{ noInterpolatedPlayer: NoInterpolatedPlayer ->
            val entityPlayer = noInterpolatedPlayer.entityPlayer
            if (entityPlayer.isDead) {
                module.noInterpolatedPlayers.remove(noInterpolatedPlayer)
                return@forEach
            }
            noInterpolatedPlayer.updatePosition()
            module.fixPos(noInterpolatedPlayer.entityPlayer)
            if (module.sneak.GetSwitch()) {
                entityPlayer.isSneaking = true
            }
            if (module.cancelAnimations.GetSwitch()) {
                module.cancelAnimations(entityPlayer)
            }
        })
    }
}