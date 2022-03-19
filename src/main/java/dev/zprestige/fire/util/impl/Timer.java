package dev.zprestige.fire.util.impl;

import dev.zprestige.fire.util.Utils;

public class Timer implements Utils  {
    protected long time = -1L;

    public boolean getTime(long ms) {
        return nanoToMs(System.nanoTime() - time) >= ms;
    }

    public void syncTime() {
        this.time = System.nanoTime();
    }

    public long nanoToMs(long time) {
        return time / 1000000L;
    }
}