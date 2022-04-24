package dev.zprestige.fire.module.player.fastexp;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.stream.IntStream;

public class TickListener extends EventListener<TickEvent, FastExp> {

    public TickListener(final FastExp fastExp) {
        super(TickEvent.class, fastExp);
    }

    @Override
    public void invoke(final Object object) {
        if (mc.currentScreen == null) {
            switch (module.mode.GetCombo()) {
                case "Vanilla":
                    if (!mc.player.getHeldItemMainhand().getItem().equals(Items.EXPERIENCE_BOTTLE)) {
                        return;
                    }
                    mc.rightClickDelayTimer = 0;

                    break;
                case "Packet":
                    if (module.activateMode.GetCombo().equals("RightClick") && !mc.gameSettings.keyBindUseItem.isKeyDown()) {
                        return;
                    }
                    if (module.activateMode.GetCombo().equals("MiddleClick") && !Mouse.isButtonDown(2)) {
                        return;
                    }
                    if (module.activateMode.GetCombo().equals("Custom") && !Keyboard.isKeyDown(module.customKey.GetKey())) {
                        return;
                    }
                    if (module.handOnly.GetSwitch() && !mc.player.getHeldItemMainhand().getItem().equals(Items.EXPERIENCE_BOTTLE)) {
                        return;
                    }
                    final int slot = Main.inventoryManager.getItemSlot(Items.EXPERIENCE_BOTTLE);
                    if (slot != -1) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));

                        IntStream.range(0, (int) module.packets.GetSlider()).forEach(i -> mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND)));
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                    }

                    break;
            }
        }
    }
}
