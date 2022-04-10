package dev.zprestige.fire.module.misc;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.FrameEvent;
import dev.zprestige.fire.events.impl.PacketEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Descriptor(description = "Automatically provides kits for friends")
public class ZeroBKitBot extends Module {
    protected final Timer crouchTimer = new Timer(), chatTimer = new Timer(), tpaTimer = new Timer(), timeOut = new Timer(), teleportTimer = new Timer();
    protected String name = "";
    protected Kit kit = null;
    protected int amount = -1;
    protected boolean sentRequest;
    protected BlockPos respawnPosition, pvp, regear, olypepvp, olyperegear;
    protected final Vec3i[] surroundings = new Vec3i[]{
            new Vec3i(0, 0, -1),
            new Vec3i(0, 0, 1),
            new Vec3i(-1, 0, 0),
            new Vec3i(1, 0, 0)
    };
    protected final List<String> whiteList = Arrays.asList(
            "zPrestige_",
            "ILykCyds",
            "EmperiumKilla",
            "Olype",
            "ImpactOnTop",
            "Lasione",
            "_Vonz_",
            "DeeplyBored",
            "CloakedStar",
            "Nsm_",
            "0b0t_org",
            "_lyric",
            "karlitomyo",
            "lukegod101",
            "0w0_",
            "SnowClientUser",
            "TudouCat",
            "Vertent",
            "1drw",
            "Bepize",
            "KillArmenians",
            "VloneGuerrilla",
            "kkk_lol",
            "exptweaks",
            "Zaouski",
            "bigfatshrek"
    );

    @Override
    public void onEnable() {
        crouchTimer.syncTime();
        respawnPosition = null;
        pvp = null;
        regear = null;
        olyperegear = null;
        olypepvp = null;
    }

