package dev.zprestige.fire.module.player;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.EntityUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import java.util.stream.IntStream;

@Descriptor(description = "Refills non full stacks in your hotbar")
public class Refill extends Module {
    public final Switch strict = Menu.Switch("Strict", false);
    public final Slider delay = Menu.Slider("Delay", 50, 0, 500);
    public final Slider fillAt = Menu.Slider("Fill At", 60, 1, 64);
    protected final Timer timer = new Timer();

    @RegisterListener
    public void onTick(final TickEvent event) {
        if (mc.currentScreen == null && (!strict.GetSwitch() || !EntityUtil.isMoving()) && timer.getTime((long) delay.GetSlider()) && IntStream.range(0, 9).anyMatch(this::refillSlot)) {
            timer.syncTime();
        }
    }

    protected boolean refillSlot(final int slot) {
        final ItemStack stack = mc.player.inventory.getStackInSlot(slot);
        if ((!stack.isEmpty() && stack.getItem() != Items.AIR) && stack.isStackable() && stack.getCount() < stack.getMaxStackSize() && stack.getCount() <= fillAt.GetSlider()) {
            for (int i = 9; i < 36; ++i) {
                final ItemStack item = mc.player.inventory.getStackInSlot(i);
                if (!item.isEmpty() && stack.getItem() == item.getItem() && stack.getDisplayName().equals(item.getDisplayName())) {
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
                    mc.playerController.updateController();
                    return true;
                }
            }
        }
        return false;
    }
}