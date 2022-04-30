package dev.zprestige.fire.module.movement.step

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.ComboBox
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch
import net.minecraft.network.play.client.CPacketPlayer

@Descriptor(description = "Allows you to teleport up to 2 blocks high")
class Step : Module() {
    val mode: ComboBox = Menu.ComboBox(
        "Mode", "Vanilla", arrayOf(
            "Vanilla",
            "Ncp"
        )
    )
    val height: Slider = Menu.Slider("Height", 2.0f, 1.0f, 2.0f)
    private val entities: Switch = Menu.Switch("Entities", false)
    private val singleOffsets = floatArrayOf(0.42f, 0.753f)
    private val doubleOffsets = floatArrayOf(0.42f, 0.78f, 0.63f, 0.51f, 0.9f, 1.21f, 1.45f, 1.43f)

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            MoveListener(this)
        )
    }

    override fun onDisable() {
        Main.tickManager.syncTimer()
        mc.player.stepHeight = 0.6f
    }

    fun checkRiding(): Boolean {
        return !(!entities.GetSwitch() && mc.player.isRiding)
    }

    fun checkEmpty(amount: Int, i: FloatArray): Boolean {
        return if (amount == 1) checkFirstHeight(i) else checkFirstHeight(i) || isBoundingEmpty(
            i,
            2.1f
        ) && !isBoundingEmpty(i, 1.9f)
    }

    private fun checkFirstHeight(i: FloatArray): Boolean {
        return isBoundingEmpty(i, 1.1f) && !isBoundingEmpty(i, 0.9f)
    }

    fun canStep(): Boolean {
        return mc.player.collidedHorizontally && mc.player.onGround
    }

    fun performStep(amount: Int, i: FloatArray) {
        sendOffsets(amount, i)
    }

    private fun sendOffsets(amount: Int, i: FloatArray) {
        for (j in if (amount == 1) singleOffsets else if (checkFirstHeight(i)) singleOffsets else doubleOffsets) {
            mc.player.connection.sendPacket(
                CPacketPlayer.Position(
                    mc.player.posX,
                    mc.player.posY + j,
                    mc.player.posZ,
                    mc.player.onGround
                )
            )
        }
        mc.player.setPosition(
            mc.player.posX,
            mc.player.posY + if (amount == 2) if (checkFirstHeight(i)) 1 else 2 else 1,
            mc.player.posZ
        )
    }

    private fun isBoundingEmpty(i: FloatArray, y: Float): Boolean {
        return mc.world.getCollisionBoxes(
            mc.player,
            mc.player.entityBoundingBox.offset(i[0].toDouble(), y.toDouble(), i[1].toDouble())
        ).isEmpty()
    }
}