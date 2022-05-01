package dev.zprestige.fire.module.misc.freelook;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TurnEvent;
import net.minecraft.util.math.MathHelper;

public class TurnListener extends EventListener<TurnEvent, Freelook> {

    public TurnListener(final Freelook freelook){
        super(TurnEvent.class, freelook);
    }

    @Override
    public void invoke(final Object object){
        final TurnEvent event = (TurnEvent) object;
        module.yaw = module.yaw + event.getYaw() * 0.15f;
        module.pitch = module.pitch - event.getPitch() * 0.15f;
        module.pitch = MathHelper.clamp(module.pitch, -90.0f, 90.0f);
        event.setCancelled();
    }
}
