package dev.zprestige.fire.ui.font;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

public class FontRenderer {
    protected final ImageAWT defaultFont;
    protected final int FONT_HEIGHT;

    public FontRenderer(Font font) {
        defaultFont = new ImageAWT(font);
        FONT_HEIGHT = (int) getHeight();
    }

    public float getHeight() {
        return defaultFont.getHeight() / 2f;
    }

    public int getSize() {
        return defaultFont.getFont().getSize();
    }

    @ParametersAreNonnullByDefault
    public int drawStringWithShadow(String text, float x, float y, int color) {
        return drawString(text, x, y, color, true);
    }

    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        float currY = y - 3.0f;
        if (text.contains("\n")) {
            String[] parts = text.split("\n");
            float newY = 0.0f;

            for (String s : parts) {
                drawText(s, x, currY + newY, color, dropShadow);
                newY += getHeight();
            }

            return 0;
        }
        if (dropShadow)
            drawText(text, x + 0.4f, currY + 0.3f, new Color(0, 0, 0, 150).getRGB(), true);
        return drawText(text, x, currY, color, false);
    }

    private int drawText(String text, float x, float y, int color, boolean ignoreColor) {
        if (text == null) return 0;

        if (text.isEmpty()) return (int) x;

        GlStateManager.translate((double) x - 1.5, (double) y + 0.5, 0.0);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.enableTexture2D();

        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        int currentColor = color;

        if ((currentColor & 0xFC000000) == 0) currentColor |= 0xFF000000;


        defaultFont.drawString(text, 0.0, 0.0, currentColor);

        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GlStateManager.disableBlend();
        GlStateManager.translate(-((double) x - 1.5), -((double) y + 0.5), 0.0);

        return (int) (x + (float) getStringWidth(text));
    }

    public int getStringWidth(String text) {
        return defaultFont.getStringWidth(text) / 2;
    }
}