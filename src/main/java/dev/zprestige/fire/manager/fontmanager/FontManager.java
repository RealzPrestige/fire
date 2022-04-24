package dev.zprestige.fire.manager.fontmanager;

import dev.zprestige.fire.module.client.customfont.CustomFont;
import dev.zprestige.fire.ui.font.FontRenderer;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.InputStream;

public class FontManager {
    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final int FONT_HEIGHT = mc.fontRenderer.FONT_HEIGHT;
    protected FontRenderer customFont;

    public FontManager() {
        loadFont(35);
    }

    public void loadFont(int size) {
        customFont = new FontRenderer(getFont(size));
    }

    public float getFontHeight() {
        return CustomFont.Instance.isEnabled() ? customFont.getHeight() / 2 : FONT_HEIGHT;
    }

    public float getStringWidth(String text) {
        if (CustomFont.Instance.isEnabled()) {
            return customFont.getStringWidth(text);
        }
        return mc.fontRenderer.getStringWidth(text);
    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        if (CustomFont.Instance.isEnabled()) {
            customFont.drawStringWithShadow(text, x, y, color);
        } else {
            mc.fontRenderer.drawStringWithShadow(text, x, y, color);
        }
    }
    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if (CustomFont.Instance.isEnabled()) {
            customFont.drawString(text, x, y, color, shadow);
        } else {
            mc.fontRenderer.drawString(text, x, y, color, shadow);
        }
    }

    private Font getFont(float size) {
        final Font plain = new Font("default", Font.PLAIN, (int) size);
        try {
            InputStream inputStream = FontManager.class.getResourceAsStream("/assets/minecraft/textures/font/" + "Font" + ".ttf");

            if (inputStream != null) {
                Font awtClientFont = Font.createFont(0, inputStream);
                awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
                inputStream.close();
                return awtClientFont;
            }
            return plain;
        } catch (Exception exception) {
            return plain;
        }
    }
}
