package dev.zprestige.fire.module.movement.antivoid;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Switch;
import net.minecraft.util.math.BlockPos;

@Descriptor(description = "Prevents you from falling into the void")
public class AntiVoid extends Module {
    public final Switch placeBlock = Menu.Switch("Place Block", false);
    public final Switch packet = Menu.Switch("Packet", true).visibility(z -> placeBlock.GetSwitch());
    public final Switch rotate = Menu.Switch("Rotate", true).visibility(z -> placeBlock.GetSwitch());
    public final Switch strict = Menu.Switch("Strict", true).visibility(z -> placeBlock.GetSwitch());
    protected BlockPos pos;
    protected boolean alreadyInVoid;

    public AntiVoid() {
        eventListeners = new EventListener[]{
                new MoveListener(this),
                new TickListener(this)
        };
    }
}
