package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class MouseOverEvent extends Event {

    public MouseOverEvent(){
        super(Stage.None, true);
    }
}
