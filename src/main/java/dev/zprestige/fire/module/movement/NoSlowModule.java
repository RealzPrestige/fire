package dev.zprestige.fire.module.movement;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.*;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import net.minecraft.network.play.client.*;

@Descriptor(description = "Removes slow down from doing stuff")
public class NoSlowModule extends Module {
    public final Slider onGroundSpeed = Menu.Slider("OnGround Speed", 5.0f, 0.1f, 5.0f);
    public final Slider airSpeed = Menu.Slider("Air Speed", 5.0f, 0.1f, 5.0f);
    public final Switch strict = Menu.Switch("Strict", false);
    public final Switch airStrict = Menu.Switch("AirStrict", false);
    public final Switch items = Menu.Switch("Items", true);
    protected boolean airSneaking = false, groundSneaking;

    @RegisterListener
    public void onTick(final TickEvent event) {
        if (airSneaking && airStrict.getValue() && !mc.player.isHandActive()) {
            airSneaking = false;

            if (mc.getConnection() != null) {
                mc.getConnection().getNetworkManager().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
        }

        if (groundSneaking && strict.GetSwitch()) {
            if (!mc.player.isHandActive()) {
                groundSneaking = false;

                if (mc.getConnection() != null) {
                    mc.getConnection().getNetworkManager().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                }
            }
            if (airSneaking) {
                airSneaking = false;
                if (mc.getConnection() != null) {
                    mc.getConnection().getNetworkManager().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                }
            }
        }
    }

    @RegisterListener
    public void onItemInputUpdate(final ItemInputUpdateEvent event) {
        if (isSlowed()) {
            final float val = mc.player.onGround ? onGroundSpeed.GetSlider() : airSpeed.GetSlider();
            event.getMovementInput().moveForward *= val;
            event.getMovementInput().moveStrafe *= val;
        }
    }

    @RegisterListener
    public void onUseItem(final EntityUseItemEvent event) {
        if (isSlowed()) {
            if (airStrict.getValue() && !airSneaking) {
                airSneaking = true;
                if (mc.getConnection() != null) {
                    mc.getConnection().getNetworkManager().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                }
            }
            if (strict.GetSwitch() && !groundSneaking) {
                groundSneaking = true;
                if (mc.getConnection() != null) {
                    mc.getConnection().getNetworkManager().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                }
            }
        }

    }

    protected boolean isSlowed() {
        return (mc.player.isHandActive() && items.getValue()) && !mc.player.isRiding() && !mc.player.isElytraFlying();
    }
}