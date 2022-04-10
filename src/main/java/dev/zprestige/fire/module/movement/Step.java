package dev.zprestige.fire.module.movement;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.MoveEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.network.play.client.CPacketPlayer;

@Descriptor(description = "Allows you to teleport up to 2 blocks high")
public class Step extends Module {
    public final ComboBox mode = Menu.ComboBox("Mode", "Vanilla", new String[]{
            "Vanilla",
            "Ncp"
    });
    public final Slider height = Menu.Slider("Height", 2.0f, 1.0f, 2.0f);
    public final Switch entities = Menu.Switch("Entities", false);
    protected final float[] singleOffsets = {0.42f, 0.753f}, doubleOffsets = {0.42f, 0.78f, 0.63f, 0.51f, 0.9f, 1.21f, 1.45f, 1.43f};

    @Override
    public void onDisable() {
        Main.tickManager.syncTimer();
        mc.player.stepHeight = 0.6f;
    }

    @RegisterListener
    public void onMove(final MoveEvent event) {
        if (checkRiding()) {
            switch (mode.GetCombo()) {
                case "Vanilla":
                    mc.player.stepHeight = height.GetSlider();
                    break;
                case "Ncp":
                    if (canStep()) {
                        final int height = (int) this.height.GetSlider();
                        if (checkEmpty(height)) {
                            performStep(height, EntityUtil.getSpeed(0.1f));
                        }
                    }
                    break;
            }
        }
    }

    protected boolean checkRiding() {
        return !(!entities.GetSwitch() && mc.player.isRiding());
    }

    protected boolean checkEmpty(final int amount) {
        final float[] i = EntityUtil.getSpeed(0.1f);
        return amount == 1 ? checkFirstHeight(i) : (checkFirstHeight(i)) || (isBoundingEmpty(i, 2.1f) && !isBoundingEmpty(i, 1.9f));
    }

    protected boolean checkFirstHeight(final float[] i) {
        return isBoundingEmpty(i, 1.1f) && !isBoundingEmpty(i, 0.9f);
    }

    protected boolean canStep() {
        return mc.player.collidedHorizontally && mc.player.onGround;
    }

    protected void performStep(final int amount, final float[] i) {
        sendOffsets(amount, i);
    }

    protected void sendOffsets(final int amount, final float[] i) {
        for (float j : amount == 1 ? singleOffsets : checkFirstHeight(i) ? singleOffsets : doubleOffsets) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + j, mc.player.posZ, mc.player.onGround));
        }
        mc.player.setPosition(mc.player.posX, mc.player.posY + (amount == 2 ? checkFirstHeight(i) ? 1 : 2 : 1), mc.player.posZ);
    }

    protected boolean isBoundingEmpty(final float[] i, final float y) {
        return mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(i[0], y, i[1])).isEmpty();
    }
}
