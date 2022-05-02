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
        if (module.players.GetSwitch()) {
            final RenderLivingBaseEvent event = (RenderLivingBaseEvent) object;
            event.getEntityLivingBase().hurtTime = 0;
            if (!event.getEntityLivingBase().equals(mc.player) && event.getEntityLivingBase().entityId != 696969696 && event.getEntityLivingBase() instanceof EntityPlayer) {
                event.setCancelled();
                if (module.fill.GetSwitch()) {
                    module.prepareFill(module.fillColor.GetColor());
                    event.render();
                    module.releaseFill();
                }
                if (module.outline.GetSwitch()) {
                    module.prepareOutline(module.outlineColor.GetColor(), module.outlineWidth.GetSlider());
                    event.render();
                    module.releaseOutline();
                }
            }
        }
    }
}
