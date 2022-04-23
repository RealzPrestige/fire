package dev.zprestige.fire.manager.inventorymanager;

import dev.zprestige.fire.Main;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryManager {
    protected final Minecraft mc = Main.mc;

    public void switchToSlot(final int slot) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    public void switchBack(final int slot) {
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    public int getItemSlot(final Item item) {
        int itemSlot = -1;
        for (int i = 1; i <= 45; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem().equals(item)) {
                itemSlot = i;
                break;
            }
        }
        return itemSlot;
    }

    public int getBlockSlot(final Block block) {
        int itemSlot = -1;
        for (int i = 1; i <= 45; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem().equals(Item.getItemFromBlock(block))) {
                itemSlot = i;
                break;
            }
        }
        return itemSlot;
    }


    public int getBlockFromHotbar(final Block block) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem().equals(Item.getItemFromBlock(block)))
                slot = i;
        }
        return slot;
    }

    public int getItemFromHotbar(final Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem().equals(item))
                slot = i;
        }
        return slot;
    }
}
