package dev.zprestige.fire.module.visual.nametags;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.FrameEvent;
import dev.zprestige.fire.util.impl.RenderUtil;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class Frame3DListener extends EventListener<FrameEvent.FrameEvent3D, NameTags> {

    public Frame3DListener(final NameTags nameTags) {
        super(FrameEvent.FrameEvent3D.class, nameTags);
    }

    @Override
    public void invoke(final Object object) {
        if (module.nullCheck()) {
            for (final EntityPlayer entityPlayer : mc.world.playerEntities) {
                if (entityPlayer.equals(mc.player)) {
                    continue;
                }
                final Vec3d vec3d = RenderUtil.interpolateEntity(entityPlayer);
                final AxisAlignedBB bb = entityPlayer.getEntityBoundingBox();
                final float height = (float) (bb.maxY - bb.minY);
                final String name = entityPlayer.getName();
                final float health = (float) Math.ceil(entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount());
                final String healthString = String.valueOf(health);
                final NetworkPlayerInfo latencyInfo = mc.player.connection.getPlayerInfo(entityPlayer.getGameProfile().getId());
                final float latency = mc.player.connection == null ? 0 : latencyInfo.getResponseTime();
                final String latencyString = String.valueOf(latency), latencySuffix = "ms";
                final float nameWidth = Main.fontManager.getStringWidth(name), spaceWidth = Main.fontManager.getStringWidth(" "), healthWidth = Main.fontManager.getStringWidth(healthString), latencyWidth = Main.fontManager.getStringWidth(latencyString), latencySuffixWidth = Main.fontManager.getStringWidth(latencySuffix);
                final float totalStringWidth = (nameWidth + spaceWidth + healthWidth + spaceWidth + latencyWidth + latencySuffixWidth) / 2.0f;
                final double dist = ((mc.getRenderViewEntity() == null) ? mc.player : mc.getRenderViewEntity()).getDistance(vec3d.x + mc.getRenderManager().viewerPosX, vec3d.y + mc.getRenderManager().viewerPosY, vec3d.z + mc.getRenderManager().viewerPosZ);
                final float negativeY = (float) -((0.0018 + module.scale.GetSlider() * dist) / 100.0f);

                RenderUtil.prepare3D(vec3d.x, vec3d.y + height, vec3d.z, module.scale.GetSlider() / 1000.0f);
                RenderUtil.prepareScale(0.75f);
                float k = -37.5f;
                float m = -37.5f;
                final ItemStack mainHandStack = entityPlayer.getHeldItemMainhand();
                module.drawItem(mainHandStack, k / 0.75f, (negativeY - Main.fontManager.getFontHeight() - 10.0f) / 0.75f);
                if (mainHandStack.getCount() != 1 && !mainHandStack.isEmpty()) {
                    RenderUtil.prepareScale(0.65f);
                    Main.fontManager.drawStringWithShadow(String.valueOf(mainHandStack.getCount()),k / 0.65f, (negativeY - 5.5f) / 0.65f, -1);
                    RenderUtil.releaseScale();
                }
                for (ItemStack itemStack : entityPlayer.inventory.armorInventory) {
                    if (itemStack != null) {
                        k += 12.5f;
                        m += 15.0f;
                        if (!itemStack.isEmpty() && itemStack.getItem().isDamageable()) {
                            final double percent = Math.floor(module.getPercentage(itemStack));
                            final String string = (percent + "%").replace(".0", "");
                            RenderUtil.prepareScale(0.65f);
                            Main.fontManager.drawStringWithShadow(string, (m - ((Main.fontManager.getStringWidth(string) * 0.5f) / 2.0f)) / 0.65f, (negativeY - 22.5f) / 0.65f, new Color(module.redByPercentage(percent) / 255.0f, module.greenByPercentage(percent) / 255.0f, 0.0f).getRGB());
                            RenderUtil.releaseScale();
                        }
                        module.drawItem(itemStack, k / 0.75f, (negativeY - Main.fontManager.getFontHeight() - 10.0f) / 0.75f);
                    }
                }
                final ItemStack offhandStack = entityPlayer.getHeldItemOffhand();
                module.drawItem(offhandStack, (k + 12.5f) / 0.75f, (negativeY - Main.fontManager.getFontHeight() - 10.0f) / 0.75f);
                if (offhandStack.getCount() != 1 && !offhandStack.isEmpty()) {
                    RenderUtil.prepareScale(0.65f);
                    Main.fontManager.drawStringWithShadow(String.valueOf(offhandStack.getCount()), (m + 25.0f) / 0.65f, (negativeY - 5.5f) / 0.65f, -1);
                    RenderUtil.releaseScale();
                }
                RenderUtil.releaseScale();
                Main.fontManager.drawStringWithShadow(name, -(totalStringWidth), negativeY, Main.friendManager.isFriend(entityPlayer.getName()) ? Color.CYAN.getRGB() : -1);
                Main.fontManager.drawStringWithShadow(healthString, (-totalStringWidth + nameWidth + spaceWidth), negativeY, module.getHealthColor(health).getRGB());
                Main.fontManager.drawStringWithShadow(latencyString, (-totalStringWidth + nameWidth + spaceWidth + healthWidth + spaceWidth), negativeY, module.getLatencyColor(health).getRGB());
                Main.fontManager.drawStringWithShadow(latencySuffix, (-totalStringWidth + nameWidth + spaceWidth + healthWidth + spaceWidth + latencyWidth), negativeY, Main.friendManager.isFriend(entityPlayer.getName()) ? Color.CYAN.getRGB() : -1);
                RenderUtil.release3D();
            }
        }
    }
}
