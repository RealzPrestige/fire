package dev.zprestige.fire.module.player.criticals

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch
import net.minecraft.network.play.client.CPacketPlayer

@Descriptor(description = "Turns hits into critical hits")
class Criticals : Module() {
    val offset: Slider = Menu.Slider("Offset", 0.1f, 0.1f, 1.0f)
    val allowMoving: Switch = Menu.Switch("Allow Moving", false)

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            PacketSendListener(this)
        )
    }

    fun sendPacket(offset: Float) {
        mc.player.connection.sendPacket(CPacketPlayer.Position(mc.player.posX,
            mc.player.posY + offset,
            mc.player.posZ,
            false
        )
        )
    }
}
