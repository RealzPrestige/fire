package dev.zprestige.fire.module.combat;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.KeyEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Key;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.EntityUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;

import java.util.stream.IntStream;

public class AutoArmor extends Module {
    public final Slider delay = Menu.Slider("Delay", 50.0f, 0.1f, 500.0f);
    public final Switch strict = Menu.Switch("Strict", false);
    public final Key elytraSwap = Menu.Key("Elytra Swap", Keyboard.KEY_NONE);
    public final Key singleMend = Menu.Key("Single Mend", Keyboard.KEY_NONE);
    public final Switch safety = Menu.Switch("Safety", false).visibility(z -> singleMend.GetKey() != Keyboard.KEY_NONE);
    public final Switch autoExp = Menu.Switch("Auto Exp", false).visibility(z -> singleMend.GetKey() != Keyboard.KEY_NONE);
    public final Switch lookDown = Menu.Switch("LookDown", false).visibility(z -> autoExp.GetSwitch() && singleMend.GetKey() != Keyboard.KEY_NONE);
    public final Slider packets = Menu.Slider("Packets", 1.0f, 1.0f, 10.0f).visibility(z -> autoExp.GetSwitch() && singleMend.GetKey() != Keyboard.KEY_NONE);
    public final Slider enemyRange = Menu.Slider("Enemy Range", 20.0f, 0.1f, 50.0f).visibility(z -> safety.GetSwitch() && singleMend.GetKey() != Keyboard.KEY_NONE);
    public final Slider threshold = Menu.Slider("Threshold", 85.0f, 0.1f, 100.0f).visibility(z -> singleMend.GetKey() != Keyboard.KEY_NONE);
    protected final Timer timer = new Timer();
    protected boolean takingOff = false, announced = false, elytra = false;

    @RegisterListener
    public void onKeyEvent(final KeyEvent event) {
        if (event.getKey() == singleMend.GetKey()) {
            takingOff = !takingOff;
        }
        if (event.getKey() == elytraSwap.GetKey()) {
            elytra = !elytra;
        }
    }

    @RegisterListener
    public void onTick(final TickEvent event) {
        if (mc.currentScreen != null || (strict.GetSwitch() && EntityUtil.isMoving())) {
            return;
        }
        if (singleMend.isHold()) {
            takingOff = Keyboard.isKeyDown(singleMend.GetKey());
        }
        if (elytraSwap.isHold()) {
            elytra = Keyboard.isKeyDown(elytraSwap.GetKey());
        }
        if (takingOff && canTakeOff()) {
            if (!announced) {
                Main.chatManager.sendRemovableMessage("Started single mending.", 1);
                announced = true;
            }
            takeOffSingle();
            return;
        } else if (announced) {
            if (!canTakeOff()) {
                Main.chatManager.sendRemovableMessage("Enemy in range, stopping single mend.", 1);
            } else {
                Main.chatManager.sendRemovableMessage("Finished single mend", 1);
            }
            announced = false;
        }
        final int slot = findSlot();
        if (timer.getTime((long) delay.GetSlider())) {
            if (elytra) {
                final int e = Main.inventoryManager.getItemSlot(Items.ELYTRA);
                if (e != -1) {
                    if (air(38)) {
                        clickSlot(e);
                    } else if (!elytra()){
                        clickSlot(6);
                    }
                    return;
                }
            }
            if (!elytra && elytra()) {
                final int chest = Main.inventoryManager.getItemSlot(Items.DIAMOND_CHESTPLATE);
                if (chest != -1) {
                    if (air(38)) {
                        clickSlot(chest);
                    } else if (!chestplate()){
                        clickSlot(6);
                    }
                    return;
                }
            }
            if (slot != -1) {
                clickSlot(slot);
            }
        }
    }

    protected boolean canTakeOff() {
        return EntityUtil.getClosestTarget(EntityUtil.TargetPriority.Range, enemyRange.GetSlider()) == null;
    }

