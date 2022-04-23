package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

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