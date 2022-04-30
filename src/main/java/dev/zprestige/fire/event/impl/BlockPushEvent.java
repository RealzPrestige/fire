package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class BlockPushEvent extends Event {

    public BlockPushEvent(){
        super(Stage.None, true);
    }
}
