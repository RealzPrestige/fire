package dev.zprestige.fire.module.movement.blink

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.module.exploit.burrow.Burrow
import dev.zprestige.fire.settings.impl.ComboBox
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.network.Packet
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

@Descriptor(description = "Cancels packets and releases them when disabled")
class Blink : Module() {
    val mode: ComboBox = Menu.ComboBox("Mode", "Blink", arrayOf(
        "Blink",
        "Pulse"
    )
    )
    val cPacketPlayer: Switch = Menu.Switch("CPacketPlayer", false)
    val ticks: Slider = Menu.Slider("Ticks", 10.0f, 1.0f, 20.0f).visibility { z: Float? -> mode.GetCombo() == "Pulse" }
    private val burrowOnFinish: Switch = Menu.Switch("Burrow On Finish", false).visibility { z: Boolean? -> mode.GetCombo() == "Blink" }
    val packets: Queue<Packet<*>> = ConcurrentLinkedQueue()
    var entity: EntityOtherPlayerMP? = null
    var i = 0

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            Frame3DListener(this),
            PacketSendListener(this),
            TickListener(this)
        )
    }

    override fun onEnable() {
        entity = null
        spawnEntity()
    }

    override fun onDisable() {
        poll(false)
    }

    fun spawnEntity() {
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
        this.entity = entity
    }

    fun poll(reEnable: Boolean) {
        if (nullCheck()) {
            if (entity != null) {
                mc.world.removeEntity(entity!!)
            }
            while (!packets.isEmpty()) {
                mc.player.connection.sendPacket(packets.poll())
            }
            if (mode.GetCombo() == "Blink" && burrowOnFinish.GetSwitch()) {
                val burrow = Main.moduleManager.getModuleByClass(Burrow::class.java) as Burrow
                burrow.enableModule()
            }
            if (reEnable) {
                disableModule()
                enableModule()
            }
        }
    }
}