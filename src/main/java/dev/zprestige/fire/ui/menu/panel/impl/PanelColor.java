package dev.zprestige.fire.ui.menu.panel.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.ui.menu.panel.PanelScreen;
import dev.zprestige.fire.ui.menu.panel.PanelSetting;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * Thanks wallhacks for epic SPARK CLIENT! !!!!!!
 * now i steal tiny bit of code idk gl
 */
public class PanelColor extends PanelSetting {
    protected final ColorBox setting;
    protected final ColorPicker picker;
    protected final AlphaSlider alpha;
    protected final HueSlider hue;
    protected final Clickable paste, copy, hex;

    public PanelColor(final ColorBox setting, final float x, final float y, final float width, final float height) {
        super(setting, x, y, width, height);
        this.setting = setting;
        paste = new Clickable("Paste", x + 99, y + 18, 25, 12) {
            @Override
            public void action() {
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
        };
        copy = new Clickable("Copy", x + 99, y + 33, 25, 12) {
            @Override
            public void action() {
                String hex = setting.GetColor().getRed() + "-" + setting.GetColor().getGreen() + "-" + setting.GetColor().getBlue() + "-" + setting.GetColor().getAlpha();
                StringSelection selection = new StringSelection(hex);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
        };
        hex = new Clickable("", x + 99, y + 48, 25, 12);
        alpha = new AlphaSlider("A", (int) (x + 86), (int) (y + 16), 10, 46);
        hue = new HueSlider((int) (x + 72), (int) (y + 16), 10, 46);
        picker = new ColorPicker((int) x, (int) (y + 16), 68, 46);
    }

    boolean isExtended = false;


    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        if (!setting.isVisible()){
            isExtended = false;
        }
        final float y = this.y - secondY;
        RenderUtil.prepareScale(0.73f);
        Main.fontManager.drawStringWithShadow(setting.getName(), new Vector2D(x / 0.73f, (y + 7.5f - Main.fontManager.getFontHeight() / 2.0f) / 0.73f), new Color(col[0], col[1], col[2], 1.0f).getRGB());
        RenderUtil.releaseScale();
        RenderUtil.drawRoundedRect(x + width - 12, y + 3, x + width - 3, y + 12, 6, PanelScreen.thirdBackgroundColor);
        RenderUtil.drawRoundedRect(x + width - 11, y + 4, x + width - 4, y + 11, 6, PanelScreen.secondBackgroundColor);
        RenderUtil.drawRoundedRect(x + width - 11, y + 4, x + width - 4, y + 11, 6, setting.GetColor());
        if (isExtended) {
            drawPicker(setting, mouseX, mouseY);
            paste.setX(x + 99);
            paste.setY(y + 18);
            paste.render(mouseX, mouseY);
            copy.setX(x + 99);
            copy.setY(y + 33);
            copy.render(mouseX, mouseY);
            hex.setX(x + 99);
            hex.setY(y + 48);
            hex.setDisplayString(String.format("#%06x", setting.GetColor().getRGB() & 0xFFFFFF));
            hex.render(mouseX, mouseY);
        }
        height = isExtended ? 65 : 15;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public void drawPicker(ColorBox subColor, int mouseX, int mouseY) {
        final float y = this.y - secondY;
        alpha.updatePositionAndSize((int) (x + 86), (int) (y + 4 + 12), 10, 46);
        hue.updatePositionAndSize((int) (x + 72), (int) (y + 4 + 12), 10, 46);
        picker.updatePositionAndSize((int) x, (int) (y + 4 + 12), 68, 46);
        int r = subColor.GetColor().getRed();
        int g = subColor.GetColor().getGreen();
        int b = subColor.GetColor().getBlue();
        int a = subColor.GetColor().getAlpha();
        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        if (!Mouse.isButtonDown(0)) {
            alpha.mouseReleased();
            hue.mouseReleased();
            picker.mouseReleased();
        }

        if (hue.picking) {
            hsb[0] = hue.pick(mouseY, hsb[0]);
            int alpha = subColor.GetColor().getAlpha();
            subColor.setValue(new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])));
            subColor.setValue(new Color(subColor.GetColor().getRed(), subColor.GetColor().getGreen(), subColor.GetColor().getBlue(), alpha));
        }

