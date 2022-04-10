package dev.zprestige.fire.ui.menu.dropdown.category.modules;

import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.ui.menu.dropdown.category.modules.settings.AbstractSetting;
import dev.zprestige.fire.util.impl.Vector2D;

import java.util.ArrayList;

public abstract class AbstractModule {
    protected ArrayList<AbstractSetting> abstractSettings = new ArrayList<>();
    protected final Module module;
    protected Vector2D position, size;
    protected float scroll;
    protected boolean open;

    public AbstractModule(Module module, Vector2D position, Vector2D size) {
        this.module = module;
        this.position = position;
        this.size = size;
        this.scroll = 0.0f;
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

    public void setOpen(boolean open) {
        this.open = open;
    }

    public float getScroll() {
        return scroll;
    }

    public void setScroll(float scroll) {
        this.scroll = scroll;
    }

    public abstract float getHeight();

    public ArrayList<AbstractSetting> getAbstractSettings() {
        return abstractSettings;
    }
}