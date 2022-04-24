package dev.zprestige.fire.module.visual.chams;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.RenderLivingBaseEvent;
import net.minecraft.entity.player.EntityPlayer;

public class RenderLivingBaseListener extends EventListener<RenderLivingBaseEvent, Chams> {

    public RenderLivingBaseListener(final Chams chams) {
        super(RenderLivingBaseEvent.class, chams);
    }

    @Override
    public void invoke(final Object object) {
        final RenderLivingBaseEvent event = (RenderLivingBaseEvent) object;
        if (!event.getEntityLivingBase().equals(mc.player) && event.getEntityLivingBase() instanceof EntityPlayer) {
            event.setCancelled();
            if (module.fill.GetSwitch()){
                module.prepareFill();
                event.render();
                module.releaseFill();
            }
            if (module.outline.GetSwitch()) {
                module.prepareOutline();
                event.render();
                module.releaseOutline();
            }
        }
    }
}
