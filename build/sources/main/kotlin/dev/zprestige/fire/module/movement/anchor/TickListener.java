package dev.zprestige.fire.module.movement.anchor;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.manager.holemanager.HoleManager;
import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.util.impl.BlockUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.TreeMap;

public class TickListener extends EventListener<TickEvent, Anchor> {

    public TickListener(final Anchor anchor) {
        super(TickEvent.class, anchor);
    }

    @Override
    public void invoke(final Object object) {
        if (module.pos != null && mc.player.getDistanceSq(module.pos) > module.range.GetSlider() * 2.0f) {
            module.pos = null;
        }
        if (module.pos != null && !Main.holeManager.contains(module.pos)) {
            module.pos = null;
        }
        if (BlockUtil.isPlayerSafe(new PlayerManager.Player(mc.player))) {
            module.pos = null;
            module.timer.syncTime();
            return;
        }
        if (module.pos == null) {
            final TreeMap<Double, BlockPos> posses = new TreeMap<>();
            for (final HoleManager.HolePos holePos : Main.holeManager.getHoles()) {
                if (holePos.isDouble()) {
                    continue;
                }
                final BlockPos pos1 = holePos.getPos();
                final double dist = mc.player.getDistanceSq(pos1);
                if (module.mode.GetCombo().equals("Snap") ? mc.player.posY > pos1.getY() : mc.player.posY >= pos1.getY() && dist < module.range.GetSlider() * 2.0f) {
                    posses.put(dist, pos1);
                }
            }
            if (!posses.isEmpty()) {
                module.pos = posses.firstEntry().getValue();
            }
        } else if (module.timer.getTime(1000)) {
            if (module.pos.getY() < mc.player.posY) {
                switch (module.mode.GetCombo()) {
                    case "Stop Motion":
                        if (module.isOver(module.pos) && module.intersects(module.pos)) {
                            mc.player.motionX = 0;
                            mc.player.motionZ = 0;
                            module.setMovementsFalse();
                        }
                        break;
                    case "Pull":
                        final AxisAlignedBB bb = new AxisAlignedBB(module.pos).shrink(0.35f);
                        final float speed = module.pullSpeed.GetSlider() / 10.0f;
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
                        module.setMovementsFalse();
                        break;
                    case "Snap":
                        if (!module.onGround.GetSwitch() || mc.player.onGround) {
                            mc.player.setPosition(module.pos.getX() + 0.5f, mc.player.posY, module.pos.getZ() + 0.5f);
                        }
                        mc.player.motionY = -module.snapFall.GetSlider();
                        module.setMovementsFalse();
                        break;
                }
            }
        }
    }
}
