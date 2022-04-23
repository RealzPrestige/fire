package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;

public class KeyEvent extends Event {
    protected final int key;

    public KeyEvent(final int key) {
        super(Stage.None, false);
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}