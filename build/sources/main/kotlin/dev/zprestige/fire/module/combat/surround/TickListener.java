package dev.zprestige.fire.module.combat.surround;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TickEvent;
import dev.zprestige.fire.util.impl.BlockUtil;
import net.minecraft.init.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class TickListener extends EventListener<TickEvent, Surround> {

    public TickListener(final Surround surround) {
        super(TickEvent.class, surround);
    }

    @Override
    public void invoke(final Object object) {
        final BlockPos pos = BlockUtil.getPosition();
        if (!mc.player.onGround || mc.player.stepHeight > 0.6f || (module.lastPos != null && pos.getY() > module.lastPos.getY())) {
            module.disableModule();
            return;
        }
        if (!module.multiTask.GetSwitch() && mc.player.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            return;
        }
        module.lastPos = pos;
        int blocks = 0;
        switch (module.mode.GetCombo()) {
            case "Instant":
                ArrayList<Surround.Position> offs = module.getOffsets(pos);
                if (module.smartPriority.GetSwitch()) {
                    offs = offs.stream().sorted(Comparator.comparing(Surround.Position::getPriority).reversed()).collect(Collectors.toCollection(ArrayList::new));
                }
                for (final Surround.Position position : offs) {
                    if (blocks > module.blocksPerTick.GetSlider() || (module.preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations())) {
                        return;
                    }
                    final BlockPos pos1 = position.getPos();
                    if (mc.world.getBlockState(pos1).getMaterial().isReplaceable() && (!module.extend.GetSwitch() || !mc.player.getEntityBoundingBox().intersects(new AxisAlignedBB(pos1))) && module.isntIntersectingWithPlayer(pos1)) {
                        final int slot = module.getSlotByItem();
                        if (slot != -1) {
                            Main.interactionManager.placeBlockWithSwitch(pos1, module.rotate.GetSwitch(), module.packet.GetSwitch(), module.strict.GetSwitch() , slot);
                            module.addFade(pos1);
                            blocks++;
                        } else {
                            module.disableModule();
                        }
                    }
                }
                if (module.extend.GetSwitch()) {
                    ArrayList<Surround.Position> positions = module.extendedPosses();
                    if (module.smartPriority.GetSwitch()) {
                        positions = positions.stream().sorted(Comparator.comparing(Surround.Position::getPriority).reversed()).collect(Collectors.toCollection(ArrayList::new));
                    }
                    for (final Surround.Position position : positions) {
                        if (blocks > module.blocksPerTick.GetSlider() || (module.preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations())) {
                            return;
                        }
                        final BlockPos pos1 = position.getPos();
                        if (mc.world.getBlockState(pos1).getMaterial().isReplaceable() && !mc.player.getEntityBoundingBox().intersects(new AxisAlignedBB(pos1)) && module.isntIntersectingWithPlayer(pos1)) {
                            final int slot = module.getSlotByItem();
                            if (slot != -1) {
                                if ((module.preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations())) {
                                    return;
                                }
                                Main.interactionManager.placeBlockWithSwitch(pos1, module.rotate.GetSwitch(), module.packet.GetSwitch(), module.strict.GetSwitch(), slot);
                                module.addFade(pos1);
                                blocks++;
                            } else {
                                module.disableModule();
                            }

                        }
                    }
                }
                break;
            case "Tick":
                ArrayList<Surround.Position> offs1 = module.getOffsets(pos);
                if (module.smartPriority.GetSwitch()) {
                    offs1 = offs1.stream().sorted(Comparator.comparing(Surround.Position::getPriority).reversed()).collect(Collectors.toCollection(ArrayList::new));
                }
                for (final Surround.Position position : offs1) {
                    if (module.preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations()) {
                        return;
                    }
                    final BlockPos pos1 = position.getPos();
                    if (mc.world.getBlockState(pos1).getMaterial().isReplaceable() && (!module.extend.GetSwitch() || !mc.player.getEntityBoundingBox().intersects(new AxisAlignedBB(pos1))) && module.isntIntersectingWithPlayer(pos1)) {
                        final int slot = module.getSlotByItem();
                        if (slot != -1) {
                            Main.interactionManager.placeBlockWithSwitch(pos1, module.rotate.GetSwitch(), module.packet.GetSwitch(), module.strict.GetSwitch(), slot);
                            module.addFade(pos1);
                            return;
                        } else {
                            module.disableModule();
                        }
                    }
                }
                if (module.extend.GetSwitch()) {
                    ArrayList<Surround.Position> positions = module.extendedPosses();
                    if (module.smartPriority.GetSwitch()) {
                        positions = positions.stream().sorted(Comparator.comparing(Surround.Position::getPriority).reversed()).collect(Collectors.toCollection(ArrayList::new));
                    }
                    for (final Surround.Position position : positions) {
                        if (module.preventRotationRubberband.GetSwitch() && Main.rotationManager.maxRotations()) {
                            return;
                        }
                        final BlockPos pos1 = position.getPos();
                        if (mc.world.getBlockState(pos1).getMaterial().isReplaceable() && !mc.player.getEntityBoundingBox().intersects(new AxisAlignedBB(pos1)) && module.isntIntersectingWithPlayer(pos1)) {
                            final int slot = module.getSlotByItem();
                            if (slot != -1) {
                                Main.interactionManager.placeBlockWithSwitch(pos1, module.rotate.GetSwitch(), module.packet.GetSwitch(), module.strict.GetSwitch(), slot);
                                module.addFade(pos1);
                                return;
                            } else {
                                module.disableModule();
                            }
                        }
                    }
                }
                break;
        }
    }
}
