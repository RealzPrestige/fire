package dev.zprestige.fire.ui.menu.dropdown.category.modules.settings.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.client.clickgui.ClickGui;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.ui.menu.dropdown.category.modules.settings.AbstractSetting;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderButton extends AbstractSetting {
    protected final Slider setting;
    protected int extension;
    protected boolean integer;

    public SliderButton(Slider setting, Vector2D size) {
        super(setting, size);
        this.setting = setting;
    }


    @Override
    public void render(int mouseX, int mouseY) {
        dragSlider(mouseX, mouseY);
        RenderUtil.drawRect(new Vector2D(position.getX(), position.getY()), new Vector2D(position.getX() + size.getX() * sliderWidthValue(), position.getY() + size.getY()), ClickGui.Instance.color.GetColor().getRGB());
        RenderUtil.drawOutline(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY(), new Color(0, 0, 0, 30), 1.0f);
        final float y = position.getY() + size.getY() / 2 - Main.fontManager.getFontHeight() / 2;
        Main.fontManager.drawStringWithShadow(setting.getName(), new Vector2D(position.getX() + 2, y), -1);
        final String val = setting.GetSlider() + "";
        Main.fontManager.drawStringWithShadow(val, new Vector2D(position.getX() + size.getX() - Main.fontManager.getStringWidth(val) - 2, y), Color.GRAY.getRGB());
        if (inside(mouseX, mouseY)){
            RenderUtil.drawRect(position, new Vector2D(position.getX() + size.getX(), position.getY() + size.getY()), new Color(0, 0, 0, 50).getRGB());
        }
    }

    @Override
    public void click(int mouseX, int mouseY, int state) {

    }

    @Override
    public void release(int mouseX, int mouseY, int state) {

    }

    @Override
    public void type(char typedChar, int keyCode) {

    }

    @Override
    public float getHeight() {
        return size.getY();
    }

    public float sliderWidthValue() {
        return (setting.GetSlider() - setting.getMin()) / (setting.getMax() - setting.getMin());
    }

    public void dragSlider(int mouseX, int mouseY) {
        if (isInsideExtended(mouseX, mouseY) && Mouse.isButtonDown(0)) {
            setSliderValue(mouseX);
            extension = 400;
        } else {
            extension = 0;
        }
    }

    protected void setSliderValue(int mouseX) {
        setting.setValue(setting.getMin());
        final float diff = Math.min(size.getX(), Math.max(0, mouseX - position.getX()));
        final float min = setting.getMin();
        final float max = setting.getMax();
        setting.setValue(diff == 0 ? setting.getMin() : roundNumber(diff / size.getX() * (max - min) + min));
    }

    protected float roundNumber(double value) {
        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(1, RoundingMode.FLOOR);
        return decimal.floatValue();
    }


    protected boolean isInsideExtended(int mouseX, int mouseY) {
        return mouseX > position.getX() - extension && mouseX < position.getX() + size.getX() + extension && mouseY > position.getY() - extension && mouseY < position.getY() + size.getY() + extension;
    }
}
