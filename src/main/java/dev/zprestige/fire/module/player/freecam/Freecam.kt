package dev.zprestige.fire.module.player.freecam

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.Slider
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity

@Descriptor(description = "hacker")
class Freecam : Module() {
    val speed: Slider = Menu.Slider("Speed", 1.0f, 0.1f, 10.0f)
    var entity: EntityOtherPlayerMP? = null
    lateinit var rots: FloatArray
    lateinit var pos: FloatArray
    var ridingEntity: Entity? = null

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            PacketSendListener(this),
            TickListener(this)
        )
    }

    override fun onEnable() {
        val entity = mc.player.getRidingEntity()
        if (entity != null) {
            ridingEntity = entity
            mc.player.dismountRidingEntity()
        } else {
            ridingEntity = null
        }
        rots = floatArrayOf(
            mc.player.rotationYaw,
            mc.player.rotationPitch
        )
        pos = floatArrayOf(mc.player.posX.toFloat(), mc.player.posY.toFloat(), mc.player.posZ.toFloat())
        spawnEntity()
    }

    override fun onDisable() {
        mc.player.rotationYaw = rots[0]
        mc.player.rotationPitch = rots[1]
        if (ridingEntity != null) {
            mc.player.startRiding(ridingEntity!!, true)
        }
        mc.player.setPosition(pos[0].toDouble(), pos[1].toDouble(), pos[2].toDouble())
        if (entity != null) {
            mc.world.removeEntity(entity!!)
        }
    }

    private fun spawnEntity() {
        val entity = EntityOtherPlayerMP(mc.world, mc.player.gameProfile)
        entity.copyLocationAndAnglesFrom(mc.player)
        entity.rotationYawHead = mc.player.rotationYawHead
        entity.prevRotationYawHead = mc.player.rotationYawHead
        entity.rotationYaw = mc.player.rotationYaw
        entity.prevRotationYaw = mc.player.rotationYaw
        entity.rotationPitch = mc.player.rotationPitch
        entity.prevRotationPitch = mc.player.rotationPitch
        entity.cameraYaw = mc.player.rotationYaw
        entity.cameraPitch = mc.player.rotationPitch
        entity.limbSwing = mc.player.limbSwing
        mc.world.addEntityToWorld(entity.entityId, entity)
        this.entity = entity
    }
}