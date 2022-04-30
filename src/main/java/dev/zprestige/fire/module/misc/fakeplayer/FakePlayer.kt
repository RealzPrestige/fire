package dev.zprestige.fire.module.misc.fakeplayer

import com.mojang.authlib.GameProfile
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.Switch
import net.minecraft.client.entity.EntityOtherPlayerMP
import java.util.*

@Descriptor(description = "Spawns a fake entity for e.g testing")
class FakePlayer : Module() {
    val start: Switch = Menu.Switch("Start", false)
    val record: Switch = Menu.Switch("Record", false)
    val recording = ArrayList<Location>()
    val id = 438297483
    var fakePlayer: EntityOtherPlayerMP? = null
    var recorded = false
    var index = 0
    var running = 0
    var prevPosX = 0f
    var prevPosY = 0f
    var prevPosZ = 0f
    var prevRotationYaw = 0f
    var prevRotationYawHead = 0f
    var prevRotationPitch = 0f

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            TickListener(this)
        )
    }

    override fun onEnable() {
        if (mc.world != null) {
            fakePlayer = EntityOtherPlayerMP(mc.world,
                GameProfile(UUID.fromString("12cbdfad-33b7-4c07-aeac-01766e609482"), "zPrestige_")
            )
            fakePlayer!!.copyLocationAndAnglesFrom(mc.player)
            fakePlayer!!.inventory.copyInventory(mc.player.inventory)
            fakePlayer!!.health = 36f
            mc.world.addEntityToWorld(id, fakePlayer!!)
            prevPosX = fakePlayer!!.posX.toFloat()
            prevPosY = fakePlayer!!.posY.toFloat()
            prevPosZ = fakePlayer!!.posZ.toFloat()
            prevRotationYaw = fakePlayer!!.rotationYaw
            prevRotationYawHead = fakePlayer!!.rotationYawHead
            prevRotationPitch = fakePlayer!!.rotationPitch
        }
    }

    override fun onDisable() {
        if (fakePlayer != null) {
            mc.world.removeEntityFromWorld(id)
        }
        index = 0
        running = 0
    }

    class Location(val x: Float, val y: Float, val z: Float, val rotationYaw: Float, val rotationYawHead: Float, val rotationPitch: Float)
}