package dev.zprestige.fire.ui.menu.dropdown.category;

import dev.zprestige.fire.module.Category;
import dev.zprestige.fire.ui.menu.dropdown.category.modules.AbstractModule;
import dev.zprestige.fire.util.impl.Vector2D;

import java.util.ArrayList;

public abstract class AbstractCategory {
    protected ArrayList<AbstractModule> abstractModules = new ArrayList<>();
    protected final Category category;
    protected Vector2D position, size, drag;
    protected boolean dragging, open;
    protected float scissorHeight, otherX;

    public AbstractCategory(Category category, Vector2D position, Vector2D size){
        this.category = category;
        this.position = position;
        this.size = size;
        this.scissorHeight = 0;
        this.open = true;
        otherX = position.getX();
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

    public float getOtherX() {
        return otherX;
    }
}
