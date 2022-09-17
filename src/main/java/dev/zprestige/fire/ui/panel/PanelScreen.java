package dev.zprestige.fire.ui.panel;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.Category;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.ui.panel.panels.PanelDrawable;
import dev.zprestige.fire.ui.panel.panels.impl.PanelConfigs;
import dev.zprestige.fire.ui.panel.panels.impl.PanelHudEditor;
import dev.zprestige.fire.ui.panel.panels.impl.PanelSocials;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class PanelScreen extends GuiScreen {
    public static final ClickGui PANEL = (ClickGui) Main.moduleManager.getModuleByClass(ClickGui.class);
    protected final ArrayList<PanelDrawable> panelDrawables = new ArrayList<>();
    public static PanelDrawable panelDrawable;
    public static PanelCategory activeCategory;
    public static PanelModule activeModule;
    protected static final Timer timer = new Timer(), deletingTimer = new Timer(), backspaceTimer = new Timer();
    protected static final ArrayList<PanelCategory> panelCategories = new ArrayList<>();
    public static final Color backgroundColor = new Color(0x1E1E1E), secondBackgroundColor = new Color(0x212121), thirdBackgroundColor = new Color(0x2D2D2D);
    protected static boolean setup, searching;
    public static float x = 0, y = 0, width = 500, height = 300, secondStart = 75, secondWidth = 425, secondStartY = 20, secondEndY = 10, categoryHeight = 15, lineY = 0.0f, j = 0.0f;
    public static String searchingString = "";
    protected static float[] insideSearch = new float[]{backgroundColor.getRed() / 255.0f, backgroundColor.getGreen() / 255.0f, backgroundColor.getBlue() / 255.0f};

    public PanelScreen() {
        setup = false;
        panelCategories.clear();
        Main.moduleManager.getCategories().forEach(category -> panelCategories.add(new PanelCategory(category, x, y, secondStart, categoryHeight)));
        panelCategories.stream().filter(panelCategory -> panelCategory.category.equals(Category.Combat)).forEach(panelCategory -> activeCategory = panelCategory);
        panelDrawable = null;
        activeModule = null;
        panelDrawables.add(new PanelConfigs(x, y, secondStart, categoryHeight));
        panelDrawables.add(new PanelSocials(x, y, secondStart, categoryHeight));
        panelDrawables.add(new PanelHudEditor(x, y, secondStart, categoryHeight));
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!setup) {
            setup();
        }
        for (int i = 20; i <= 30; i++) {
            final float val = ((30 - i) / 2f);
            RenderUtil.drawRoundedRect(x - val, y - val, x + width + val, y + height + val, 10, new Color(0, 0, 0, i));
        }
        RenderUtil.drawRoundedRect(x, y, x + width, y + height, 10, backgroundColor);
        for (int i = 20; i <= 28; i = i + 2) {
            final float val = ((28 - i) / 2f);
            RenderUtil.drawRoundedRect(x + secondStart, y + secondStartY, x + secondStart + secondWidth - secondEndY + val, y + height - secondEndY + val, 10, new Color(0, 0, 0, i));
        }
        RenderUtil.drawRoundedRect(x + secondStart, y + secondStartY, x + secondStart + secondWidth - secondEndY, y + height - secondEndY, 10, secondBackgroundColor);
        final String watermark = Main.name;
        final String version = Main.version;
        final float watermarkWidth = Main.fontManager.getStringWidth(watermark + version);
        final float watermarkWidth1 = Main.fontManager.getStringWidth(watermark + " ");
        final float scaleFactor = 1.2f;
        RenderUtil.prepareScale(scaleFactor);
        final float x1 = (x + 37.5f - (watermarkWidth / 2.0f)) / scaleFactor;
        final float y1 = (y + 10 - (Main.fontManager.getFontHeight() / 2.0f)) / scaleFactor;
        Main.fontManager.drawStringWithShadow(watermark, x1, y1, PANEL.color.GetColor().getRGB());
        Main.fontManager.drawStringWithShadow(version, x1 + watermarkWidth1, y1, -1);
        RenderUtil.releaseScale();
        RenderUtil.drawRoundedRect(x + 5, y + 20, x + secondStart - 5, y + 21, 1, secondBackgroundColor);
        float target = 0;
        boolean renderLine = false;
        if (PanelScreen.activeCategory != null) {
            target = activeCategory.y;
            renderLine = true;
        }
        if (PanelScreen.panelDrawable != null) {
            target = panelDrawable.getY();
            renderLine = true;
        }
        if (renderLine) {
            lineY = normalizeNumber(lineY, target, animationFactor() * 10);
        }
        RenderUtil.drawRoundedRect(x + 2, lineY, x + 3, lineY + categoryHeight, 1, PANEL.color.GetColor());
        drawAvatar();
        updateSearchColor(insideSearching(mouseX, mouseY), animationFactor());
        for (int i = 20; i <= 28; i = i + 2) {
            final float val = ((28 - i) / 2f);
            RenderUtil.drawRoundedRect(x + secondStart + (secondWidth - secondEndY) / 2.0f - 50.0f, y + 2, x + secondStart + (secondWidth - secondEndY) / 2.0f + 50.0f + val, y + secondStartY - 2 + val, 10, new Color(0, 0, 0, i));
        }
        final Color searchingColor = new Color(insideSearch[0], insideSearch[1], insideSearch[2], 1.0f);
        RenderUtil.drawRoundedRect(x + secondStart + (secondWidth - secondEndY) / 2.0f - 50.0f, y + 2, x + secondStart + (secondWidth - secondEndY) / 2.0f + 50.0f, y + secondStartY - 2, 10, secondBackgroundColor);
        RenderUtil.drawRoundedRect(x + secondStart + (secondWidth - secondEndY) / 2.0f - 49.0f, y + 3, x + secondStart + (secondWidth - secondEndY) / 2.0f + 49.0f, y + secondStartY - 3, 10, searchingColor);
        RenderUtil.prepareScale(0.91f);
        RenderUtil.prepareScissor((int) x, (int) y, (int) (secondStart + (secondWidth - secondEndY) / 2.0f + 49.0f), (int) (y + height));
        if (searchingString.equals("")) {
            Main.fontManager.drawStringWithShadow("Search" + getDots() + (searching ? getTypingIcon() : ""), (x + secondStart + (secondWidth - secondEndY) / 2.0f - 47.0f) / 0.91f, ((y + 3) + (secondStartY - 3) / 2.0f - Main.fontManager.getFontHeight() / 2.0f) / 0.91f, new Color(1.0f, 1.0f, 1.0f, 0.50f).getRGB());
        } else {
            Main.fontManager.drawStringWithShadow(searchingString + (searching ? getTypingIcon() : ""), (x + secondStart + (secondWidth - secondEndY) / 2.0f - 47.0f) / 0.91f, ((y + 3) + (secondStartY - 3) / 2.0f - Main.fontManager.getFontHeight() / 2.0f) / 0.91f, -1);
        }
        RenderUtil.releaseScissor();
        RenderUtil.releaseScale();
        if (!Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
            backspaceTimer.syncTime();
        }
        if (searching && Keyboard.isKeyDown(Keyboard.KEY_BACK) && deletingTimer.getTime(100000 / backspaceTimer.nanoTime())) {
            if (searchingString.length() > 0) {
                searchingString = searchingString.substring(0, searchingString.length() - 1);
            }
            deletingTimer.syncTime();
        }
        RenderUtil.drawRoundedRect(x + 2, j, x + secondStart - 2, j + 1, 1, secondBackgroundColor);
        panelCategories.forEach(panelCategory -> panelCategory.render(mouseX, mouseY));
        panelDrawables.forEach(panelDrawable -> panelDrawable.render(mouseX, mouseY));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (insideSearching(mouseX, mouseY)) {
            searching = !searching;
        } else if (searching) {
            searching = false;
        }
        panelCategories.forEach(panelCategory -> panelCategory.click(mouseX, mouseY, mouseButton));
        panelDrawables.forEach(panelDrawable -> panelDrawable.click(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (searching) {
            searchingString = getTypingText(searchingString, typedChar, keyCode);
        }
        panelCategories.forEach(panelCategory -> panelCategory.type(typedChar, keyCode));
        panelDrawables.forEach(panelDrawable -> panelDrawable.type(typedChar, keyCode));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        panelCategories.forEach(panelCategory -> panelCategory.release(mouseX, mouseY, state));
        panelDrawables.forEach(panelDrawable -> panelDrawable.release(state));
    }

    protected final Timer timer1 = new Timer();

    protected String getDots() {
        if (timer1.getTime(1500)) {
            timer1.syncTime();
        }
        if (timer1.getTime(1000)) {
            return "...";
        }
        if (timer1.getTime(500)) {
            return "..";
        }
        return ".";
    }

    protected boolean insideSearching(final int mouseX, final int mouseY) {
        return mouseX > x + secondStart + (secondWidth - secondEndY) / 2.0f - 50.0f && mouseX < x + secondStart + (secondWidth - secondEndY) / 2.0f + 50.0f && mouseY > y + 2 && mouseY < y + secondStartY - 2;
    }

    protected void drawAvatar() {
        final ResourceLocation resourceLocation = new ResourceLocation("textures/images/avatar.png");
        for (int i = 20; i <= 30; i = i + 2) {
            final float val = ((30 - i) / 2f);
            RenderUtil.drawRoundedRect(x + 2, y + height - secondStartY - secondEndY, x + secondStart - 2 + val, y + height - secondEndY + val, 10, new Color(0, 0, 0, i));
        }
        RenderUtil.drawRoundedRect(x + 2, y + height - secondStartY - secondEndY, x + secondStart - 2, y + height - secondEndY, 10, secondBackgroundColor);
        RenderUtil.drawRoundedRect(x + 3, y + height - secondStartY - secondEndY + 1, x + secondStart - 3, y + height - secondEndY - 1, 10, backgroundColor);
        final String name = mc.player.getName();
        RenderUtil.prepareScale(0.8);
        Main.fontManager.drawStringWithShadow(name, (x + 16) / 0.8f, (y + height - secondStartY - secondEndY + 5.5f) / 0.8f, -1);
        Main.fontManager.drawStringWithShadow(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime() + "ms",(x + 16) / 0.8f, (y + height - secondStartY - secondEndY + 7.5f + Main.fontManager.getFontHeight()) / 0.8f, Color.GRAY.getRGB());
        RenderUtil.releaseScale();
        RenderUtil.image(resourceLocation, (int) (x + 5), (int) (y + height - secondStartY - secondEndY + 7.5), 9, 9);
    }

    protected void updateSearchColor(final boolean inside, final float animationFactor) {
        if (inside) {
            insideSearch[0] = normalizeNumber(insideSearch[0], backgroundColor.getRed() / 255.0f - 0.005f, animationFactor);
            insideSearch[1] = normalizeNumber(insideSearch[1], backgroundColor.getGreen() / 255.0f - 0.005f, animationFactor);
            insideSearch[2] = normalizeNumber(insideSearch[2], backgroundColor.getBlue() / 255.0f - 0.005f, animationFactor);

        } else {
            insideSearch[0] = normalizeNumber(insideSearch[0], backgroundColor.getRed() / 255.0f, animationFactor);
            insideSearch[1] = normalizeNumber(insideSearch[1], backgroundColor.getGreen() / 255.0f, animationFactor);
            insideSearch[2] = normalizeNumber(insideSearch[2], backgroundColor.getBlue() / 255.0f, animationFactor);
        }
    }

    protected void setup() {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        x = scaledResolution.getScaledWidth() / 2.0f - width / 2.0f;
        y = scaledResolution.getScaledHeight() / 2.0f - height / 2.0f;
        float i = y + secondStartY + 3;
        for (final PanelCategory panelCategory : panelCategories) {
            panelCategory.setX(x);
            panelCategory.setY(i);
            i += 17.0f;
            panelCategory.setupPanelModules();
        }
        if (PanelScreen.activeCategory != null) {
            lineY = activeCategory.y;
        }
        if (PanelScreen.panelDrawable != null) {
            lineY = panelDrawable.getY();
        }
        float j = y + height - secondStartY - secondEndY - 17.0f;
        for (final PanelDrawable panelDrawable : panelDrawables) {
            panelDrawable.setX(x);
            panelDrawable.setY(j);
            j -= 17.0f;
        }
        PanelScreen.j = j + 13.0f;
        setup = true;
    }

    protected String getTypingText(String string, char typedChar, int keyCode) {
        String newString = string;
        switch (keyCode) {
            case 14:
                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    newString = "";
                }
                if (newString.length() > 0) {
                    newString = newString.substring(0, newString.length() - 1);
                    deletingTimer.syncTime();
                }
                break;
            case 27:
            case 28:
                searching = false;
                break;
            default:
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    newString = newString + typedChar;
                    break;
                }
        }
        return newString;
    }

    public static float animationFactor() {
        return 0.1f;
    }

    protected float normalizeNumber(final float input, final float target, final float factor) {
        if (input > target) {
            return input - ((input - target) * factor) / 10;
        }
        if (input < target) {
            return input + ((target - input) * factor) / 10;
        }
        return input;
    }

    public String getTypingIcon() {
        if (timer.getTime(1000)) {
            timer.syncTime();
            return "";
        }
        if (timer.getTime(500)) {
            return "_";
        }
        return "";
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
