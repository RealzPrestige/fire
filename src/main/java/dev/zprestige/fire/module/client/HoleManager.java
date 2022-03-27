package dev.zprestige.fire.module.client;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;

import java.awt.*;

public class HoleManager extends Module {
    public final Slider range = Menu.Slider("Range", 20.0f, 0.1f, 50.0f);

    public HoleManager(){
        enableModule();
    }

    @RegisterListener
    public void onFrame3D(FrameEvent.FrameEvent3D event){
        Main.holeManager.loadHoles(range.GetSlider());
    }

    @Override
    public String getData(){
        return String.valueOf(range.GetSlider());
    }
}
