package dev.zprestige.fire.module.player;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.BlockInteractEvent;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ColorBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.Objects;

public class PacketMine extends Module {
    public final Switch render = Menu.Switch("Render", false);
    public final ColorBox inactiveColor = Menu.Color("Inactive Color", Color.RED);
    public final ColorBox activeColor = Menu.Color("Active Color", Color.GREEN);
    public final Slider outlineWidth = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f);
    protected float size;
    protected boolean changed;
    protected EnumFacing facing;
    protected BlockPos activePos;

    @RegisterListener
    public void onTick(final TickEvent event) {
        if (activePos != null) {
            if (BlockUtil.getState(activePos).equals(Blocks.AIR)) {
                end();
                return;
            }
            if (size < 1.0f) {
                if (BlockUtil.getState(activePos).equals(Blocks.OBSIDIAN)) {
                    size += 0.025f;
                } else if (BlockUtil.getState(activePos).equals(Blocks.ENDER_CHEST)) {
                    size += 0.05f;
                } else {
                    size += 0.5f;
                }
            }
        }
    }

    @RegisterListener
    public void onFrame3D(FrameEvent.FrameEvent3D event) {
        if (render.GetSwitch() && activePos != null) {
            final ColorBox colorBox;
            if (size > 0.9f) {
                if (changed){
                    Main.fadeManager.removePosition(activePos);
                    changed = false;
                }
                colorBox = activeColor;
            } else {
                changed = true;
                colorBox = inactiveColor;
            }
            Main.fadeManager.createFadePosition(activePos, colorBox.GetColor(), colorBox.GetColor(), true, true, outlineWidth.GetSlider(), 50, colorBox.GetColor().getAlpha());
        }
    }

    @RegisterListener
    public void onClickBlock(final BlockInteractEvent.ClickBlock event) {
        if (mc.playerController.curBlockDamageMP > 0.1f) {
            mc.playerController.isHittingBlock = true;
        }
        if (activePos != null) {
            attemptBreak(activePos, facing);
        }
    }

    @RegisterListener
    public void onDamageBlock(final BlockInteractEvent.DamageBlock event) {
        if (activePos != null) {
            abort(activePos, facing);
        }
        if (!BlockUtil.getState(event.getPos()).equals(Blocks.BEDROCK)) {
            initiateBreaking(event.getPos(), event.getFacing());
            event.setCancelled(true);
        }
    }

    protected void attemptBreak(final BlockPos pos, final EnumFacing enumFacing) {
        final int slot = Main.inventoryManager.getItemFromHotbar(Items.DIAMOND_PICKAXE);
        if (slot != -1) {
            final int currentItem = mc.player.inventory.currentItem;
            Main.inventoryManager.switchToSlot(slot);
            Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, enumFacing));
            Main.inventoryManager.switchBack(currentItem);
        }
    }

    protected void initiateBreaking(final BlockPos pos, final EnumFacing enumFacing) {
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, enumFacing));
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, enumFacing));
        mc.player.swingArm(EnumHand.MAIN_HAND);
        activePos = pos;
        facing = enumFacing;
    }

    protected void abort(final BlockPos pos, final EnumFacing enumFacing) {
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, enumFacing));
        mc.playerController.isHittingBlock = false;
        mc.playerController.curBlockDamageMP = 0.0f;
        mc.world.sendBlockBreakProgress(mc.player.getEntityId(), pos, -1);
        mc.player.resetCooldown();
        end();
    }

    protected void end() {
        activePos = null;
        facing = null;
        size = 0.0f;
    }

}
