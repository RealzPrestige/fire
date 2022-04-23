package dev.zprestige.fire.module.misc.rundetect;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.TickEvent;
import dev.zprestige.fire.util.impl.EntityUtil;
import net.minecraft.init.Items;

public class TickListener extends EventListener<TickEvent, RunDetect> {

    public TickListener(final RunDetect runDetect){
        super(TickEvent.class, runDetect);
    }

    @Override
    public void invoke(final Object object){
        mc.world.playerEntities.stream().filter(player -> !player.equals(mc.player) && !module.potentialRunnersList.contains(player) && mc.player.getDistanceSq(EntityUtil.getPlayerPos(player)) < (module.radius.GetSlider() * module.radius.GetSlider())).forEach(module.potentialRunnersList::add);
        module.potentialRunnersList.stream().filter(entityPlayer -> !module.swordedPotentialRunnersList.contains(entityPlayer) && entityPlayer.getHeldItemMainhand().getItem().equals(Items.DIAMOND_SWORD)).forEach(module.swordedPotentialRunnersList::add);
        module.swordedPotentialRunnersList.stream().filter(entityPlayer -> entityPlayer.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) && !module.finalRunningPlayers.contains(entityPlayer)).forEach(module.finalRunningPlayers::add);
        module.potentialRunnersList.stream().filter(entityPlayer -> mc.player.getDistanceSq(EntityUtil.getPlayerPos(entityPlayer)) > (module.radius.GetSlider() * module.radius.GetSlider())).findFirst().ifPresent(module.potentialRunnersList::remove);
        module.swordedPotentialRunnersList.stream().filter(entityPlayer -> mc.player.getDistanceSq(EntityUtil.getPlayerPos(entityPlayer)) > (module.radius.GetSlider() * module.radius.GetSlider())).findFirst().ifPresent(module.potentialRunnersList::remove);
    }
}
