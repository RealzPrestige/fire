package dev.zprestige.fire.module.player.fastexp

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import net.minecraft.init.Items
import net.minecraft.network.play.client.CPacketHeldItemChange
import net.minecraft.network.play.client.CPacketPlayerTryUseItem
import net.minecraft.util.EnumHand
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

class TickListener(fastExp: FastExp) : EventListener<TickEvent, FastExp>(TickEvent::class.java, fastExp) {

    override fun invoke(e: Any) {
        if (mc.currentScreen == null) {
            when (module.mode.GetCombo()) {
                "Vanilla" -> {
                    if (mc.player.heldItemMainhand.getItem() != Items.EXPERIENCE_BOTTLE) {
                        return
                    }
                    mc.rightClickDelayTimer = 0
                }
                "Packet" -> {
                    if (module.activateMode.GetCombo() == "RightClick" && !mc.gameSettings.keyBindUseItem.isKeyDown) {
                        return
                    }
                    if (module.activateMode.GetCombo() == "MiddleClick" && !Mouse.isButtonDown(2)) {
                        return
                    }
                    if (module.activateMode.GetCombo() == "Custom" && !Keyboard.isKeyDown(module.customKey.GetKey())) {
                        return
                    }
                    if (module.handOnly.GetSwitch() && mc.player.heldItemMainhand.getItem() != Items.EXPERIENCE_BOTTLE) {
                        return
                    }
                    val slot = Main.inventoryManager.getItemSlot(Items.EXPERIENCE_BOTTLE)
                    val currentItem = mc.player.inventory.currentItem
                    if (slot != -1) {
                        mc.player.connection.sendPacket(CPacketHeldItemChange(slot))
                        for (i in 0..module.packets.GetSlider().toInt()){
                            mc.player.connection.sendPacket(CPacketPlayerTryUseItem(EnumHand.MAIN_HAND))
                        }
                        mc.player.connection.sendPacket(CPacketHeldItemChange(currentItem))
                    }
                }
            }
        }
    }
}