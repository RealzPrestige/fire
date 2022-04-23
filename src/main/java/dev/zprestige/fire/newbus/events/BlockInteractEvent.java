package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;
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