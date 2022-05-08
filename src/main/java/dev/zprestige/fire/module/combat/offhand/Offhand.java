package dev.zprestige.fire.module.combat.offhand;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.BlockUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;

import java.util.Arrays;

@Descriptor(description = "Puts item in your offhand")
public class Offhand extends Module {
    public final ComboBox mode = Menu.ComboBox("Mode", "Totem", new String[]{
            "Totem",
            "Crystal",
            "Gapple"
    });
    public final Slider health = Menu.Slider("Health", 15.0f, 0.1f, 36.0f);
    public final Slider safeHealth = Menu.Slider("Safe Health", 10.0f, 0.1f, 36.0f);
    public final Slider delay = Menu.Slider("Delay", 50.0f, 0.1f, 1000.0f);
    public final Switch hotbar = Menu.Switch("Hotbar", false);
    public final Switch fallback = Menu.Switch("Fallback", false);
    public final Switch lethal = Menu.Switch("Lethal", false);
    public final Slider minimumLethal = Menu.Slider("Minimum Lethal", 20.0f, 0.1f, 100.0f).visibility(z -> lethal.GetSwitch());
    public final Switch gapple = Menu.Switch("Gapple", false);
    public final Switch gappleRightClick = Menu.Switch("Gapple Right Click", false).visibility(z -> gapple.GetSwitch());

    public Offhand() {
        eventListeners = new EventListener[]{
                new MotionUpdateListener(this)
        };
    }

    protected int findSlot() {
        if (mc.currentScreen != null) {
            return -1;
        }
        final int totem = Main.inventoryManager.getItemSlot(Items.TOTEM_OF_UNDYING, hotbar.GetSwitch());
        if (!valid()) {
            return totem;
        }
        if (gapple.GetSwitch() && mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_SWORD)) {
            if (gappleRightClick.GetSwitch()) {
                if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    return Main.inventoryManager.getItemSlot(Items.GOLDEN_APPLE, hotbar.GetSwitch());
                }
            } else {
                return Main.inventoryManager.getItemSlot(Items.GOLDEN_APPLE, hotbar.GetSwitch());
            }
        }
        switch (mode.GetCombo()) {
            case "Totem":
                return totem;
            case "Crystal":
                final int crystal = Main.inventoryManager.getItemSlot(Items.END_CRYSTAL, hotbar.GetSwitch());
                if (crystal != -1) {
                    return Main.inventoryManager.getItemSlot(Items.END_CRYSTAL, hotbar.GetSwitch());
                }
                if (fallback.GetSwitch()) {
                    return totem;
                }
            case "Gapple":
                final int gapple = Main.inventoryManager.getItemSlot(Items.GOLDEN_APPLE, hotbar.GetSwitch());
                if (gapple != -1) {
                    return gapple;
                }
                if (fallback.GetSwitch()) {
                    return totem;
                }
        }
        return -1;
    }

    protected boolean valid() {
        final double hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        final boolean safe = BlockUtil.isPlayerSafe(new PlayerManager.Player(mc.player));
        return !(lethal.GetSwitch() && mc.player.fallDistance > minimumLethal.GetSlider()) && hp > (safe ? safeHealth.GetSlider() : health.GetSlider());
    }

    protected void swapItem(final int i) {
        final Item item = mc.player.inventory.getStackInSlot(i).getItem();
        if (!mc.player.getHeldItemOffhand().getItem().equals(item)) {
            int slot = i < 9 ? i + 36 : i;
            swap(new int[]{slot, 45, slot});
            mc.playerController.updateController();
        }
    }

    protected void swap(final int[] slots) {
        Arrays.stream(slots).forEach(i -> mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, mc.player));
    }
}