        if (picker.picking) {
            hsb[1] = picker.pickSaturation(mouseX, hsb[1]);
            hsb[2] = picker.pickBrigthness(mouseY, hsb[2]);
            int alpha = subColor.GetColor().getAlpha();
            subColor.setValue(new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])));
            subColor.setValue(new Color(subColor.GetColor().getRed(), subColor.GetColor().getGreen(), subColor.GetColor().getBlue(), alpha));
        }

        if (alpha.picking) {
            int a1 = alpha.pick(mouseY, 255 - a);
            subColor.setValue(new Color(subColor.GetColor().getRed() / 255f, subColor.GetColor().getGreen() / 255f, subColor.GetColor().getBlue() / 255f, Math.max(0.02f, 1 - a1 / 255f)));
        }

        picker.updatePickerLocation(hsb[1], hsb[2]);
        picker.drawColorPicker(r, b, g);

        hue.updateSliderLocation(Color.RGBtoHSB(r, g, b, null)[0]);
        hue.drawSlider();

        alpha.updateSliderLocation(255 - a);
        alpha.drawSlider(r / 255f, g / 255f, b / 255f, a / 255.0f);
    }

    @Override
    public void click(int mouseX, int mouseY, int state) {
        int size = 11;
        int y = (int) (this.y + 4 + Main.fontManager.getFontHeight() / 2 - size / 2);
        int x = (int) (this.x + width - size);

        if (mouseOver(x, y, (int) (x + width), y + size, mouseX, mouseY))
            isExtended = !isExtended;
        if (state == 0 && isExtended) {
            if (!alpha.picking && !hue.picking && !picker.picking) {
                copy.click(mouseX, mouseY, state);
                paste.click(mouseX, mouseY, state);
                alpha.mouseClicked(mouseX, mouseY);
                hue.mouseClicked(mouseX, mouseY);
                picker.mouseClicked(mouseX, mouseY);
            }
        }
    }

    public static boolean mouseOver(int minX, int minY, int maxX, int maxY, int mX, int mY) {
        return mX > minX && mX < maxX && mY > minY && mY < maxY;
    }

    public class Clickable {
        protected String displayString;
        protected float x, y, width, height;
        protected float[] col = new float[]{1, 1, 1};

        public Clickable(final String displayString, final float x, final float y, final float width, final float height) {
            this.displayString = displayString;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void action() {
        }

        public void click(final int mouseX, final int mouseY, final int state) {
            if (inside(mouseX, mouseY) && state == 0) {
                action();
            }
        }

        public void render(final int mouseX, final int mouseY) {
            setupCol(mouseX, mouseY);
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(x, y, x + width + val, y + height + val, 7, new Color(0, 0, 0, i));
            }
            RenderUtil.drawRoundedRect(x, y, x + width, y + height, 8, PanelScreen.thirdBackgroundColor);
            RenderUtil.drawRoundedRect(x + 1, y + 1, x + width - 1, y + height - 1, 8, PanelScreen.secondBackgroundColor);
            RenderUtil.prepareScale(0.65f);
            Main.fontManager.drawStringWithShadow(displayString, new Vector2D((x + (width / 2f) - (Main.fontManager.getStringWidth(displayString) * 0.65f) / 2.0f) / 0.65f, (y + 7.5f - Main.fontManager.getFontHeight() / 2.0f) / 0.65f), new Color(col[0], col[1], col[2], 1.0f).getRGB());
            RenderUtil.releaseScale();
        }

        protected boolean inside(final int mouseX, final int mouseY) {
            return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
        }

        protected void setupCol(final int mouseX, final int mouseY) {
            if (inside(mouseX, mouseY)) {
                IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], 0.6f, PanelScreen.animationFactor() * 10));
            } else {
                IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], 1.0f, PanelScreen.animationFactor() * 10));
            }
        }

        public void setY(float y) {
            this.y = y;
        }

        public void setX(float x) {
            this.x = x;
        }

        public void setDisplayString(String displayString) {
            this.displayString = displayString;
        }
    }

    protected static class Slider {
        String displayString;
        public boolean picking;
        int x;
        int y;
        int width;
        int height;
        int sliderOffset;

        public Slider(String diplayString, int x, int y, int width, int height) {
            this.displayString = diplayString;
            this.picking = false;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.sliderOffset = 0;
        }

        public void mouseReleased() {
            picking = false;
        }

        public void mouseClicked(int mouseX, int mouseY) {
            picking = mouseOver(mouseX, mouseY);
        }

        public void updatePositionAndSize(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean mouseOver(int mouseX, int mouseY) {
            return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
        }

    }

    protected static class RGBASlider extends Slider {

        public RGBASlider(String diplayString, int x, int y, int width, int height) {
            super(diplayString, x, y, width, height);
        }

        protected void updateSliderLocation(Integer color) {
            sliderOffset = (int) (width * color / 255f);
        }

        protected Integer pick(int mouseY, Integer original) {
            if (picking) {
                int mX = mouseY - y;
                float percent = mX / (float) height;
                percent = MathHelper.clamp(percent, 0, 1);
                return (int) (255 * percent);
            }
            return original;
        }
    }

    protected class AlphaSlider extends RGBASlider {
        protected float currentSlider = 0.0f;

        public AlphaSlider(String diplayString, int x, int y, int width, int height) {
            super(diplayString, x, y, width, height);
        }

        public void drawSlider(float r, float g, float b, float alpha) {
            boolean left = false;
            boolean col = false;
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(x, y, x + width + val, y + height + val, 7, new Color(0, 0, 0, i));
            }
            int checkerBoardSquareSize = width / 2;
            for (int i = 0; i < 12; ) {
                boolean scissored = false;
                if (y + height < PanelScreen.y + PanelScreen.secondStartY + PanelScreen.height - PanelScreen.secondStartY - PanelScreen.secondEndY - 2.0f) {
                    RenderUtil.prepareScissor(x - 2, y, checkerBoardSquareSize * 4, height);
                    scissored = true;
                }
                if (left) {
                    RenderUtil.drawRect(new Vector2D(x, y + i * 4.6f), new Vector2D(x + checkerBoardSquareSize, y + (i * 4.6f) * 2), col ? new Color(0x2A2A2A).getRGB() : new Color(0x595959).getRGB());
                    col = !col;
                } else {
                    RenderUtil.drawRect(new Vector2D(x + checkerBoardSquareSize, y + i * 4.6f), new Vector2D(x + checkerBoardSquareSize * 2, y + (i * 4.6f) * 2), col ? new Color(0x2A2A2A).getRGB() : new Color(0x595959).getRGB());
                    i++;
                }
                if (scissored) {
                    RenderUtil.releaseScissor();
                }
                left = !left;
            }
            RenderUtil.drawGradientRect(x, y, x + width, y + height, new Color(r, g, b, 1).getRGB(), 0);
            int sliderMinY = (int) (y + height - (height * alpha));
            if (currentSlider == 0.0f || currentSlider < y || currentSlider > y + height) {
                currentSlider = sliderMinY;
            } else {
                currentSlider = normalizeNumber(currentSlider, sliderMinY, PanelScreen.animationFactor() * 10);
            }
            RenderUtil.drawRect(new Vector2D(x, currentSlider), new Vector2D(x + width, currentSlider + 1), -1);
        }
    }

    protected class HueSlider extends Slider {
        protected float currentSlider = sliderOffset;

        public HueSlider(int x, int y, int width, int height) {
            super("", x, y, width, height);
        }

        private void updateSliderLocation(Float hue) {
            sliderOffset = (int) (height * hue);
        }

        private Float pick(int mouseY, Float original) {
            if (picking) {
                int mY = mouseY - y;
                float hue = mY / (float) height;
                hue = MathHelper.clamp(hue, 0f, 0.99722222221f);
                return hue;
            }
            return original;
        }


        public void drawSlider() {
            int step = 0;
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(x, y, x + width + val, y + height + val, 7, new Color(0, 0, 0, i));
            }
            RenderUtil.drawRect(new Vector2D(x, y), new Vector2D(x + width, y + 4), new Color(0xFFFF0000).getRGB());
            y += 4;
            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                RenderUtil.drawGradientRect(x, y + step * (height / 6), x + width, y + (step + 1) * (height / 6), previousStep, nextStep);
                step++;
            }
            y -= 4;
            currentSlider = normalizeNumber(currentSlider, sliderOffset, PanelScreen.animationFactor() * 10);
            RenderUtil.drawRect(new Vector2D(x, y + currentSlider), new Vector2D(x + width, y + currentSlider + 1), -1);
        }
    }

    protected class ColorPicker {
        int x;
        int y;
        int pickerX;
        int pickerY;
        int width;
        int height;
        boolean picking;
        float currentPickerX, currentPickerY;

        public ColorPicker(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.pickerX = 0;
            this.pickerY = 0;
            picking = false;
        }

        public void drawColorPicker(int r, int b, int g) {
            float selectedRed = r / 255.0f;
            float selectedGreen = g / 255.0f;
            float selectedBlue = b / 255.0f;
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(x, y, x + width + val, y + height + val, 7, new Color(0, 0, 0, i));
            }
            RenderUtil.drawPickerBase(x, y, width, height, selectedRed, selectedGreen, selectedBlue, 255);
            currentPickerX = normalizeNumber(currentPickerX, pickerX, PanelScreen.animationFactor() * 10.0f);
            currentPickerY = normalizeNumber(currentPickerY, pickerY, PanelScreen.animationFactor() * 10.0f);
            RenderUtil.drawRoundedRect(x + currentPickerX - 2, y + currentPickerY - 2, x + currentPickerX + 2, y + currentPickerY + 2, 5, Color.WHITE);
        }

        public float pickSaturation(int mouseX, float original) {
            if (picking) {
                int mX = mouseX - x;
                float saturation = mX / (float) width;
                saturation = MathHelper.clamp(saturation, 0f, 0.99f);
                return saturation;
            }
            return original;
        }

        public float pickBrigthness(int mouseY, float original) {
            if (picking) {
                int mY = mouseY - y;
                float brightness = mY / (float) height;
                brightness = MathHelper.clamp(brightness, 0f, 0.99f);
                return 1 - brightness;
            }
            return original;
        }

        public void updatePickerLocation(float saturation, float brightness) {
            pickerX = (int) (width * saturation);
            pickerY = (int) (height * (1 - brightness));
        }

        public void mouseReleased() {
            picking = false;
        }

        public void mouseClicked(int mouseX, int mouseY) {
            picking = mouseOver(mouseX, mouseY);
        }

        public void updatePositionAndSize(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean mouseOver(int mouseX, int mouseY) {
            return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
        }
    }
}