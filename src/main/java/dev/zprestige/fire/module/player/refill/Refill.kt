package dev.zprestige.fire.module.player.refill

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch
import dev.zprestige.fire.util.impl.Timer
import net.minecraft.init.Items
import net.minecraft.inventory.ClickType
import net.minecraft.item.ItemStack

@Descriptor(description = "Refills non full stacks in your hotbar")
class Refill : Module() {
    val strict: Switch = Menu.Switch("Strict", false)
    val delay: Slider = Menu.Slider("Delay", 50, 0, 500)
    private val fillAt: Slider = Menu.Slider("Fill At", 60, 1, 64)
    val timer = Timer()

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            TickListener(this)
        )
    }

    fun refillSlot(slot: Int): Boolean {
        val stack: ItemStack = mc.player.inventory.getStackInSlot(slot)
        if (!stack.isEmpty() && stack.getItem() !== Items.AIR && stack.isStackable && stack.count < stack.maxStackSize && stack.count <= fillAt.GetSlider()) {
            for (i in 9..35) {
                val item = mc.player.inventory.getStackInSlot(i)
                if (!item.isEmpty() && stack.getItem().equals(item.getItem()) && stack.displayName == item.displayName) {
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId,
                        i,
                        0,
                        ClickType.QUICK_MOVE,
                        mc.player
                    )
                    mc.playerController.updateController()
                    return true
                }
            }
        }
        return false
    }
}