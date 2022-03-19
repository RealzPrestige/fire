package dev.zprestige.fire.module.client;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.ui.hudeditor.HudEditorScreen;
import dev.zprestige.fire.ui.menu.MenuScreen;

public class HudEditor extends Module {

    @Override
    public void onEnable(){
        mc.displayGuiScreen(new HudEditorScreen());
    }

    @RegisterListener
    public void onTick(TickEvent event){
        if (!(mc.currentScreen instanceof HudEditorScreen)){
            disableModule();
            ClickGui.Instance.enableModule();
        }
    }

}
