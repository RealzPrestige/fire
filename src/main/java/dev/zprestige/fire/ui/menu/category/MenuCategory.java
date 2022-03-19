package dev.zprestige.fire.ui.menu.category;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.Category;
import dev.zprestige.fire.module.client.ClickGui;
import dev.zprestige.fire.ui.menu.MenuScreen;
import dev.zprestige.fire.ui.menu.category.modules.AbstractModule;
import dev.zprestige.fire.ui.menu.category.modules.MenuModule;
import dev.zprestige.fire.ui.menu.category.modules.settings.impl.ColorBoxButton;
import dev.zprestige.fire.util.impl.AnimationUtil;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import org.lwjgl.input.Mouse;

import java.awt.*;


public class MenuCategory extends AbstractCategory {
    protected float deltaY = position.getY() + 1, scroll = 0.0f;

    public MenuCategory(Category category, Vector2D position, Vector2D size) {
        super(category, position, size);
        final Vector2D moduleSize = new Vector2D(size.getX() - 2, size.getY());
        Main.moduleManager.getModulesInCategory(category).forEach(module -> abstractModules.add(new MenuModule(module, new Vector2D(position.getX(), deltaY += (moduleSize.getY() + 1)), moduleSize)));
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (dragging) {
            setPosition(new Vector2D(drag.getX() + mouseX, drag.getY() + mouseY));
            otherX = position.getX();
        }
        float deltaY = size.getY() + 1;
        for (AbstractModule abstractModule : abstractModules) {
            abstractModule.setPosition(new Vector2D(position.getX() + 1, position.getY() + deltaY + scroll));
            deltaY += (abstractModule.getHeight() + 1);
        }
        if (open) {
            scissorHeight = AnimationUtil.increaseNumber(scissorHeight, deltaY, MenuScreen.getAnimationSpeedAccordingly(scissorHeight, deltaY));
        } else {
            final float newSize = size.getY() - 1;
            scissorHeight = AnimationUtil.decreaseNumber(scissorHeight, newSize, MenuScreen.getAnimationSpeedAccordingly(scissorHeight, newSize));
        }
        RenderUtil.prepareScissor((int) position.getX() - 2, (int) position.getY() - 2, (int) size.getX() + 2, (int) scissorHeight + 3);
        RenderUtil.drawRect(position, new Vector2D(position.getX() + size.getX(), position.getY() + scissorHeight), ClickGui.Instance.backgroundColor.GetColor().getRGB());
        RenderUtil.drawOutline(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + scissorHeight, new Color(0, 0, 0, 50), 1.0f);
        abstractModules.forEach(abstractModule -> abstractModule.render(mouseX, mouseY));
        RenderUtil.releaseScissor();
        RenderUtil.drawRect(position, new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), ClickGui.Instance.color.GetColor().getRGB());
        RenderUtil.drawOutline(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY(), new Color(0, 0, 0, 30), 1.0f);
        final String text = category.toString();
        Main.fontManager.drawStringWithShadow(text, new Vector2D(position.getX() + size.getX() / 2 - Main.fontManager.getStringWidth(text) / 2, position.getY() + size.getY() / 2 - Main.fontManager.getFontHeight() / 2), -1);

    }

    @Override
    public void click(int mouseX, int mouseY, int state) {
        if (inside(mouseX, mouseY)) {
            switch (state) {
                case 0:
                    drag = new Vector2D(position.getX() - mouseX, position.getY() - mouseY);
                    dragging = true;
                    break;
                case 1:
                    if (open) {
                        abstractModules.forEach(abstractModule -> abstractModule.setOpen(false));
                        abstractModules.forEach(abstractModule -> abstractModule.getAbstractSettings().stream().filter(abstractSetting -> abstractSetting instanceof ColorBoxButton).forEach(abstractSetting -> {
                            ((ColorBoxButton) abstractSetting).setOpened(false);
                            ((ColorBoxButton) abstractSetting).forceScissorHeight();
                        }));
                        open = false;
                    } else {
                        open = true;
                    }
            }
        }
        if (open) {
            abstractModules.forEach(abstractModule -> abstractModule.click(mouseX, mouseY, state));
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int state) {
        if (state == 0 && dragging) {
            dragging = false;
        }
        if (open) {
            abstractModules.forEach(abstractModule -> abstractModule.release(mouseX, mouseY, state));
        }
    }

    @Override
    public void type(char typedChar, int keyCode) {
        if (open) {
            abstractModules.forEach(abstractModule -> abstractModule.type(typedChar, keyCode));
        }
    }
}