    protected void takeOffSingle() {
        if (autoExp.GetSwitch()) {
            int slot = Main.inventoryManager.getItemFromHotbar(Items.EXPERIENCE_BOTTLE);
            if (slot != -1) {
                final float pitch = mc.player.rotationPitch;
                if (lookDown.GetSwitch()) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, mc.player.onGround));
                }
                mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                IntStream.range(0, (int) packets.GetSlider()).forEach(i -> mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND)));
                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                if (lookDown.GetSwitch()) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, pitch, mc.player.onGround));
                }
            }
        }
        int i = 40;
        final Armor[] items = new Armor[]{
                new Armor(mc.player.inventory.getStackInSlot(--i), i),
                new Armor(mc.player.inventory.getStackInSlot(--i), i),
                new Armor(mc.player.inventory.getStackInSlot(--i), i),
                new Armor(mc.player.inventory.getStackInSlot(--i), i),
        };
        Armor foundItem = null;
        for (final Armor armor : items) {
            final float percentage = getPercentage(armor.getItemStack());
            if (percentage < threshold.GetSlider()) {
                foundItem = armor;
                break;
            }
        }
        if (foundItem != null) {
            int[] ints = new int[]{5, 6, 7, 8};
            for (final int i1 : ints) {
                final int slot = 44 - i1;
                if (slot == foundItem.getI()) {
                    continue;
                }
                if (!timer.getTime((long) delay.GetSlider())) {
                    return;
                }
                final ItemStack itemStack = mc.player.inventory.getStackInSlot(slot);
                if (!itemStack.equals(ItemStack.EMPTY)) {
                    clickSlot(i1);
                    break;
                }
            }
        } else {
            if (!timer.getTime((long) delay.GetSlider())) {
                return;
            }
            final int helmet = Main.inventoryManager.getItemSlot(Items.DIAMOND_HELMET);
            final int chest = Main.inventoryManager.getItemSlot(Items.DIAMOND_CHESTPLATE);
            final int legs = Main.inventoryManager.getItemSlot(Items.DIAMOND_LEGGINGS);
            final int boots = Main.inventoryManager.getItemSlot(Items.DIAMOND_BOOTS);
            if (helmet != -1 && getPercentage(itemStackInSlot(helmet)) < threshold.GetSlider()) {
                clickSlot(helmet);
                return;
            }
            if (chest != -1 && getPercentage(itemStackInSlot(chest)) < threshold.GetSlider()) {
                clickSlot(chest);
                return;
            }
            if (legs != -1 && getPercentage(itemStackInSlot(legs)) < threshold.GetSlider()) {
                clickSlot(legs);
                return;
            }
            if (boots != -1 && getPercentage(itemStackInSlot(boots)) < threshold.GetSlider()) {
                clickSlot(boots);
                return;
            }
            takingOff = false;
        }
    }

    protected ItemStack itemStackInSlot(final int slot) {
        return mc.player.inventory.getStackInSlot(slot);
    }

    protected int findSlot() {
        final int helmet = Main.inventoryManager.getItemSlot(Items.DIAMOND_HELMET);
        final int chest = Main.inventoryManager.getItemSlot(Items.DIAMOND_CHESTPLATE);
        final int legs = Main.inventoryManager.getItemSlot(Items.DIAMOND_LEGGINGS);
        final int boots = Main.inventoryManager.getItemSlot(Items.DIAMOND_BOOTS);
        if (air(39) && helmet != -1) {
            return helmet;
        }
        if (air(38) && chest != -1) {
            return chest;
        }
        if (air(37) && legs != -1) {
            return legs;
        }
        if (air(36) && boots != -1) {
            return boots;
        }
        return -1;
    }

    protected boolean air(final int slot) {
        return mc.player.inventory.getStackInSlot(slot).getItem().equals(Items.AIR);
    }

    protected boolean elytra() {
        return mc.player.inventory.getStackInSlot(38).getItem().equals(Items.ELYTRA);
    }

    protected boolean chestplate() {
        return mc.player.inventory.getStackInSlot(38).getItem().equals(Items.DIAMOND_CHESTPLATE);
    }
    protected void clickSlot(final int slot) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.QUICK_MOVE, mc.player);
        timer.syncTime();
    }

    protected float getPercentage(final ItemStack stack) {
        final float durability = stack.getMaxDamage() - stack.getItemDamage();
        return (durability / stack.getMaxDamage()) * 100.0f;
    }

    protected static class Armor {
        protected final ItemStack itemStack;
        protected final int i;

        public Armor(final ItemStack itemStack, final int i) {
            this.itemStack = itemStack;
            this.i = i;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public int getI() {
            return i;
        }
    }
}
