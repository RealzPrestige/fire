package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import dev.zprestige.fire.events.eventbus.event.IsCancellable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

@IsCancellable
public class BlockInteractEvent extends Event {
    protected final BlockPos pos;
    protected final EnumFacing facing;

    public BlockInteractEvent(final BlockPos pos, final EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
    }

    @IsCancellable
    public static class ClickBlock extends BlockInteractEvent {
        public ClickBlock(final BlockPos pos, final EnumFacing facing) {
            super(pos, facing);
        }
    }

    @IsCancellable
    public static class DamageBlock extends BlockInteractEvent {
        public DamageBlock(final BlockPos pos, final EnumFacing facing) {
            super(pos, facing);
        }
    }

    public BlockPos getPos() {
        return pos;
    }

    public EnumFacing getFacing() {
        return facing;
    }
}