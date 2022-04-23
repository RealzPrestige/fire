package dev.zprestige.fire.module.visual.damagemarkers;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.stream.Collectors;

public class TickListener extends EventListener<TickEvent, DamageMarkers> {

    public TickListener(final DamageMarkers damageMarkers){
        super(TickEvent.class, damageMarkers);
    }

    @Override
    public void invoke(final Object object){
        module.playerMap.forEach((key, value) -> mc.world.playerEntities.stream().filter(entityPlayer -> !entityPlayer.equals(mc.player) && entityPlayer.getName().equals(key)).forEach(entityPlayer -> {
            final double val = Math.ceil(value - (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()));
            if (val > 0) {
                module.damageMarkers.add(new DamageMarkers.DamageMarker(val, entityPlayer, module.getRandom(),module. getRandom(), module.getRandom(), 255.0f, module.scale.GetSlider(), module.color.GetColor()));
            }
        }));
        module.playerMap = mc.world.playerEntities.stream().filter(entityPlayer -> !entityPlayer.equals(mc.player)).collect(Collectors.toMap(EntityPlayer::getName, entityPlayer -> entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount(), (a, b) -> b, HashMap::new));
    }
}
