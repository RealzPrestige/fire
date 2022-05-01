package dev.zprestige.fire.manager.shrinkmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ShrinkManager {
    protected final HashMap<ShrinkPosition, Float> shrinkPositions = new HashMap<>();

    public ShrinkManager() {
        Main.eventBus.registerListeners(new EventListener[]{
                new Frame3DListener()
        });
    }

    protected boolean isOccupied(final BlockPos pos) {
        return shrinkPositions.entrySet().stream().anyMatch(entry -> entry.getKey().getPos().equals(pos));
    }

    public void removePosition(final BlockPos pos) {
        new HashMap<>(shrinkPositions).keySet().stream().filter(aFloat -> aFloat.getPos().equals(pos)).forEach(shrinkPositions::remove);
    }

    public void createShrinkPosition(final BlockPos pos, final Color boxColor, final Color outlineColor, final boolean box, final boolean outline, final float lineWidth, final float speed) {
        final ShrinkPosition shrinkPosition = new ShrinkPosition(pos, boxColor, outlineColor, box, outline, lineWidth, speed);
        if (!isOccupied(pos)) {
            addPosition(shrinkPosition, 0.001f);
        } else {
            for (Map.Entry<ShrinkPosition, Float> entry : shrinkPositions.entrySet()) {
                final ShrinkPosition shrinkPosition1 = entry.getKey();
                if (shrinkPosition1.getPos().equals(pos)) {
                    shrinkPositions.put(shrinkPosition1, 0.001f);
                }
            }
        }
    }

    public void addPosition(ShrinkPosition shrinkPosition, float startAlpha) {
        shrinkPositions.put(shrinkPosition, startAlpha);
    }

    public static class ShrinkPosition {
        protected final BlockPos pos;
        protected final Color boxColor, outlineColor;
        protected final boolean box, outline;
        protected final float lineWidth, speed;

        public ShrinkPosition(final BlockPos pos, final Color boxColor, final Color outlineColor, final boolean box, final boolean outline, final float lineWidth, final float speed) {
            this.pos = pos;
            this.boxColor = boxColor;
            this.outlineColor = outlineColor;
            this.box = box;
            this.outline = outline;
            this.lineWidth = lineWidth;
            this.speed = speed;
        }

        public BlockPos getPos() {
            return pos;
        }

        public void render(final float shrink) {
            final AxisAlignedBB bb = new AxisAlignedBB(pos).shrink(shrink);
            if (box) {
                RenderUtil.drawBBBoxWithHeight(bb, boxColor, 1);
            }
            if (outline) {
                RenderUtil.drawBlockOutlineBBWithHeight(bb, outlineColor, lineWidth, 1);
            }
        }
    }
}
