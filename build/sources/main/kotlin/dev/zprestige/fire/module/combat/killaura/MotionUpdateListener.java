package dev.zprestige.fire.module.combat.killaura;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.bus.Stage;
import dev.zprestige.fire.event.impl.MotionUpdateEvent;
import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

public class MotionUpdateListener extends EventListener<MotionUpdateEvent, KillAura> {

    public MotionUpdateListener(final KillAura killAura) {
        super(MotionUpdateEvent.class, killAura);
    }

    @Override
    public void invoke(final Object object) {
        final MotionUpdateEvent event = (MotionUpdateEvent) object;
        if (!event.getStage().equals(Stage.Pre) || !module.nullCheck() || ((module.swordOnly.GetSwitch() && !mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_SWORD)) && !module.autoSwitch.GetSwitch())) {
            return;
        }
        final PlayerManager.Player player = EntityUtil.getClosestTarget(module.targetPriority(module.targetPriority.GetCombo()), module.range.GetSlider());
        if (player == null) {
            return;
        }
        final EntityPlayer entityPlayer = player.getEntityPlayer();
        final float range = !mc.player.canEntityBeSeen(entityPlayer) ? module.wallRange.GetSlider() : module.range.GetSlider();
        if (player.getDistance() > range) {
            return;
        }
        if (module.rotate.GetSwitch()) {
            final float partialTicks = mc.getRenderPartialTicks();
            final float[] angle = Main.rotationManager.calculateAngle(entityPlayer.getPositionEyes(partialTicks));
            event.setYaw(angle[0]);
            event.setPitch(angle[1]);
        }
        if ((module.delay.GetSwitch() && !module.timer.getTime(650))) {
            return;
        }
        if (module.autoSwitch.GetSwitch() && !mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_SWORD)) {
            final int slot = Main.inventoryManager.getItemFromHotbar(Items.DIAMOND_SWORD);
            if (slot != -1) {
                Main.inventoryManager.switchToSlot(slot);
            }
        }
        if (module.packet.GetSwitch()) {
            mc.player.connection.sendPacket(new CPacketUseEntity(entityPlayer));
        } else {
            mc.playerController.attackEntity(mc.player, entityPlayer);
        }
        mc.player.swingArm(mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_SWORD) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
        module.timer.syncTime();
    }
}
