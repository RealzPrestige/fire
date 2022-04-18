package dev.zprestige.fire.module.movement;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.manager.HoleManager;
import dev.zprestige.fire.manager.PlayerManager;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.TreeMap;

@Descriptor(description = "Makes getting into holes easier")
public class Anchor extends Module {
    public final ComboBox mode = Menu.ComboBox("Mode", "Stop Motion", new String[]{
            "Stop Motion",
            "Pull",
            "Snap"
    });
    public final Slider snapFall = Menu.Slider("Snap Fall", 1.0f, 0.1f, 5.0f).visibility(z -> mode.GetCombo().equals("Snap"));
    public final Slider pullSpeed = Menu.Slider("Pull Speed", 1.0f, 0.1f, 5.0f).visibility(z -> mode.GetCombo().equals("Pull"));
    public final Slider height = Menu.Slider("Height", 1.0f, 0.1f, 5.0f).visibility(z -> mode.GetCombo().equals("Stop Motion"));
    public final Slider range = Menu.Slider("Range", 1.0f, 0.1f, 5.0f);
    public final Switch onGround = Menu.Switch("On Ground", true).visibility(z -> mode.GetCombo().equals("Snap"));
    protected final Vec3i[] offsets = new Vec3i[]{
      new Vec3i(0, 0, -1),
      new Vec3i(0, 0, 1),
      new Vec3i(-1, 0, 0),
      new Vec3i(1, 0, 0)
    };
    protected final Timer timer = new Timer();
    protected BlockPos pos;

    @RegisterListener
    public void onTick(final TickEvent event) {
        if (pos != null && mc.player.getDistanceSq(pos) > range.GetSlider() * 2.0f) {
            pos = null;
        }
        if (pos != null && !Main.holeManager.contains(pos)) {
            pos = null;
        }
        if (BlockUtil.isPlayerSafe(new PlayerManager.Player(mc.player))) {
            pos = null;
            timer.syncTime();
            return;
        }
        if (pos == null) {
            final TreeMap<Double, BlockPos> posses = new TreeMap<>();
            for (final HoleManager.HolePos holePos : Main.holeManager.getHoles()) {
                if (holePos.isDouble()) {
                    continue;
                }
                final BlockPos pos1 = holePos.getPos();
                final double dist = mc.player.getDistanceSq(pos1);
                if (mode.GetCombo().equals("Snap") ? mc.player.posY > pos1.getY() : mc.player.posY >= pos1.getY() && dist < range.GetSlider() * 2.0f) {
                    posses.put(dist, pos1);
                }
            }
            if (!posses.isEmpty()) {
                pos = posses.firstEntry().getValue();
            }
        } else if (timer.getTime(1000)) {
            if (pos.getY() < mc.player.posY) {
                switch (mode.GetCombo()) {
                    case "Stop Motion":
                        if (isOver(pos) && intersects(pos)) {
                            mc.player.motionX = 0;
                            mc.player.motionZ = 0;
                            setMovementsFalse();
                        }
                        break;
                    case "Pull":
                            final AxisAlignedBB bb = new AxisAlignedBB(pos).shrink(0.35f);
                            final float speed = pullSpeed.GetSlider() / 10.0f;
                            if (mc.player.posX > bb.maxX) {
                                mc.player.motionX = -speed;
                            } else if (mc.player.posX < bb.minX) {
                                mc.player.motionX = speed;
                            }
                            if (mc.player.posZ > bb.maxZ) {
                                mc.player.motionZ = -speed;
                            } else if (mc.player.posZ < bb.minZ) {
                                mc.player.motionZ = speed;
                            }
                            setMovementsFalse();
                        break;
                    case "Snap":
                        if (!onGround.GetSwitch() || mc.player.onGround) {
                            mc.player.setPosition(pos.getX() + 0.5f, mc.player.posY, pos.getZ() + 0.5f);
                        }
                        mc.player.motionY = -snapFall.GetSlider();
                        setMovementsFalse();
                        break;
                }
            }
        }
    }

    protected void setMovementsFalse(){
        mc.gameSettings.keyBindForward.pressed = false;
        mc.gameSettings.keyBindBack.pressed = false;
        mc.gameSettings.keyBindRight.pressed = false;
        mc.gameSettings.keyBindLeft.pressed = false;
    }

    protected boolean isOver(final BlockPos pos){
        final AxisAlignedBB bb = mc.player.getEntityBoundingBox();
        for (int i = 0; i < height.GetSlider(); i++){
            final BlockPos pos1 = pos.up(i);
            final AxisAlignedBB bb1 = new AxisAlignedBB(pos1);
            if (bb.intersects(bb1)){
                return true;
            }
        }
        return false;
    }

    protected boolean intersects(final BlockPos pos){
        final AxisAlignedBB bb = mc.player.getEntityBoundingBox();
        for (int i = 0; i < 5; i++) {
            for (final Vec3i vec3i : offsets) {
                final BlockPos pos1 = pos.up(i).add(vec3i);
                final AxisAlignedBB bb1 = new AxisAlignedBB(pos1);
                if (bb.intersects(bb1)){
                    return false;
                }
            }
        }
        return true;
    }
}
