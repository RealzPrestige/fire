package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.util.impl.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HoleManager  {
    protected final Minecraft mc = Main.mc;
    protected List<HolePos> holes = new ArrayList<>();

    protected final Vec3i[] Hole = {
            new Vec3i(1, 0, 0),
            new Vec3i(-1, 0, 0),
            new Vec3i(0, -1, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(0, 0, -1)
    };

    protected final Vec3i[] DoubleHoleNorth = {
            new Vec3i(0, 0, -2),
            new Vec3i(-1, 0, -1),
            new Vec3i(1, 0, -1),
            new Vec3i(0, -1, -1),
            new Vec3i(0, -1, 0),
            new Vec3i(-1, 0, 0),
            new Vec3i(1, 0, 0),
            new Vec3i(0, 0, 1)
    };

    protected final Vec3i[] DoubleHoleWest = {
            new Vec3i(-2, 0, 0),
            new Vec3i(-1, 0, 1),
            new Vec3i(-1, 0, -1),
            new Vec3i(-1, -1, 0),
            new Vec3i(0, -1, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(0, 0, -1),
            new Vec3i(1, 0, 0)
    };

    public void loadHoles(final float range){
        Main.threadManager.run(() -> holes = getHoles(range));
    }

    protected ArrayList<HolePos> getHoles(final float range) {
        final ArrayList<HolePos> holes = new ArrayList<>();
        for (BlockPos pos : BlockUtil.getBlocksInRadius(range).stream().filter(blockPos -> mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)).collect(Collectors.toList())) {
            if (isEnterable(pos)) {
                boolean isSafe = true;
                for (Vec3i vec3i : Hole) {
                    if (isntSafe(pos.add(vec3i))) {
                        isSafe = false;
                    }
                }
                if (isSafe) {
                    holes.add(new HolePos(pos, Type.Bedrock));
                    continue;
                }
                boolean isUnsafe = true;
                for (Vec3i vec3i : Hole) {
                    if (isntUnsafe(pos.add(vec3i))) {
                        isUnsafe = false;
                    }
                }
                if (isUnsafe) {
                    holes.add(new HolePos(pos, Type.Obsidian));
                    continue;
                }
                boolean isSafeDoubleNorth = true;
                for (Vec3i vec3i : DoubleHoleNorth) {
                    if (isntSafe(pos.add(vec3i))) {
                        isSafeDoubleNorth = false;
                    }
                }
                if (isSafeDoubleNorth) {
                    holes.add(new HolePos(pos, Type.DoubleBedrockNorth));
                    continue;
                }
                boolean isUnSafeDoubleNorth = true;
                for (Vec3i vec3i : DoubleHoleNorth) {
                    if (isntUnsafe(pos.add(vec3i))) {
                        isUnSafeDoubleNorth = false;
                    }
                }
                if (isUnSafeDoubleNorth) {
                    holes.add(new HolePos(pos, Type.DoubleObsidianNorth));
                    continue;
                }
                boolean isSafeDoubleWest = true;
                for (Vec3i vec3i : DoubleHoleWest) {
                    if (isntUnsafe(pos.add(vec3i))) {
                        isSafeDoubleWest = false;
                    }
                }
                if (isSafeDoubleWest) {
                    holes.add(new HolePos(pos, Type.DoubleBedrockWest));
                    continue;
                }
                boolean isUnSafeDoubleWest = true;
                for (Vec3i vec3i : DoubleHoleWest) {
                    if (isntUnsafe(pos.add(vec3i))) {
                        isUnSafeDoubleWest = false;
                    }
                }
                if (isUnSafeDoubleWest) {
                    holes.add(new HolePos(pos, Type.DoubleObsidianWest));
                }
            }
        }
        return holes;
    }

    protected boolean isEnterable(BlockPos pos) {
        return mc.world.getBlockState(pos.up()).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(pos.up().up()).getBlock().equals(Blocks.AIR);
    }

    protected boolean isntUnsafe(BlockPos pos) {
        return !mc.world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK) && !mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN);
    }

    protected boolean isntSafe(BlockPos pos) {
        return !mc.world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK);
    }

    public boolean contains(final BlockPos pos){
        return Main.holeManager.getHoles().stream().anyMatch(holePos -> holePos.getPos().equals(pos));
    }

    public List<HolePos> getHoles() {
        return holes;
    }

    public static class HolePos {
        protected final BlockPos pos;
        protected final Type holeType;

        public HolePos(BlockPos pos, Type holeType ) {
            this.pos = pos;
            this.holeType = holeType;
        }

        public BlockPos getPos() {
            return pos;
        }

        public Type getHoleType() {
            return holeType;
        }

        public boolean isBedrock(){
            return holeType.equals(Type.Bedrock) || holeType.equals(Type.DoubleBedrockWest) || holeType.equals(Type.DoubleBedrockNorth);
        }

        public boolean isWestDouble(){
            return holeType.equals(Type.DoubleBedrockWest) || holeType.equals(Type.DoubleObsidianWest);
        }
        public boolean isDouble(){
            return holeType.equals(HoleManager.Type.DoubleBedrockNorth) || holeType.equals(Type.DoubleBedrockWest) || holeType.equals(Type.DoubleObsidianNorth) || holeType.equals(Type.DoubleObsidianWest);
        }
    }

    public enum Type {
        Bedrock,
        Obsidian,
        DoubleBedrockNorth,
        DoubleBedrockWest,
        DoubleObsidianNorth,
        DoubleObsidianWest
    }
}