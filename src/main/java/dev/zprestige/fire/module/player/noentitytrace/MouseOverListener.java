package dev.zprestige.fire.module.player.noentitytrace;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.MouseOverEvent;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class MouseOverListener extends EventListener<MouseOverEvent, NoEntityTrace> {

    public MouseOverListener(final NoEntityTrace noEntityTrace){
        super(MouseOverEvent.class, noEntityTrace);
    }

    @Override
    public void invoke(final Object object){
        final MouseOverEvent event = (MouseOverEvent) object;
        final Item item = mc.player.getHeldItemMainhand().getItem();
        if ((item.equals(Items.DIAMOND_PICKAXE) && module.pickaxe.GetSwitch()) || (item.equals(Items.GOLDEN_APPLE) && module.gapple.GetSwitch())){
            event.setCancelled();
        }
    }
}
