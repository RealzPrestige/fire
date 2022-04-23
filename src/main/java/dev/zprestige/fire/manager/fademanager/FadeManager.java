package dev.zprestige.fire.manager.fademanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FadeManager {
    protected final HashMap<FadePosition, Float> fadePositions = new HashMap<>();

    public FadeManager() {
        Main.newBus.registerListeners(new EventListener[]{
                new Frame3DListener()
        });
    }

    protected boolean isOccupied(BlockPos pos) {
        return fadePositions.entrySet().stream().anyMatch(entry -> entry.getKey().getPos().equals(pos));
    }

    public void removePosition(final BlockPos pos) {
        new HashMap<>(fadePositions).keySet().stream().filter(aFloat -> aFloat.getPos().equals(pos)).forEach(fadePositions::remove);
    }

    public void createFadePosition(final BlockPos pos, final Color boxColor, final Color outlineColor, final boolean box, final boolean outline, final float lineWidth, final float speed, float startAlpha) {
        final FadePosition fadePosition = new FadePosition(pos, boxColor, outlineColor, box, outline, lineWidth, speed);
        if (!isOccupied(pos)) {
            addPosition(fadePosition, startAlpha);
        } else {
            for (Map.Entry<FadePosition, Float> entry : fadePositions.entrySet()) {
                final FadePosition fadePosition1 = entry.getKey();
                if (fadePosition1.getPos().equals(pos)) {
                    fadePositions.put(fadePosition1, startAlpha);
                }
            }
        }
    }

    public void addPosition(FadePosition fadePosition, float startAlpha) {
        fadePositions.put(fadePosition, startAlpha);
    }

    public static class FadePosition {
        protected final BlockPos pos;
        protected final Color boxColor, outlineColor;
        protected final boolean box, outline;
        protected final float lineWidth, speed;

        public FadePosition(final BlockPos pos, final Color boxColor, final Color outlineColor, final boolean box, final boolean outline, final float lineWidth, final float speed) {
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

        public void render(float alpha) {
            final Color newBoxColor = new Color(boxColor.getRed() / 255f, boxColor.getGreen() / 255f, boxColor.getBlue() / 255f, alpha / 255f);
            final Color newOutlineColor = new Color(outlineColor.getRed() / 255f, outlineColor.getGreen() / 255f, outlineColor.getBlue() / 255f, alpha / 255f);
            if (box) {
                RenderUtil.drawBox(pos, newBoxColor);
            }
            if (outline) {
                RenderUtil.drawOutline(pos, newOutlineColor, lineWidth);
            }
        }
    }
}
