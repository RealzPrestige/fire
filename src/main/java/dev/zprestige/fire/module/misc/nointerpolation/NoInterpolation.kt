package dev.zprestige.fire.module.misc.nointerpolation

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch
import net.minecraft.entity.player.EntityPlayer

@Descriptor(description = "Removes interpolation between ticks")
class NoInterpolation : Module() {
    val delay: Slider = Menu.Slider("Delay", 1.0f, 1.0f, 20.0f)
    val sneak: Switch = Menu.Switch("Sneak", false)
    val cancelAnimations: Switch = Menu.Switch("Cancel Animations", false)
    var noInterpolatedPlayers = ArrayList<NoInterpolatedPlayer>()
    var i = 0

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            Frame3DListener(this),
            TickListener(this)
        )
    }

    fun fixPos(entityPlayer: EntityPlayer) {
        entityPlayer.prevPosX = entityPlayer.posX
        entityPlayer.prevPosY = entityPlayer.posY
        entityPlayer.prevPosZ = entityPlayer.posZ
        entityPlayer.prevChasingPosX = entityPlayer.posX
        entityPlayer.prevChasingPosY = entityPlayer.posY
        entityPlayer.prevChasingPosZ = entityPlayer.posZ
    }

    fun cancelAnimations(entityPlayer: EntityPlayer) {
        entityPlayer.limbSwing = 0f
        entityPlayer.limbSwingAmount = 0f
        entityPlayer.prevLimbSwingAmount = 0f
        entityPlayer.rotationPitch = 0f
        entityPlayer.prevRotationPitch = 0f
    }

    operator fun contains(entityPlayer: EntityPlayer): Boolean {
        return noInterpolatedPlayers.stream()
            .anyMatch { noInterpolatedPlayer: NoInterpolatedPlayer -> noInterpolatedPlayer.entityPlayer == entityPlayer }
    }

    class NoInterpolatedPlayer(val entityPlayer: EntityPlayer) {
        var x = 0.0
        var y = 0.0
        var z = 0.0

        init {
            setPos()
        }

        fun updatePosition() {
            entityPlayer.setPosition(x, y, z)
        }

        fun setPos() {
            x = entityPlayer.posX
            y = entityPlayer.posY
            z = entityPlayer.posZ
        }
    }
}