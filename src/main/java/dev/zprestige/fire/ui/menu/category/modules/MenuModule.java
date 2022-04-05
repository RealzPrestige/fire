package dev.zprestige.fire.ui.menu.category.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.module.client.ClickGui;
import dev.zprestige.fire.settings.impl.*;
import dev.zprestige.fire.ui.menu.MenuScreen;
import dev.zprestige.fire.ui.menu.category.modules.settings.AbstractSetting;
import dev.zprestige.fire.ui.menu.category.modules.settings.impl.*;
import dev.zprestige.fire.util.impl.AnimationUtil;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;


import java.awt.*;

public class MenuModule extends AbstractModule {
    protected float scissorHeight = 0.0f;

    public MenuModule(Module module, Vector2D position, Vector2D size) {
        super(module, position, size);
        final Vector2D settingSize = new Vector2D(size.getX() - 2, size.getY());
        module.getSettings().stream().filter(setting -> !setting.getName().equals("Enabled") && !setting.getName().equals("Drawn")).forEach(setting -> {
            if (setting instanceof Switch) {
                 abstractSettings.add(new SwitchButton((Switch) setting, settingSize));
            }
            if (setting instanceof Slider){
                abstractSettings.add(new SliderButton((Slider) setting, settingSize));
            }
            if (setting instanceof Key){
                abstractSettings.add(new KeyButton((Key) setting, settingSize));
            }
            if (setting instanceof ComboBox){
                abstractSettings.add(new ComboBoxButton((ComboBox) setting, settingSize));
            }
            if (setting instanceof ColorBox){
                abstractSettings.add(new ColorBoxButton((ColorBox) setting, settingSize));
            }
        });
    }

    @Override
    public void render(int mouseX, int mouseY) {
        final String text = module.getName();
        if (module.isEnabled()) {
            RenderUtil.drawRect(position, new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), ClickGui.Instance.color.GetColor().getRGB());
        }
        RenderUtil.drawOutline(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY(), new Color(0, 0, 0, 30), 1.0f);
        Main.fontManager.drawStringWithShadow(text, new Vector2D(position.getX() + 2, position.getY() + size.getY() / 2 - Main.fontManager.getFontHeight() / 2), -1);
        if (inside(mouseX, mouseY)) {
            RenderUtil.drawRect(position, new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), new Color(0, 0, 0, 50).getRGB());
        }
        float totalHeight = size.getY() + 1;
        for (AbstractSetting abstractSetting : abstractSettings) {
            abstractSetting.setPosition(new Vector2D(position.getX() + 1, position.getY() + totalHeight));
            if (abstractSetting.getSetting().isVisible()) {
                totalHeight += abstractSetting.getHeight() + 1;
            }
        }
        RenderUtil.prepareScissor((int) position.getX(), (int) position.getY(), (int) size.getX(), (int) scissorHeight);
            abstractSettings.stream().filter(setting -> setting.getSetting().isVisible()).filter(abstractSetting -> abstractSetting.getPosition().getY() < position.getY() + scissorHeight).forEach(abstractSetting -> abstractSetting.render(mouseX, mouseY));
        RenderUtil.releaseScissor();
        if (open) {
            scissorHeight = AnimationUtil.decreaseNumber(scissorHeight, totalHeight, MenuScreen.getAnimationSpeedAccordingly(scissorHeight, totalHeight));
        } else {
            scissorHeight = AnimationUtil.decreaseNumber(scissorHeight, size.getY(), MenuScreen.getAnimationSpeedAccordingly(scissorHeight, size.getY()));
        }
    }

    @Override
    public void click(int mouseX, int mouseY, int state) {
        if (inside(mouseX, mouseY)) {
            switch (state) {
                case 0:
                    module.toggleModule();
                    break;
                case 1:
                    open = !open;
                    break;
                case 2:
                    module.toggleDrawn();
                    Main.chatManager.sendMessage("Updated drawn for " + Main.chatManager.prefixColor + module.getName() + ChatFormatting.RESET + " to " + module.getDrawn());
                    break;
            }
        }
        if (open){
            abstractSettings.stream().filter(setting -> setting.getSetting().isVisible()).forEach(abstractSetting -> abstractSetting.click(mouseX, mouseY, state));
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int state) {
        if (open){
            abstractSettings.stream().filter(setting -> setting.getSetting().isVisible()).forEach(abstractSetting -> abstractSetting.release(mouseX, mouseY, state));
        }
    }

    @Override
    public void type(char typedChar, int keyCode) {
        if (open){
            abstractSettings.stream().filter(setting -> setting.getSetting().isVisible()).forEach(abstractSetting -> abstractSetting.type(typedChar, keyCode));
        }
    }

    @Override
    public float getHeight() {
        return scissorHeight;
    }
}
