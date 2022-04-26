package dev.zprestige.fire.module.misc.constelytrafly;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.module.movement.longjump.LongJump;
import net.minecraft.network.play.client.CPacketEntityAction;

public class TickListener extends EventListener<TickEvent, ConstElytraFly> {

    public TickListener(final ConstElytraFly constElytraFly){
        super(TickEvent.class, constElytraFly);
    }

    @Override
    public void invoke(final Object object){
        if (!mc.player.isElytraFlying()) {
            if (mc.player.onGround) {
                mc.player.jump();
                module.offGroundTimer.syncTime();
                Main.tickManager.setTimer(0.1f);
            } else {
                if (module.offGroundTimer.getTime((long) module.offGroundTakeOff.GetSlider())) {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                }
            }
        } else {
            mc.gameSettings.keyBindJump.pressed = !module.jumpTimer.getTime(100);
            Main.tickManager.syncTimer();
            final LongJump longJump = (LongJump) Main.moduleManager.getModuleByClass(LongJump.class);
            if (mc.player.posY <= 120.5f){
                if (!longJump.isEnabled()){
                    longJump.enableModule();
                }
            } else if (mc.player.posY >= 121.5f){
                if (longJump.isEnabled()){
                    longJump.disableModule();
                }
            }
        }
    }
}
