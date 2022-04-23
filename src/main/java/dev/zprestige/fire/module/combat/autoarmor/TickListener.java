package dev.zprestige.fire.module.combat.autoarmor;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.init.Items;
import org.lwjgl.input.Keyboard;

public class TickListener extends EventListener<TickEvent, AutoArmor> {

    public TickListener(final AutoArmor autoArmor){
        super(TickEvent.class, autoArmor);
    }

    @Override
    public void invoke(final Object object) {
        if (mc.currentScreen != null || (module.strict.GetSwitch() && EntityUtil.isMoving())) {
            return;
        }
        if (module.singleMend.isHold()) {
            module.takingOff = module.singleMend.GetKey() != -1 && Keyboard.isKeyDown(module.singleMend.GetKey());
        }
        if (module.elytraSwap.isHold()) {
            module.elytra = module.elytraSwap.GetKey() != -1 && Keyboard.isKeyDown(module.elytraSwap.GetKey());
        }
        if (module.takingOff && module.canTakeOff()) {
            if (!module.announced) {
                Main.chatManager.sendRemovableMessage("Started single mending.", 1);
                module.announced = true;
            }
            module.takeOffSingle();
            return;
        } else if (module.announced) {
            if (!module.canTakeOff()) {
                Main.chatManager.sendRemovableMessage("Enemy in range, stopping single mend.", 1);
            } else {
                Main.chatManager.sendRemovableMessage("Finished single mend", 1);
            }
            module.announced = false;
        }
        final int slot = module.findSlot();
        if (module.timer.getTime((long) module.delay.GetSlider())) {
            if (module.elytra) {
                final int e = Main.inventoryManager.getItemSlot(Items.ELYTRA);
                if (e != -1) {
                    if (module.air(38)) {
                        module.clickSlot(e);
                    } else if (!module.elytra()){
                        module.clickSlot(6);
                    }
                    return;
                }
            }
            if (!module.elytra && module.elytra()) {
                final int chest = Main.inventoryManager.getItemSlot(Items.DIAMOND_CHESTPLATE);
                if (chest != -1) {
                    if (module.air(38)) {
                        module.clickSlot(chest);
                    } else if (!module.chestplate()){
                        module.clickSlot(6);
                    }
                    return;
                }
            }
            if (slot != -1) {
                module.clickSlot(slot);
            }
        }
    }
}
