package dev.zprestige.fire.util.impl;

import dev.zprestige.fire.util.Utils;

public class AnimationUtil implements Utils {

    public static Integer increaseNumber(int input, int target, int delta) {
        if (input < target)
            return input + delta;
        return target;
    }

    public static Integer decreaseNumber(int input, int target, int delta) {
        if (input > target)
            return input - delta;
        return target;
    }

    public static Float increaseNumber(float input, float target, float delta) {
        if (input < target)
            return input + delta;
        return target;
    }
    public static Float decreaseNumber(float input, float target, float delta) {
        if (input > target)
            return input - delta;
        return target;
    }
}
