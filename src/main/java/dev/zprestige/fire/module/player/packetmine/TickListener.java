package dev.zprestige.fire.module.player.packetmine;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.module.combat.autocrystal.AutoCrystal;
import dev.zprestige.fire.util.impl.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import org.lwjgl.input.Keyboard;

public class TickListener extends EventListener<TickEvent, PacketMine> {

    public TickListener(final PacketMine packetMine) {
        super(TickEvent.class, packetMine);
    }

    @Override
    public void invoke(final Object object) {
        if (module.activePos != null) {
            if (BlockUtil.getState(module.activePos).equals(Blocks.AIR)) {
                module.end();
            }
        }
        if (module.attemptingReBreak && !BlockUtil.getState(module.prevPos).equals(Blocks.AIR)) {
            module.initiateBreaking(module.prevPos, module.prevFace);
            module.attemptingReBreak = false;
            module.size = 0.0f;
        }
        if (module.instant.GetSwitch()) {
            mc.playerController.blockHitDelay = 0;
        }
        if (module.instant.GetSwitch() && module.minedPos != null && module.minedFace != null && module.timer.getTime((long) module.instantTiming.GetSlider()) && (mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_PICKAXE) || module.instantSilentSwitch.GetSwitch()) && (module.instantKey.GetKey() == -1 || Keyboard.isKeyDown(module.instantKey.GetKey()))) {
            if (module.rotate.GetSwitch()) {
                Main.rotationManager.facePos(module.minedPos);
            }
            if (module.instantPlaceCrystal.GetSwitch()) {
                final AutoCrystal autoCrystal = (AutoCrystal) Main.moduleManager.getModuleByClass(AutoCrystal.class);
                if (autoCrystal.isEnabled()){
                    autoCrystal.placeCrystal(module.minedPos, null);
                }
            }
            int currentItem = mc.player.inventory.currentItem;
            boolean switched = false;
            if (module.instantSilentSwitch.GetSwitch()) {
                final int slot = Main.inventoryManager.getItemSlot(Items.DIAMOND_PICKAXE);
                if (slot != -1) {
                    Main.inventoryManager.switchToSlot(slot);
                    switched = true;
                }
            }
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, module.minedPos, module.minedFace));
            if (switched) {
                Main.inventoryManager.switchBack(currentItem);
            }
            module.shouldCancel = true;
            module.timer.syncTime();
        } else {
            module.shouldCancel = false;
        }
    }
}
