package dev.zprestige.fire.module.misc.rundetect;

import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.settings.impl.Slider;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

@Descriptor(description = "Predicts when enemies will run out of holes")
public class RunDetect extends Module {
    public final Slider radius = Menu.Slider("Radius", 3.0f, 0.1f, 15.0f);
    protected final ArrayList<EntityPlayer> potentialRunnersList = new ArrayList<>(), swordedPotentialRunnersList = new ArrayList<>(), finalRunningPlayers = new ArrayList<>();

    public RunDetect(){
        eventListeners = new EventListener[]{
                new Frame3DListener(this),
                new TickListener(this)
        };
    }

    protected void remove(final EntityPlayer entityPlayer) {
        potentialRunnersList.remove(entityPlayer);
        swordedPotentialRunnersList.remove(entityPlayer);
        finalRunningPlayers.remove(entityPlayer);
    }

}