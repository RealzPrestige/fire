package dev.zprestige.fire.manager;

import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class MineManager extends RegisteredClass {
    protected final Timer timeoutTimer = new Timer();
    protected boolean mining;
    protected BlockPos miningPos;

    @RegisterListener
    public void onTick(final TickEvent event){
        if (miningPos != null) {
            if (BlockUtil.getState(miningPos).equals(Blocks.AIR)) {
                setMiningPos(null);
                timeoutTimer.syncTime();
            } else {
                setMining(true);
            }
        } else if (timeoutTimer.getTime(1000)){
            setMining(false);
        }
    }

    public void setMiningPos(BlockPos miningPos) {
        this.miningPos = miningPos;
    }

    public boolean isntMining() {
        return !mining;
    }

    public void setMining(boolean mining) {
        this.mining = mining;
    }
}
