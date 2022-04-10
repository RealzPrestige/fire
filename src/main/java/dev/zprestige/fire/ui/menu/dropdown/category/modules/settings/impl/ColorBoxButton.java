package dev.zprestige.fire.ui.menu.dropdown.category.modules.settings.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.ClickGui;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.ui.menu.dropdown.MenuScreen;
import dev.zprestige.fire.ui.menu.dropdown.category.modules.settings.AbstractSetting;
import dev.zprestige.fire.util.impl.AnimationUtil;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class ColorBoxButton extends AbstractSetting {
    protected static Tessellator tessellator = Tessellator.getInstance();
    protected static BufferBuilder builder = tessellator.getBuffer();
    protected float scissorHeight;
    protected ColorBox setting;
    protected Color finalColor;
    protected boolean pickingColor = false, pickingHue = false, pickingAlpha = false, opened = false, dragging = false;

    public ColorBoxButton(ColorBox setting, Vector2D size) {
        super(setting, size);
        this.setting = setting;
        finalColor = setting.GetColor();
        scissorHeight = 0;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        RenderUtil.drawOutline(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY(), new Color(0, 0, 0, 30), 1.0f);
        final float y = position.getY() + size.getY() / 2 - Main.fontManager.getFontHeight() / 2;
        Main.fontManager.drawStringWithShadow(setting.getName(), new Vector2D(position.getX() + 2, y), -1);
        RenderUtil.drawRect(new Vector2D(position.getX() + size.getX() - 13, position.getY() + 3), new Vector2D(position.getX() + size.getX() - 4, position.getY() + size.getY() - 3), setting.GetColor().getRGB());
        RenderUtil.drawOutline(position.getX() + size.getX() - 13, position.getY() + 3, position.getX() + size.getX() - 4, position.getY() + size.getY() - 3, new Color(0, 0, 0, 30), 1.0f);
        RenderUtil.prepareScissor((int) position.getX() + 1, (int) (position.getY() + size.getY()), (int) size.getX() - 3, (int) scissorHeight);
        drawPicker(setting, (int) (position.getX() + 1), (int) ((int) position.getY() + size.getY() + 3), (int) position.getX() + 1, (int) (position.getY() + size.getY() + 92), (int) position.getX() + 1, (int) (position.getY() + size.getY() + 82), mouseX, mouseY);
        RenderUtil.releaseScissor();
        RenderUtil.prepareScissor((int) position.getX() - 10, (int) (position.getY() + size.getY()), (int) size.getX() + 10, (int) scissorHeight + 2);
        RenderUtil.drawOutline(position.getX(), position.getY() + size.getY() + 1, position.getX() + size.getX(), position.getY() + size.getY() + 121, new Color(0, 0, 0, 30), 1.0f);
        RenderUtil.drawOutline(position.getX() + 1, position.getY() + size.getY() + 3, position.getX() + size.getX() - 3, position.getY() + size.getY() + 81, new Color(0, 0, 0, 30), 1.0f);
        RenderUtil.drawOutline(position.getX() + 1, position.getY() + size.getY() + 82, position.getX() + size.getX() - 3, position.getY() + size.getY() + 89, new Color(0, 0, 0, 30), 1.0f);
        RenderUtil.drawOutline(position.getX() + 1, position.getY() + size.getY() + 92, position.getX() + size.getX() - 3, position.getY() + size.getY() + 99, new Color(0, 0, 0, 30), 1.0f);
        RenderUtil.drawOutline(position.getX() + 1, position.getY() + size.getY() + 100, position.getX() + (size.getX() / 2) - 1, position.getY() + size.getY() + 111, new Color(0, 0, 0, 30), 1.0f);
        Main.fontManager.drawStringWithShadow("Copy", new Vector2D(position.getX() + (size.getX() / 4) + 1 - Main.fontManager.getStringWidth("Copy") / 2, position.getY() + size.getY() + 100 + 5.5f - Main.fontManager.getFontHeight() / 2), -1);
        RenderUtil.drawOutline(position.getX() + (size.getX() / 2) + 1, position.getY() + size.getY() + 100, position.getX() + size.getX() - 1, position.getY() + size.getY() + 111, new Color(0, 0, 0, 30), 1.0f);
        Main.fontManager.drawStringWithShadow("Paste", new Vector2D(position.getX() + (size.getX() / 2) + 1 + size.getX() / 4 - Main.fontManager.getStringWidth("Paste") / 2, position.getY() + size.getY() + 100 + 5.5f - Main.fontManager.getFontHeight() / 2), -1);
        RenderUtil.releaseScissor();
        if (opened) {
            scissorHeight = AnimationUtil.increaseNumber(scissorHeight, 111, MenuScreen.getAnimationSpeedAccordingly(scissorHeight, 111));
        } else {
            scissorHeight = AnimationUtil.decreaseNumber(scissorHeight, 0, MenuScreen.getAnimationSpeedAccordingly(scissorHeight, 0));
        }
        if (opened) {
            setting.setValue(finalColor);
        }
    }

    protected boolean insideCopy(int mouseX, int mouseY) {
        return mouseX > position.getX() + 1 && mouseX < position.getX() + (size.getX() / 2) - 1 && mouseY > position.getY() + size.getY() + 100 && mouseY < position.getY() + size.getY() + 111;
    }

    protected boolean insidePaste(int mouseX, int mouseY) {
        return mouseX > position.getX() + (size.getX() / 2) + 1 && mouseX < position.getX() + size.getX() - 1 && mouseY > position.getY() + size.getY() + 100 && mouseY < position.getY() + size.getY() + 111;
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 && inside(mouseX, mouseY)) {
            opened = !opened;
        }
        if (opened) {
            if (insideCopy(mouseX, mouseY)) {
                copyClipBoard();
            }
            if (insidePaste(mouseX, mouseY)) {
                pasteClipBoard();
            }
        }
    }

    public void copyClipBoard() {
        String hex = finalColor.getRed() + "-" + finalColor.getGreen() + "-" + finalColor.getBlue() + "-" + finalColor.getAlpha();
        StringSelection selection = new StringSelection(hex);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    public void pasteClipBoard() {
        String string;
        try {
            string = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (IOException | UnsupportedFlavorException exception) {
            return;
        }
        try {
            String[] color1 = string.split("-");
            setting.setValue(new Color(Integer.parseInt(color1[0]), Integer.parseInt(color1[1]), Integer.parseInt(color1[2]), Integer.parseInt(color1[3])));
        } catch (Exception e) {
            Main.chatManager.sendMessage("Wrong color format. " + e.getMessage());
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int releaseButton) {
        dragging = false;
        pickingColor = pickingHue = pickingAlpha = false;
    }

    @Override
    public void type(char typedChar, int keyCode) {
    }

    public void forceScissorHeight() {
        scissorHeight = 0;
    }

    @Override
    public float getHeight() {
        return size.getY() + (scissorHeight > 3 ? scissorHeight : 0);
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public static boolean mouseOver(int minX, int minY, int maxX, int maxY, int mX, int mY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }

    public static Color getColor(Color color, float alpha) {
        final float red = (float) color.getRed() / 255;
        final float green = (float) color.getGreen() / 255;
        final float blue = (float) color.getBlue() / 255;
        return new Color(red, green, blue, alpha);
    }

    public static void drawPickerBase(int pickerX, int pickerY, int pickerWidth, int pickerHeight, float red, float green, float blue) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_POLYGON);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glVertex2f(pickerX, pickerY);
        glVertex2f(pickerX, pickerY + pickerHeight);
        glColor4f(red, green, blue, 255f);
        glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        glVertex2f(pickerX + pickerWidth, pickerY);
        glEnd();
        glDisable(GL_ALPHA_TEST);
        glBegin(GL_POLYGON);
        glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        glVertex2f(pickerX, pickerY);
        glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        glVertex2f(pickerX, pickerY + pickerHeight);
        glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        glVertex2f(pickerX + pickerWidth, pickerY);
        glEnd();
        glEnable(GL_ALPHA_TEST);
        glShadeModel(GL_FLAT);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void drawGradientRect(final double leftpos, final double top, final double right, final double bottom, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(leftpos, top);
        GL11.glVertex2d(leftpos, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL_SMOOTH);
        builder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(right, top, 0).color((float) (endColor >> 24 & 255) / 255.0F, (float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(left, top, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(left, bottom, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(right, bottom, 0).color((float) (endColor >> 24 & 255) / 255.0F, (float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void gradient(int minX, int minY, int maxX, int maxY, int startColor, int endColor, boolean left) {
        if (left) {
            final float startA = (startColor >> 24 & 0xFF) / 255.0f;
            final float startR = (startColor >> 16 & 0xFF) / 255.0f;
            final float startG = (startColor >> 8 & 0xFF) / 255.0f;
            final float startB = (startColor & 0xFF) / 255.0f;

            final float endA = (endColor >> 24 & 0xFF) / 255.0f;
            final float endR = (endColor >> 16 & 0xFF) / 255.0f;
            final float endG = (endColor >> 8 & 0xFF) / 255.0f;
            final float endB = (endColor & 0xFF) / 255.0f;

            glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL_SMOOTH);
            GL11.glBegin(GL11.GL_POLYGON);
            {
                GL11.glColor4f(startR, startG, startB, startA);
                GL11.glVertex2f(minX, minY);
                GL11.glVertex2f(minX, maxY);
                GL11.glColor4f(endR, endG, endB, endA);
                GL11.glVertex2f(maxX, maxY);
                GL11.glVertex2f(maxX, minY);
            }
            GL11.glEnd();
            GL11.glShadeModel(GL11.GL_FLAT);
            glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        } else drawGradientRect(minX, minY, maxX, maxY, startColor, endColor);
    }

    public static int gradientColor(int color, int percentage) {
        int r = (((color & 0xFF0000) >> 16) * (100 + percentage) / 100);
        int g = (((color & 0xFF00) >> 8) * (100 + percentage) / 100);
        int b = ((color & 0xFF) * (100 + percentage) / 100);
        return new Color(r, g, b).hashCode();
    }

    public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor, boolean hovered) {
        if (hovered) {
            startColor = gradientColor(startColor, -20);
            endColor = gradientColor(endColor, -20);
        }
        float c = (float) (startColor >> 24 & 255) / 255.0F;
        float c1 = (float) (startColor >> 16 & 255) / 255.0F;
        float c2 = (float) (startColor >> 8 & 255) / 255.0F;
        float c3 = (float) (startColor & 255) / 255.0F;
        float c4 = (float) (endColor >> 24 & 255) / 255.0F;
        float c5 = (float) (endColor >> 16 & 255) / 255.0F;
        float c6 = (float) (endColor >> 8 & 255) / 255.0F;
        float c7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(left, top, 0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(left, bottom, 0).color(c5, c6, c7, c4).endVertex();
        bufferbuilder.pos(right, bottom, 0).color(c5, c6, c7, c4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }


    public void drawPicker(ColorBox setting, int pickerX, int pickerY, int hueSliderX, int hueSliderY, int alphaSliderX, int alphaSliderY, int mouseX, int mouseY) {
        float[] color = new float[]{
                0, 0, 0, 0
        };

        try {
            color = new float[]{
                    Color.RGBtoHSB(setting.GetColor().getRed(), setting.GetColor().getGreen(), setting.GetColor().getBlue(), null)[0], Color.RGBtoHSB(setting.GetColor().getRed(), setting.GetColor().getGreen(), setting.GetColor().getBlue(), null)[1], Color.RGBtoHSB(setting.GetColor().getRed(), setting.GetColor().getGreen(), setting.GetColor().getBlue(), null)[2], setting.GetColor().getAlpha() / 255f
            };
        } catch (Exception ignored) {

        }

        final int pickerWidth = (int) (size.getX() - 3);
        final int pickerHeight = 78;
        final int hueSliderWidth = pickerWidth + 8;
        final int hueSliderHeight = 7;
        final int alphaSliderHeight = 7;

        if (pickingColor) {
            if (!(Mouse.isButtonDown(0) && mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY))) {
                pickingColor = false;
            }
        }

        if (pickingHue) {
            if (!(Mouse.isButtonDown(0) && mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight, mouseX, mouseY))) {
                pickingHue = false;
            }
        }

        if (pickingAlpha) {
            if (!(Mouse.isButtonDown(0) && mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + pickerWidth, alphaSliderY + alphaSliderHeight, mouseX, mouseY)))
                pickingAlpha = false;
        }

        if (Mouse.isButtonDown(0) && mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY))
            pickingColor = true;
        if (Mouse.isButtonDown(0) && mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight, mouseX, mouseY))
            pickingHue = true;
        if (Mouse.isButtonDown(0) && mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + pickerWidth, alphaSliderY + alphaSliderHeight, mouseX, mouseY))
            pickingAlpha = true;

        if (pickingHue) {
            float restrictedX = (float) Math.min(Math.max(hueSliderX, mouseX), hueSliderX + hueSliderWidth - 7);
            color[0] = (restrictedX - (float) hueSliderX) / hueSliderWidth;
        }

        if (pickingAlpha) {
            float restrictedX = (float) Math.min(Math.max(alphaSliderX, mouseX), alphaSliderX + pickerWidth);
            color[3] = 1 - (restrictedX - (float) alphaSliderX) / pickerWidth;
        }

        if (pickingColor) {
            float restrictedX = (float) Math.min(Math.max(pickerX, mouseX), pickerX + pickerWidth);
            float restrictedY = (float) Math.min(Math.max(pickerY, mouseY), pickerY + pickerHeight);
            color[1] = (restrictedX - (float) pickerX) / pickerWidth;
            color[2] = 1 - (restrictedY - (float) pickerY) / pickerHeight;
        }

        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);

        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;

        drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue);

        drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth - 2, hueSliderHeight, color[0]);

        int cursorX = (int) (pickerX + color[1] * pickerWidth);
        int cursorY = (int) ((pickerY + pickerHeight) - color[2] * pickerHeight);

        Gui.drawRect(cursorX - 1, cursorY - 1, cursorX + 1, cursorY + 1, -1);
        RenderUtil.drawOutline(cursorX - 1, cursorY - 1, cursorX + 1, cursorY + 1, ClickGui.Instance.color.GetColor(), 1.0f);


        drawAlphaSlider(alphaSliderX, alphaSliderY, pickerWidth, alphaSliderHeight, selectedRed, selectedGreen, selectedBlue, color[3]);

        finalColor = getColor(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), color[3]);
    }

    public void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step = 0;
        if (height > width) {
            RenderUtil.drawRect(new Vector2D(x, y), new Vector2D(x + width, y + 4), 0xFFFF0000);
            y += 4;

            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                drawGradientRect(x, y + step * (height / 6f), x + width, y + (step + 1) * (height / 6f), previousStep, nextStep, false);
                step++;
            }
            int sliderMinY = (int) (y + height * hue) - 4;
            RenderUtil.drawRect(new Vector2D(x, sliderMinY - 1), new Vector2D(x + width, sliderMinY + 1), -1);
            RenderUtil.drawOutline(x, sliderMinY - 1, x + width, sliderMinY + 1, Color.BLACK, 1);
            RenderUtil.drawOutline(x, sliderMinY - 1, x + width, sliderMinY + 1, ClickGui.Instance.color.GetColor(), 1.0f);
        } else {
            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                gradient(x + step * (width / 6), y, x + (step + 1) * (width / 6), y + height, previousStep, nextStep, true);
                step++;
            }

            int sliderMinX = (int) (x + (width * hue));
            RenderUtil.drawRect(new Vector2D(sliderMinX - 1, y), new Vector2D(sliderMinX + 1, y + height), -1);
            RenderUtil.drawOutline(sliderMinX - 1, y, sliderMinX + 1, y + height, Color.BLACK, 1);
            RenderUtil.drawOutline(sliderMinX - 1, y, sliderMinX + 1, y + height, ClickGui.Instance.color.GetColor(), 1.0f);
        }
    }

    public void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue, float alpha) {

        boolean left = true;
        int checkerBoardSquareSize = height / 2;

        for (int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                RenderUtil.drawRect(new Vector2D(x + squareIndex, y), new Vector2D(x + squareIndex + checkerBoardSquareSize, y + height), 0xFFFFFFFF);
                RenderUtil.drawRect(new Vector2D(x + squareIndex, y + checkerBoardSquareSize), new Vector2D(x + squareIndex + checkerBoardSquareSize, y + height), 0xFF909090);

                if (squareIndex < width - checkerBoardSquareSize) {
                    int minX = x + squareIndex + checkerBoardSquareSize;
                    int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
                    RenderUtil.drawRect(new Vector2D(minX, y), new Vector2D(maxX, y + height), 0xFF909090);
                    RenderUtil.drawRect(new Vector2D(minX, y + checkerBoardSquareSize), new Vector2D(maxX, y + height), 0xFFFFFFFF);
                }
            }

            left = !left;
        }

        drawLeftGradientRect(x, y, x + width + 13, y + height, new Color(red, green, blue, 1).getRGB(), 0);
        int sliderMinX = (int) (x + width - (width * alpha));
        RenderUtil.drawRect(new Vector2D(sliderMinX - 1, y), new Vector2D(sliderMinX + 1, y + height), -1);
        RenderUtil.drawOutline(sliderMinX - 1, y, sliderMinX + 1, y + height, Color.BLACK, 1);
        RenderUtil.drawOutline(sliderMinX - 1, y, sliderMinX + 1, y + height, ClickGui.Instance.color.GetColor(), 1.0f);
    }
}
