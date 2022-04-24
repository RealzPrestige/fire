package dev.zprestige.fire.module.misc.removeexpdesync;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;
import java.util.List;

@Descriptor(description = "Attempts to remove exp desync")
public class RemoveEXPDesync extends Module {
    public final Slider timer = Menu.Slider("Timer", 1.0f, 0.1f, 10.0f);
    public final Slider force = Menu.Slider("Force", 111.0f, 0.1f, 120.0f);
    public final Slider attempts = Menu.Slider("Attempts", 1.0f, 1.0f, 20.0f);
    protected List<Entity> activeEntities = new ArrayList<>();
    protected boolean started;
    protected int index;

    public RemoveEXPDesync(){
        eventListeners = new EventListener[]{
                new PacketSendListener(this),
                new TickListener(this)
        };
    }

    protected boolean isEXP(final EnumHand enumHand){
        return mc.player.getHeldItem(enumHand).getItem().equals(Items.EXPERIENCE_BOTTLE);
    }
}
