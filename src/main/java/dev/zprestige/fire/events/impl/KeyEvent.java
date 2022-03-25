package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;

public class KeyEvent extends Event {
    final int key;

    public KeyEvent(final int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
