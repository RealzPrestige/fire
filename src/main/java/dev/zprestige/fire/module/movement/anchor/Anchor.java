package dev.zprestige.fire.module.movement.anchor;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Descriptor(description = "Makes getting into holes easier")
public class Anchor extends Module {
    public final ComboBox mode = Menu.ComboBox("Mode", "Stop Motion", new String[]{
            "Stop Motion",
            "Pull",
            "Snap"
    });
    public final Slider snapFall = Menu.Slider("Snap Fall", 1.0f, 0.1f, 5.0f).visibility(z -> mode.GetCombo().equals("Snap"));
    public final Slider pullSpeed = Menu.Slider("Pull Speed", 1.0f, 0.1f, 5.0f).visibility(z -> mode.GetCombo().equals("Pull"));
    public final Slider height = Menu.Slider("Height", 1.0f, 0.1f, 5.0f).visibility(z -> mode.GetCombo().equals("Stop Motion"));
    public final Slider range = Menu.Slider("Range", 1.0f, 0.1f, 5.0f);
    public final Switch onGround = Menu.Switch("On Ground", true).visibility(z -> mode.GetCombo().equals("Snap"));
    protected final Vec3i[] offsets = new Vec3i[]{
            new Vec3i(0, 0, -1),
            new Vec3i(0, 0, 1),
            new Vec3i(-1, 0, 0),
            new Vec3i(1, 0, 0)
    };
    protected final Timer timer = new Timer();
    protected BlockPos pos;

    public Anchor() {
        eventListeners = new EventListener[]{
                new TickListener(this)
        };
    }

    protected void setMovementsFalse() {
        mc.gameSettings.keyBindForward.pressed = false;
        mc.gameSettings.keyBindBack.pressed = false;
        mc.gameSettings.keyBindRight.pressed = false;
        mc.gameSettings.keyBindLeft.pressed = false;
    }

    protected boolean isOver(final BlockPos pos) {
        final AxisAlignedBB bb = mc.player.getEntityBoundingBox();
        for (int i = 0; i < height.GetSlider(); i++) {
            final BlockPos pos1 = pos.up(i);
            final AxisAlignedBB bb1 = new AxisAlignedBB(pos1);
            if (bb.intersects(bb1)) {
                return true;
            }
        }
        return false;
    }

    protected boolean intersects(final BlockPos pos) {
        final AxisAlignedBB bb = mc.player.getEntityBoundingBox();
        for (int i = 0; i < 5; i++) {
            for (final Vec3i vec3i : offsets) {
                final BlockPos pos1 = pos.up(i).add(vec3i);
                final AxisAlignedBB bb1 = new AxisAlignedBB(pos1);
                if (bb.intersects(bb1)) {
                    return false;
                }
            }
        }
        return true;
    }

}
