package dev.zprestige.fire.module.visual.itemnames;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.FrameEvent;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, ItemNames> {

    public Frame3DListener(final ItemNames itemNames) {
        super(FrameEvent.FrameEvent3D.class, itemNames);
    }

    @Override
    public void invoke(final Object object) {
        if (module.nullCheck()) {
            for (final Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityItem) {
                    final EntityItem entityItem = (EntityItem) entity;
                    final ItemStack itemStack = entityItem.getItem();
                    final String string = itemStack.getDisplayName() + " x" + entityItem.getItem().getCount();
                    final float stringWidth = Main.fontManager.getStringWidth(string);
                    final Vec3d vec3d = RenderUtil.interpolateEntity(entity);
                    final AxisAlignedBB bb = entity.getEntityBoundingBox();
                    RenderUtil.prepare3D(vec3d.x, vec3d.y + ((bb.maxY - bb.minY) / 2.0f), vec3d.z, module.scale.GetSlider() / 1000);
                    Main.fontManager.drawStringWithShadow(string, -(stringWidth / 2.0f), 0, -1);
                    RenderUtil.release3D();
                }
            }
        }
    }
}
