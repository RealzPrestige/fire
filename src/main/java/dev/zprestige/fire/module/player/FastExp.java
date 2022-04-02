package dev.zprestige.fire.module.player;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Key;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.stream.IntStream;

public class FastExp extends Module {
    public final ComboBox mode = Menu.ComboBox("Mode", "Packet", new String[]{
            "Vanilla",
            "Packet"
    });
    public final ComboBox activateMode = Menu.ComboBox("Activate Mode", "RightClick", new String[]{
            "RightClick",
            "MiddleClick",
            "Custom"
    }).visibility(z -> mode.GetCombo().equals("Packet"));
    public final Key customKey = Menu.Key("Custom Key", Keyboard.KEY_NONE).visibility(z -> mode.GetCombo().equals("Packet") && activateMode.GetCombo().equals("Custom"));
    public final Slider packets = Menu.Slider("Packets", 2, 1, 10).visibility(z -> mode.GetCombo().equals("Packet"));
    public final Switch handOnly = Menu.Switch("Hand Only", false).visibility(z -> mode.GetCombo().equals("Packet"));

    @RegisterListener
    public void onTick(final TickEvent event) {
        if (mc.currentScreen == null) {
            switch (mode.GetCombo()) {
                case "Vanilla":
                    if (handOnly.GetSwitch() && !mc.player.getHeldItemMainhand().getItem().equals(Items.EXPERIENCE_BOTTLE)) {
                        return;
                    }
                    mc.rightClickDelayTimer = 0;

                    break;
                case "Packet":
                    if (activateMode.GetCombo().equals("RightClick") && !mc.gameSettings.keyBindUseItem.isKeyDown()) {
                        return;
                    }
                    if (activateMode.GetCombo().equals("MiddleClick") && !Mouse.isButtonDown(2)) {
                        return;
                    }
                    if (activateMode.GetCombo().equals("Custom") && !Keyboard.isKeyDown(customKey.GetKey())) {
                        return;
                    }
                    if (handOnly.GetSwitch() && !mc.player.getHeldItemMainhand().getItem().equals(Items.EXPERIENCE_BOTTLE)) {
                        return;
                    }
                    final int slot = Main.inventoryManager.getItemSlot(Items.EXPERIENCE_BOTTLE);
                    if (slot != -1) {
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));

                        IntStream.range(0, (int) packets.GetSlider()).forEach(i -> mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND)));
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                    }

                    break;
            }
        }
    }
}