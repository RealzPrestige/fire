package dev.zprestige.fire.ui.menu.category.modules.settings;

import dev.zprestige.fire.settings.Setting;
import dev.zprestige.fire.util.impl.Vector2D;

public abstract class AbstractSetting {
    protected Setting setting;
    protected Vector2D position, size;

    public AbstractSetting(Setting setting, Vector2D size){
        this.setting = setting;
        this.size = size;
    }

    public abstract void render(int mouseX, int mouseY);

    public abstract void click(int mouseX, int mouseY, int state);

    public abstract void release(int mouseX, int mouseY, int state);

    public abstract void type(char typedChar, int keyCode);

    protected boolean inside(int mouseX, int mouseY){
        return mouseX > position.getX() && mouseX < position.getX() + size.getX() && mouseY > position.getY() && mouseY < position.getY() + size.getY();
    }

    public void setPosition(Vector2D position){
        this.position = position;
    }

    public Vector2D getPosition() {
        return position;
    }

    public abstract float getHeight();
}
