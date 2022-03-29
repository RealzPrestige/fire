package dev.zprestige.fire.manager;

import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FadeManager extends RegisteredClass {
    protected final HashMap<FadePosition, Float> fadePositions = new HashMap<>();

    @RegisterListener
    public void onFrame3D(FrameEvent.FrameEvent3D event) {
        final HashMap<FadePosition, Float> fadePositions1 = new HashMap<>(fadePositions);
        for (Map.Entry<FadePosition, Float> entry : fadePositions1.entrySet()) {
            final FadePosition fadePosition = entry.getKey();
            final float alpha = entry.getValue();
            final float newAlpha = alpha - (alpha / (102.0f - fadePosition.speed));
            if (entry.getValue() <= 10) {
                fadePositions.remove(fadePosition);
                continue;
            }
            fadePositions.put(fadePosition, newAlpha);
            fadePosition.render(newAlpha);
        }
    }

    protected boolean isOccupied(BlockPos pos) {
        return fadePositions.entrySet().stream().anyMatch(entry -> entry.getKey().getPos().equals(pos));
    }

    public void removePosition(final BlockPos pos){
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
