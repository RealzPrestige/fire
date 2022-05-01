package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BlockInteractEvent extends Event {
    protected final BlockPos pos;
    protected final EnumFacing facing;

    public BlockInteractEvent(final BlockPos pos, final EnumFacing facing, final boolean cancellable) {
        super(Stage.None, cancellable);
        this.pos = pos;
        this.facing = facing;
    }

    public static class ClickBlock extends BlockInteractEvent {
        public ClickBlock(final BlockPos pos, final EnumFacing facing) {
            super(pos, facing, true);
        }
    }

    public static class DamageBlock extends BlockInteractEvent {
        public DamageBlock(final BlockPos pos, final EnumFacing facing) {
            super(pos, facing, true);
        }
    }

    public BlockPos getPos() {
        return pos;
    }

    public EnumFacing getFacing() {
        return facing;
    }
}