package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class EntityPushEvent extends Event {

    public EntityPushEvent(){
        super(Stage.None, true);
    }
}