    @RegisterListener
    public void onTick(final TickEvent event) {
        if (!nullCheck()){
            return;
        }
        if (respawnPosition == null) {
            Main.chatManager.sendRemovableMessage("Crouch inside respawn point.", 1);
            if (mc.gameSettings.keyBindSneak.isKeyDown() && crouchTimer.getTime(1000)) {
                respawnPosition = BlockUtil.getPosition();
            }
            return;
        }
        if (pvp == null) {
            Main.chatManager.sendRemovableMessage("Crouch next to PvP Kit chest.", 1);
            if (mc.gameSettings.keyBindSneak.isKeyDown() && crouchTimer.getTime(1000)) {
                final BlockPos pos = BlockUtil.getPosition();
                for (final Vec3i vec3i : surroundings) {
                    final BlockPos pos1 = pos.add(vec3i);
                    if (BlockUtil.getState(pos1).equals(Blocks.CHEST) || BlockUtil.getState(pos1).equals(Blocks.TRAPPED_CHEST)) {
                        if (pos1 == respawnPosition) {
                            Main.chatManager.sendMessage("PvP pos can't be inside respawnPosition.");
                            return;
                        }
                        pvp = pos1;
                    }
                }
                crouchTimer.syncTime();
            }
            return;
        }
        if (regear == null) {
            Main.chatManager.sendRemovableMessage("Crouch next to Regear Kit chest.", 1);
            if (mc.gameSettings.keyBindSneak.isKeyDown() && crouchTimer.getTime(1000)) {
                final BlockPos pos = BlockUtil.getPosition();
                for (final Vec3i vec3i : surroundings) {
                    final BlockPos pos1 = pos.add(vec3i);
                    if (BlockUtil.getState(pos1).equals(Blocks.CHEST) || BlockUtil.getState(pos1).equals(Blocks.TRAPPED_CHEST)) {
                        if (pos1 == pvp || pos1 == respawnPosition) {
                            Main.chatManager.sendMessage("Regear pos can't be inside PvP kit pos or respawnPosition.");
                            return;
                        }
                        regear = pos1;
                    }
                }
                crouchTimer.syncTime();
            }
            return;
        }
        if (olypepvp == null) {
            Main.chatManager.sendRemovableMessage("Crouch next to Olype PvP Kit chest", 1);
            if (mc.gameSettings.keyBindSneak.isKeyDown() && crouchTimer.getTime(1000)) {
                final BlockPos pos = BlockUtil.getPosition();
                for (final Vec3i vec3i : surroundings) {
                    final BlockPos pos1 = pos.add(vec3i);
                    if (BlockUtil.getState(pos1).equals(Blocks.CHEST) || BlockUtil.getState(pos1).equals(Blocks.TRAPPED_CHEST)) {
                        if (pos1 == pvp || pos1 == respawnPosition || pos1 == regear) {
                            Main.chatManager.sendMessage("Olype PvP pos can't be inside PvP kit pos, Regear pos or respawnPosition.");
                            return;
                        }
                        olypepvp = pos1;
                    }
                }
                crouchTimer.syncTime();
            }
            return;
        }
        if (olyperegear == null) {
            Main.chatManager.sendRemovableMessage("Crouch next to Olype Regear Kit chest", 1);
            if (mc.gameSettings.keyBindSneak.isKeyDown() && crouchTimer.getTime(1000)) {
                final BlockPos pos = BlockUtil.getPosition();
                for (final Vec3i vec3i : surroundings) {
                    final BlockPos pos1 = pos.add(vec3i);
                    if (BlockUtil.getState(pos1).equals(Blocks.CHEST) || BlockUtil.getState(pos1).equals(Blocks.TRAPPED_CHEST)) {
                        if (pos1 == pvp || pos1 == respawnPosition || pos1 == regear || pos1 == olypepvp) {
                            Main.chatManager.sendMessage("Olype Regear pos can't be inside PvP kit pos, Regear pos, Olype PvP pos or respawnPosition.");
                            return;
                        }
                        olyperegear = pos1;
                    }
                }
                crouchTimer.syncTime();
            }
            return;
        }
        final BlockPos respawnCenter = new BlockPos(respawnPosition.getX() + 0.5f, respawnPosition.getY(), respawnPosition.getZ() + 0.5f);
        final AxisAlignedBB bb = mc.player.getEntityBoundingBox().shrink(0.45);
        final AxisAlignedBB bb1 = new AxisAlignedBB(respawnCenter).shrink(0.45);
        if (!bb.intersects(bb1)) {
            if (bb.minX < bb1.minX) {
                mc.player.motionX = 0.05;
            } else if (bb.minX > bb1.minX) {
                mc.player.motionX = -0.05;
            }
            if (bb.minZ < bb1.minZ) {
                mc.player.motionZ = 0.05;
            } else if (bb.minZ > bb1.minZ) {
                mc.player.motionZ = -0.05;
            }
        } else {
            mc.player.setPosition(respawnCenter.getX() + 0.5f, respawnCenter.getY(), respawnCenter.getZ() + 0.5f);
        }
        if (kit != null) {
            if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChest)) {
                mc.currentScreen = null;
            }
            if (mc.currentScreen == null && getAmountOfShulkersInInventory() < amount) {
                BlockPos pos = null;
                switch (kit) {
                    case PvP:
                        pos = pvp;
                        break;
                    case Regear:
                        pos = regear;
                        break;
                    case OlypePvP:
                        pos = olypepvp;
                        break;
                    case OlypeRegear:
                        pos = olyperegear;
                        break;
                }
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, Main.interactionManager.getFirstEnumFacing(pos), EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
            } else {
                int shulks = getAmountOfShulkersInInventory();
                if (shulks < amount) {
                    final GuiChest screen = (GuiChest) mc.currentScreen;
                    if (screen == null) {
                        return;
                    }
                    final ContainerChest containerChest = (ContainerChest) mc.player.openContainer;
                    for (int i = 0; i < containerChest.getLowerChestInventory().getSizeInventory(); i++) {
                        if (getAmountOfShulkersInInventory() >= amount) {
                            timeOut.syncTime();
                            mc.player.closeScreen();
                            return;
                        }
                        final Item item = screen.inventorySlots.getSlot(i).getStack().getItem();
                        if (Main.interactionManager.shulkers.contains(Block.getBlockFromItem(item))) {
                            mc.playerController.windowClick(containerChest.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
                        }
                    }
                }
                shulks = getAmountOfShulkersInInventory();
                if (shulks >= amount) {
                    if (!sentRequest && timeOut.getTime(100)) {
                        mc.player.sendChatMessage("/tpa " + name);
                        sentRequest = true;
                        tpaTimer.syncTime();
                    }
                    final BlockPos pos = BlockUtil.getPosition();
                    if (!pos.equals(respawnPosition)) {
                        if (teleportTimer.getTime(1000)) {
                            mc.player.sendChatMessage("/kill");
                            kit = null;
                            amount = -1;
                            name = "";
                            sentRequest = false;
                        }
                    } else {
                        teleportTimer.syncTime();
                    }
                    if (sentRequest && tpaTimer.getTime(30000)) {
                        kit = null;
                        amount = -1;
                        name = "";
                        sentRequest = false;
                    }
                }
            }
        }
    }

    protected int getAmountOfShulkersInInventory() {
        return (int) IntStream.range(1, 45).filter(i -> Main.interactionManager.shulkers.contains(Block.getBlockFromItem(mc.player.inventory.getStackInSlot(i).getItem()))).count();
    }

    @RegisterListener
    public void onFrame3D(final FrameEvent.FrameEvent3D event) {
        if (respawnPosition != null) {
            RenderUtil.drawBox(respawnPosition, new Color(0, 255, 255, 100));
            renderText("Respawn Position", respawnPosition);
        }
        if (pvp != null) {
            RenderUtil.drawBox(pvp, new Color(255, 0, 0, 100));
            renderText("PvP Kit Position", pvp);
        }
        if (regear != null) {
            RenderUtil.drawBox(regear, new Color(255, 255, 0, 100));
            renderText("Regear Kit Position", regear);
        }
        if (olypepvp != null) {
            RenderUtil.drawBox(olypepvp, new Color(120, 0, 255, 100));
            renderText("Olype PvP Kit Position", olypepvp);
        }
        if (olyperegear != null) {
            RenderUtil.drawBox(olyperegear, new Color(120, 255, 0, 100));
            renderText("Olype Regear Kit Position", olyperegear);
        }
    }

    @RegisterListener
    public void onPacketReceive(final PacketEvent.PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = ((SPacketChat) event.getPacket());
            final String upp = packet.getChatComponent().getUnformattedText();
            final String message = packet.getChatComponent().getUnformattedText().toLowerCase();
            String[] split = message.split(" ");
            if (split[1].startsWith("%")) {
                split = message.replace("%", "").split(" ");
                if (kit == null) {
                    name = upp.split(" ")[0].replace("<", "").replace(">", "").replace(" ", "");
                    if (!whiteList.contains(name)){
                        if (chatTimer.getTime(1500)){
                            mc.player.sendChatMessage("goofy ahh pooron don't even try goofy " + new Random().nextInt(10000) + ".");
                            chatTimer.syncTime();
                        }
                        return;
                    }
                    Main.chatManager.sendMessage("name: " + name);
                    Main.chatManager.sendMessage("split1: " + split[1]);
                    switch (split[1]) {
                        case "pvp":
                            kit = Kit.PvP;
                            break;
                        case "regear":
                            kit = Kit.Regear;
                            break;
                        case "olypepvp":
                            kit = Kit.OlypePvP;
                            break;
                        case "olyperegear":
                            kit = Kit.OlypeRegear;
                            break;
                        default:
                            if (chatTimer.getTime(1500)) {
                                mc.player.sendChatMessage("Kit not found " + new Random().nextInt(10000) + "!");
                                chatTimer.syncTime();
                            }
                            break;
                    }
                    final int kits = Integer.parseInt(split[2]);
                    Main.chatManager.sendMessage("split2: " + split[2]);
                    if (kits != 1 && kits != 2 && kits != 3) {
                        if (chatTimer.getTime(1500)) {
                            mc.player.sendChatMessage("Invalid amount (try 1-3) " + new Random().nextInt(10000) + "!");
                            chatTimer.syncTime();
                        }
                    } else {
                        amount = kits;
                    }
                } else if (chatTimer.getTime(1500)) {
                    mc.player.sendChatMessage("Already busy " + new Random().nextInt(10000) + "!");
                    chatTimer.syncTime();
                }
            }
        }
    }

    public void renderText(final String text, final BlockPos pos) {
        RenderUtil.draw3DText(text, pos.getX() + 0.5f - mc.getRenderManager().renderPosX, pos.getY() - mc.getRenderManager().renderPosY, pos.getZ() + 0.5f - mc.getRenderManager().renderPosZ, 5, 255, 255, 255, 255);
    }

    protected enum Kit {
        PvP,
        Regear,
        OlypePvP,
        OlypeRegear
    }
}
