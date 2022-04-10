package dev.zprestige.fire.module.movement;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.MoveEvent;
import dev.zprestige.fire.events.impl.PacketEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

import java.util.Objects;

@Descriptor(description = "Allows you to jump further than usual")
public class LongJump extends Module {
    public final Slider factor = Menu.Slider("Factor", 5.0f, 0.1f, 20.0f);
    public final Slider accelerationFactor = Menu.Slider("Acceleration Factor", 2.0f, 0.1f, 10.0f);
    public final Slider verticalFactor = Menu.Slider("Vertical Factor", 4.0f, 0.1f, 6.0f);
    public final Switch useTimer = Menu.Switch("Use Timer", false);
    public final Slider timerAmount = Menu.Slider("Timer Amount", 1.0f, 0.9f, 2.0f).visibility(z -> useTimer.GetSwitch());
    public final Switch liquids = Menu.Switch("Liquids", false);
    public final Switch disableOnLag = Menu.Switch("Disable On Lag", false);
    protected double previousDistance, motionSpeed;
    protected int currentState = 1;

    @Override
    public void onDisable() {
        Main.tickManager.syncTimer();
    }

    @RegisterListener
    public void onTick(final TickEvent event) {
        previousDistance = Math.sqrt((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX) + (mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ));
        if (useTimer.GetSwitch()) {
            Main.tickManager.setTimer(timerAmount.GetSlider());
        }
    }

    @RegisterListener
    public void onMove(final MoveEvent event) {
        if (liquids.GetSwitch() || !(mc.player.isInLava() || mc.player.isInWater())) {
            switch (currentState) {
                case 0:
                    currentState++;
                    previousDistance = 0.0;
                    break;
                case 1:
                default:
                    if ((mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size() > 0 || mc.player.collidedVertically) && currentState > 0) {
                        currentState = mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F ? 0 : 1;
                    }
                    motionSpeed = previousDistance - previousDistance / 159.0;
                    break;
                case 2:
                    double height = verticalFactor.GetSlider() / 10.0f ;
                    if ((mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) && mc.player.onGround) {
                        if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                            height += (float) (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier() + 1) * 0.1f;
                        }
                        event.setMotionY((mc.player.motionY = height));
                        motionSpeed *= accelerationFactor.GetSlider();
                    }
                    break;
                case 3:
                    motionSpeed = previousDistance - 0.76 * (previousDistance - EntityUtil.getBaseMotionSpeed() * factor.GetSlider());
            }
            motionSpeed = Math.max(motionSpeed, EntityUtil.getBaseMotionSpeed() * factor.GetSlider());
            double moveForward = mc.player.movementInput.moveForward;
            double moveStrafe = mc.player.movementInput.moveStrafe;
            double yaw = mc.player.rotationYaw;
            if (moveForward != 0.0 && moveStrafe != 0.0) {
                moveForward *= Math.sin(0.7853981633974483);
                moveStrafe *= Math.cos(0.7853981633974483);
            }
            final double sin = Math.sin(Math.toRadians(yaw));
            final double cos = Math.cos(Math.toRadians(yaw));
            event.setMotionX((moveForward * motionSpeed * -sin) + (moveStrafe * motionSpeed * cos) * 0.99);
            event.setMotionZ((moveForward * motionSpeed * cos) - (moveStrafe * motionSpeed * -sin) * 0.99);
            currentState++;
        }
    }

    @RegisterListener
    public void onPacketReceive(final PacketEvent.PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            if (disableOnLag.GetSwitch()) {
                disableModule();
            }
            motionSpeed = 0.0f;
            mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());
        }
    }
}
