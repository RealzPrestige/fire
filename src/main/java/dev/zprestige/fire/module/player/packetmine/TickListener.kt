package dev.zprestige.fire.module.player.packetmine

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import dev.zprestige.fire.module.combat.autocrystal.AutoCrystal
import dev.zprestige.fire.util.impl.BlockUtil
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.network.play.client.CPacketPlayerDigging
import org.lwjgl.input.Keyboard

class TickListener(packetMine: PacketMine) : EventListener<TickEvent, PacketMine>(
    TickEvent::class.java, packetMine
) {
    override fun invoke(`object`: Any) {
        if (!module.nullCheck()) {
            return
        }
        if (module.activePos != null) {
            if (mc.player.getDistanceSq(module.activePos!!) > module.range.GetSlider() * module.range.GetSlider()) {
                module.end()
                return
            }
            if (BlockUtil.getState(module.activePos) == Blocks.AIR) {
                module.end()
                return
            }
        }
        if (module.attemptingReBreak && BlockUtil.getState(module.prevPos) != Blocks.AIR) {
            module.initiateBreaking(module.prevPos, module.prevFace)
            module.attemptingReBreak = false
            module.size = 0.0f
            return
        }
        if (module.instant.GetSwitch() && module.minedPos != null && module.minedFace != null && module.timer.getTime(
                module.instantTiming.GetSlider().toLong()
            ) && (mc.player.heldItemMainhand.getItem() == Items.DIAMOND_PICKAXE || module.instantSilentSwitch.GetSwitch()) && (module.instantKey.GetKey() == -1 || Keyboard.isKeyDown(
                module.instantKey.GetKey()
            ))
        ) {
            if (module.rotate.GetSwitch()) {
                Main.rotationManager.facePos(module.minedPos)
            }
            if (module.instantPlaceCrystal.GetSwitch()) {
                val autoCrystal = Main.moduleManager.getModuleByClass(AutoCrystal::class.java) as AutoCrystal
                if (autoCrystal.isEnabled) {
                    autoCrystal.placeCrystal(module.minedPos, null)
                }
            }
            val currentItem = mc.player.inventory.currentItem
            var switched = false
            if (module.instantSilentSwitch.GetSwitch()) {
                val slot = Main.inventoryManager.getItemSlot(Items.DIAMOND_PICKAXE)
                if (slot != -1) {
                    Main.inventoryManager.switchToSlot(slot)
                    switched = true
                }
            }
            mc.player.connection.sendPacket(CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                module.minedPos!!,
                module.minedFace!!
            )
            )
            if (switched) {
                Main.inventoryManager.switchBack(currentItem)
            }
            module.shouldCancel = true
            module.timer.syncTime()
        } else {
            module.shouldCancel = false
        }
    }
}