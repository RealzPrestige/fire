package dev.zprestige.fire.module.visual.blockhighlight;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.FrameEvent;
import dev.zprestige.fire.manager.fademanager.FadeManager;
import net.minecraft.util.math.BlockPos;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, BlockHighlight> {

    public Frame3DListener(final BlockHighlight blockHighlight){
        super(FrameEvent.FrameEvent3D.class, blockHighlight);
    }

    @Override
    public void invoke(final Object object){
        final BlockPos pos = mc.objectMouseOver.getBlockPos();
        if (!mc.world.getBlockState(pos).getMaterial().isReplaceable()){
            Main.fadeManager.addPosition(new FadeManager.FadePosition(pos, module.boxColor.GetColor(), module.outlineColor.GetColor(), module.box.GetSwitch(), module.outline.GetSwitch(), module.outlineWidth.GetSlider(), module.fadeSpeed.GetSlider()), module.boxColor.GetColor().getAlpha());
        }
    }
}
