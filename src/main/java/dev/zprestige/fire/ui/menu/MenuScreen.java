package dev.zprestige.fire.ui.menu;


import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.ClickGui;
import dev.zprestige.fire.ui.menu.category.AbstractCategory;
import dev.zprestige.fire.ui.menu.category.ConfigScreen;
import dev.zprestige.fire.ui.menu.category.MenuCategory;
import dev.zprestige.fire.ui.menu.category.SocialsScreen;
import dev.zprestige.fire.util.impl.AnimationUtil;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MenuScreen extends GuiScreen {
    protected final ArrayList<AbstractCategory> guiCategories = new ArrayList<>();
    protected AbstractCategory configScreen, socialsScreen;
    protected final Vector2D categorySize = new Vector2D(120, 15);
    protected float deltaX = -74, leftScale = 1.0f, rightScale = 1.0f, offset = 0.0f, targetOffset = 0.0f;
    protected CurrentScreen currentScreen = CurrentScreen.MENU;

    public MenuScreen() {
        Main.moduleManager.getCategories().forEach(category -> guiCategories.add(new MenuCategory(category, new Vector2D(deltaX += 124, 4), categorySize)));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        scroll();
        if (offset < targetOffset) {
            offset = AnimationUtil.increaseNumber(offset, targetOffset, 10);
        } else if (offset > targetOffset) {
            offset = AnimationUtil.decreaseNumber(offset, targetOffset, 10);
        }
        if (configScreen == null) {
            configScreen = new ConfigScreen(new Vector2D(-scaledResolution.getScaledWidth() + scaledResolution.getScaledWidth() / 2f - 200, scaledResolution.getScaledHeight() / 2f - 150), new Vector2D(400, 300));
        }
        if (shouldConfigBeRendered()) {
            configScreen.render(mouseX, mouseY);
            configScreen.setPosition(new Vector2D(configScreen.getOtherX() + offset, configScreen.getPosition().getY()));
        }

        if (socialsScreen == null) {
            socialsScreen = new SocialsScreen(new Vector2D(scaledResolution.getScaledWidth() + scaledResolution.getScaledWidth() / 2f - 200, scaledResolution.getScaledHeight() / 2f - 150), new Vector2D(400, 300));
        }

        if (shouldSocialsBeRendered()) {
            socialsScreen.render(mouseX, mouseY);
            socialsScreen.setPosition(new Vector2D(socialsScreen.getOtherX() + offset, socialsScreen.getPosition().getY()));
        }

        if (shouldMenuBeRendered(scaledResolution)) {
            if (offset != 0) {
                guiCategories.forEach(abstractCategory -> abstractCategory.setPosition(new Vector2D(abstractCategory.getOtherX() + offset, abstractCategory.getPosition().getY())));
            }
            guiCategories.forEach(guiCategory -> guiCategory.render(mouseX, mouseY));
        }

        drawArrows();
    }

    protected void scroll(){
        int wheel = Mouse.getDWheel();
        if (wheel < 0){
            for (AbstractCategory abstractCategory : guiCategories){
                abstractCategory.setPosition(new Vector2D(abstractCategory.getPosition().getX(), abstractCategory.getPosition().getY() - 16));
            }
        }
        if (wheel > 0){
            for (AbstractCategory abstractCategory : guiCategories){
                abstractCategory.setPosition(new Vector2D(abstractCategory.getPosition().getX(), abstractCategory.getPosition().getY() + 16));
            }
        }
    }

    protected boolean shouldConfigBeRendered() {
        return currentScreen.equals(CurrentScreen.CONFIG) || (currentScreen.equals(CurrentScreen.MENU) && offset > 0);
    }

    protected boolean shouldSocialsBeRendered() {
        return currentScreen.equals(CurrentScreen.SOCIALS) || (currentScreen.equals(CurrentScreen.MENU) && offset < 0);
    }

    protected boolean shouldMenuBeRendered(ScaledResolution scaledResolution) {
        return currentScreen.equals(CurrentScreen.MENU) || (currentScreen.equals(CurrentScreen.CONFIG) && offset > -scaledResolution.getScaledWidth() + 100) || (currentScreen.equals(CurrentScreen.SOCIALS) && offset < scaledResolution.getScaledWidth() - 100);
    }

    protected boolean activateMenuListeners() {
        return currentScreen.equals(CurrentScreen.MENU);
    }

    protected boolean activateConfigListeners() {
        return currentScreen.equals(CurrentScreen.CONFIG);
    }

    protected boolean activateSocialsListeners() {
        return currentScreen.equals(CurrentScreen.SOCIALS);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        playClickingSound(mouseButton);
        if (insideLeftArrow(mouseX, mouseY)) {
            clickLeftArrow();
        }
        if (insideRightArrow(mouseX, mouseY)) {
            clickRightArrow();
        }
        if (activateMenuListeners()) {
            guiCategories.forEach(guiCategory -> guiCategory.click(mouseX, mouseY, mouseButton));
        }
        if (activateConfigListeners()) {
            configScreen.click(mouseX, mouseY, mouseButton);
        }
        if (activateSocialsListeners()){
            socialsScreen.click(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (activateMenuListeners()) {
            guiCategories.forEach(guiCategory -> guiCategory.release(mouseX, mouseY, state));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (activateMenuListeners()) {
            guiCategories.forEach(guiCategory -> guiCategory.type(typedChar, keyCode));
        }
        if (activateConfigListeners()) {
            configScreen.type(typedChar, keyCode);
        }
        if (activateSocialsListeners()){
            socialsScreen.type(typedChar, keyCode);
        }
    }

    public static float getAnimationSpeedAccordingly(float current, float target) {
        if (current > target) {
            return current / (25.0f / (25.0f / (26.0f - ClickGui.Instance.animationSpeed.GetSlider())));
        }
        return ((target - current) / (25.0f / (25.0f / (26.0f - ClickGui.Instance.animationSpeed.GetSlider()))));
    }

    public static void playClickingSound(int state) {
        try {
            InputStream audioStream;
            if (state == 0) {
                audioStream = Main.mc.getResourceManager().getResource(new ResourceLocation("textures/sounds/click.wav")).getInputStream();
            } else {
                audioStream = Main.mc.getResourceManager().getResource(new ResourceLocation("textures/sounds/rclick.wav")).getInputStream();
            }
            AudioPlayer.player.start(new AudioStream(audioStream));
        } catch (IOException ignored) {
        }
    }

    public void drawArrows() {
        if (currentScreen.equals(CurrentScreen.SOCIALS)) {
            rightScale = AnimationUtil.decreaseNumber(rightScale, 0.0f, 0.01f);
        } else {
            rightScale = AnimationUtil.increaseNumber(rightScale, 1.0f, 0.01f);
        }
        GL11.glPushMatrix();
        GL11.glScaled(rightScale, rightScale, rightScale);
        RenderUtil.drawArrow((new ScaledResolution(mc).getScaledWidth() - 8) / rightScale, (new ScaledResolution(mc).getScaledHeight() / 2f) / rightScale, false);
        GL11.glPopMatrix();
        if (currentScreen.equals(CurrentScreen.CONFIG)) {
            leftScale = AnimationUtil.decreaseNumber(leftScale, 0.0f, 0.01f);
        } else {
            leftScale = AnimationUtil.increaseNumber(leftScale, 1.0f, 0.01f);
        }
        GL11.glPushMatrix();
        GL11.glScaled(leftScale, leftScale, leftScale);
        RenderUtil.drawArrow(4 / leftScale, (new ScaledResolution(mc).getScaledHeight() / 2f) / leftScale, true);
        GL11.glPopMatrix();
    }

    public boolean insideLeftArrow(int mouseX, int mouseY) {
        return mouseX > 0 && mouseX < 8 && mouseY > new ScaledResolution(mc).getScaledHeight() / 2f - 10 && mouseY < new ScaledResolution(mc).getScaledHeight() / 2f + 20;
    }

    public boolean insideRightArrow(int mouseX, int mouseY) {
        return mouseX > new ScaledResolution(mc).getScaledWidth() - 8 && mouseX < new ScaledResolution(mc).getScaledWidth() && mouseY > new ScaledResolution(mc).getScaledHeight() / 2f - 10 && mouseY < new ScaledResolution(mc).getScaledHeight() / 2f + 20;
    }

    public void clickLeftArrow() {
        if (currentScreen.equals(CurrentScreen.MENU)) {
            currentScreen = CurrentScreen.CONFIG;
            targetOffset = new ScaledResolution(mc).getScaledWidth();
        } else if (currentScreen.equals(CurrentScreen.SOCIALS)) {
            currentScreen = CurrentScreen.MENU;
            targetOffset = 0.0f;
        }
    }

    public void clickRightArrow() {
        if (currentScreen.equals(CurrentScreen.MENU)) {
            currentScreen = CurrentScreen.SOCIALS;
            targetOffset = -new ScaledResolution(mc).getScaledWidth();
        } else if (currentScreen.equals(CurrentScreen.CONFIG)) {
            currentScreen = CurrentScreen.MENU;
            targetOffset = 0.0f;
        }
    }

    @Override
    public void initGui() {
        if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer && ClickGui.Instance.blur.GetSwitch()) {
            try {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            } catch (Exception ignored) {
            }
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }

    @Override
    public void onGuiClosed() {
        try {
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    protected enum CurrentScreen {
        MENU,
        CONFIG,
        SOCIALS,
    }
}
