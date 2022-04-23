package dev.zprestige.fire.module.combat.autocrystal;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.MotionUpdateEvent;
import dev.zprestige.fire.settings.impl.*;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.EntityUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

@Descriptor(description = "Places and breaks crystals to damage enemies")
public class AutoCrystal extends Module {
    public final Slider targetRange = Menu.Slider("Target Range", 9.0f, 0.1f, 15.0f).panel("Target");
    public final ComboBox targetPriority = Menu.ComboBox("Target Priority", "UnSafe", new String[]{
            "Range",
            "UnSafe",
            "Health",
            "Fov",
    }).panel("Target");

    public final Slider placeDelay = Menu.Slider("Place Delay", 0, 0, 500).panel("Timing");
    public final Slider explodeDelay = Menu.Slider("Explode Delay", 50, 0, 500).panel("Timing");

    public final Slider placeRange = Menu.Slider("Place Range", 5.0f, 0.1f, 6.0f).panel("Ranges");
    public final Slider explodeRange = Menu.Slider("Explode Range", 5.0f, 0.1f, 6.0f).panel("Ranges");
    public final Switch advancedRaytrace = Menu.Switch("Advanced Raytrace", false).panel("Ranges");
    public final ComboBox placeRaytrace = Menu.ComboBox("Place Raytrace", "None", new String[]{
            "Center",
            "Single",
            "Double",
            "Triple",
            "None"
    }).visibility(z -> !advancedRaytrace.GetSwitch()).panel("Ranges");
    public final ComboBox explodeRaytrace = Menu.ComboBox("Explode Raytrace", "None", new String[]{
            "Center",
            "Single",
            "Double",
            "Triple",
            "None"
    }).visibility(z -> !advancedRaytrace.GetSwitch()).panel("Ranges");
    public final Slider placeWallRange = Menu.Slider("Place Wall Range", 5.0f, 0.1f, 6.0f).panel("Ranges");
    public final Slider explodeWallRange = Menu.Slider("Explode Wall Range", 5.0f, 0.1f, 6.0f).panel("Ranges");

    public final ComboBox placeCalculations = Menu.ComboBox("Place Calculations", "Highest Damage", new String[]{
            "Highest Damage",
            "Lowest Distance",
            "Min Damage Distance",
            "Damage Minus Self Damage",
            "Adaptive"
    }).panel("Calculations");
    public final ComboBox explodeCalculations = Menu.ComboBox("Explode Calculations", "Highest Damage", new String[]{
            "Highest Damage",
            "Lowest Distance",
            "Min Damage Distance",
            "Damage Minus Self Damage",
            "Adaptive"
    }).panel("Calculations");
    public final Slider minPlaceDamage = Menu.Slider("Min Place Damage", 6.0f, 0.1f, 12.0f).panel("Calculations");
    public final Slider minExplodeDamage = Menu.Slider("Min Explode Damage", 6.0f, 0.1f, 12.0f).panel("Calculations");
    public final Slider maxSelfPlaceDamage = Menu.Slider("Max Self Place Damage", 8.0f, 0.1f, 12.0f).panel("Calculations");
    public final Slider maxSelfExplodeDamage = Menu.Slider("Max Self Explode Damage", 8.0f, 0.1f, 12.0f).panel("Calculations");
    public final Slider placeAntiSuicide = Menu.Slider("Place Anti Suicide", 0.0f, 0.0f, 10.0f).panel("Calculations");
    public final Slider explodeAntiSuicide = Menu.Slider("Explode Anti Suicide", 0.0f, 0.0f, 10.0f).panel("Calculations");

    public final Switch placeRotate = Menu.Switch("Place Rotate", false).panel("Rotations");
    public final Switch explodeRotate = Menu.Switch("Explode Rotate", false).panel("Rotations");
    public final ComboBox inAirRotations = Menu.ComboBox("InAir", "Ignore Full", new String[]{
            "None",
            "Ignore Rotations",
            "Ignore Full",
    }).visibility(z -> placeRotate.GetSwitch() || explodeRotate.GetSwitch()).panel("Rotations");
   public final Switch raytraceFix = Menu.Switch("Raytrace Fix", false).panel("Rotations");
    public final Slider raytraceTicks = Menu.Slider("Raytrace Ticks ", 10.0f, 0.1f, 40.0f).visibility(z -> raytraceFix.GetSwitch()).panel("Rotations");
    public final Slider raytraceTimeoutTicks = Menu.Slider("Raytrace Timeout Ticks ", 2.0f, 0.1f, 40.0f).visibility(z -> raytraceFix.GetSwitch()).panel("Rotations");

    public final Switch predictMotion = Menu.Switch("Predict Motion", false).panel("Predict Motion");
    public final Slider predictMotionFactor = Menu.Slider("Predict Motion Factor", 2.0f, 1.0f, 20.0f).visibility(z -> predictMotion.getValue()).panel("Predict Motion");
    public final Switch predictMotionVisualize = Menu.Switch("Predict Motion Visualize", false).visibility(z -> predictMotion.getValue()).panel("Predict Motion");

    public final Switch placePacket = Menu.Switch("Place Packet", false).panel("Other");
    public final Switch explodePacket = Menu.Switch("Explode Packet", false).panel("Other");
    public final Switch placeSilentSwitch = Menu.Switch("Place Silent Switch", false).panel("Other");
    public final Switch explodeSilentSwitch = Menu.Switch("Explode Silent Switch", false).panel("Other");
    public final Switch placeInhibit = Menu.Switch("Place Inhibit", false).panel("Other");
    public final Switch onePointThirteen = Menu.Switch("One Point Thirteen", false).panel("Other");
    public final Switch explodeAntiWeakness = Menu.Switch("Explode Anti Weakness", false).panel("Other");
    public final Switch autoMineTargetPrefer = Menu.Switch("Auto Mine Target Prefer", false).panel("Other");
    public final Switch destroyLoot = Menu.Switch("Destroy Loot", false).panel("Other");
    public final Switch multiTask = Menu.Switch("MultiTask", true).panel("Other");
    public final Switch autoSwitch = Menu.Switch("Auto Switch", true).panel("Other");

    public final Switch pyroMode = Menu.Switch("Pyro Mode", true).panel("Predicting");
    public final ComboBox setDead = Menu.ComboBox("Set Dead", "None", new String[]{
            "None",
            "Safe",
            "Unsafe"
    }).panel("Predicting");
    public final ComboBox predict = Menu.ComboBox("Predict", "Normal", new String[]{
            "None",
            "Normal",
            "Ultra",
            "UltraChain"
    }).panel("Predicting");

    public final Slider facePlaceHealth = Menu.Slider("Face Place Health", 15.0f, 0.1f, 36.0f).panel("Faceplacing");
    public final Switch facePlaceSlow = Menu.Switch("Face Place Slow", false).panel("Faceplacing");
    public final Key facePlaceForceKey = Menu.Key("Face Place Force Key", Keyboard.KEY_NONE).panel("Faceplacing");

    public final Switch render = Menu.Switch("Render", false).panel("Rendering");
    public final ComboBox animation = Menu.ComboBox("Animation", "Fade", new String[]{
            "None",
            "Fade",
            "Interpolate",
            "Shrink"
    }).visibility(z -> render.GetSwitch()).panel("Rendering");
    public final Slider fadeSpeed = Menu.Slider("Fade Speed", 25.0f, 0.1f, 100.0f).visibility(z -> render.GetSwitch() && animation.GetCombo().equals("Fade")).panel("Rendering");
    public final Slider interpolateSpeed = Menu.Slider("Interpolate Speed", 25.0f, 0.1f, 100.0f).visibility(z -> render.GetSwitch() && animation.GetCombo().equals("Interpolate")).panel("Rendering");
    public final Slider shrinkSpeed = Menu.Slider("Shrink Speed", 25.0f, 0.1f, 100.0f).visibility(z -> render.GetSwitch() && animation.GetCombo().equals("Shrink")).panel("Rendering");
    public final Switch renderCPS = Menu.Switch("Render CPS", false).panel("Rendering");
    public final Switch box = Menu.Switch("Box", false).visibility(z -> render.GetSwitch()).panel("Rendering");
    public final ColorBox boxColor = Menu.Color("Box Color", new Color(255, 255, 255, 120)).visibility(z -> box.GetSwitch() && render.GetSwitch()).panel("Rendering");
    public final Switch outline = Menu.Switch("Outline", false).visibility(z -> render.GetSwitch()).panel("Rendering");
    public final ColorBox outlineColor = Menu.Color("Outline Color", new Color(255, 255, 255, 255)).visibility(z -> render.GetSwitch() && outline.GetSwitch()).panel("Rendering");
    public final Slider outlineWidth = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f).visibility(z -> render.GetSwitch() && outline.GetSwitch()).panel("Rendering");

    protected final Timer[] timers = new Timer[]{new Timer(), new Timer()};
    protected final ArrayList<EntityEnderCrystal> pyroCrystals = new ArrayList<>(), attackedCrystals = new ArrayList<>();
    protected final ArrayList<Long> crystalsPerSecond = new ArrayList<>();
    protected int ticks = 0, timeoutTicks = 0;
    protected BlockPos pos;
    protected AxisAlignedBB bb;
    protected int pyroId = -1;
    protected EntityOtherPlayerMP entityOtherPlayerMP;
    protected final Random random = new Random();

    public AutoCrystal(){
        eventListeners = new EventListener[]{
                new Frame3DListener(this),
                new MotionUpdateListener(this),
                new PacketReceiveListener(this),
                new PacketSendListener(this)
        };
    }
    @Override
    public void onDisable() {
        new ArrayList<>(pyroCrystals).forEach(entityEnderCrystal1 -> {
            entityEnderCrystal1.setDead();
            pyroCrystals.remove(entityEnderCrystal1);
        });
        bb = null;
        pos = null;
    }

    @Override
    public void onEnable() {
        if (!nullCheck()) {
            return;
        }
        if (mc.player.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL)) {
            final int crystalSlot = Main.inventoryManager.getItemFromHotbar(Items.END_CRYSTAL);
            if (crystalSlot != -1) {
                mc.player.inventory.currentItem = crystalSlot;
            }
        }
    }

    protected void handleSwitch() {
        if (autoSwitch.GetSwitch()) {
            if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                if (!mc.player.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE)) {
                    final int gappleSlot = Main.inventoryManager.getItemFromHotbar(Items.GOLDEN_APPLE);
                    if (gappleSlot != -1) {
                        mc.player.inventory.currentItem = gappleSlot;
                    }
                }
            } else {
                if (!mc.player.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL)) {
                    final int crystalSlot = Main.inventoryManager.getItemFromHotbar(Items.END_CRYSTAL);
                    if (crystalSlot != -1) {
                        mc.player.inventory.currentItem = crystalSlot;
                    }
                }
            }
        }
    }
    protected float rand() {
        return MathHelper.clamp(-20 + random.nextFloat() * 20, -20, 20);
    }

    protected void performAutoCrystal(final PlayerManager.Player player, final MotionUpdateEvent event) {
        if (raytraceFix.GetSwitch()) {
            if (ticks >= raytraceTicks.GetSlider()) {
                if (timeoutTicks >= raytraceTimeoutTicks.GetSlider()) {
                    ticks = 0;
                    timeoutTicks = 0;
                    return;
                }
                event.setYaw(-mc.player.rotationYaw);
                event.setPitch(rand());
                timeoutTicks++;
                return;
            }
        }
        boolean rotated = false;
        if (timers[0].getTime((long) placeDelay.GetSlider())) {
            final BlockPos pos = calculatePosition(player);
            if (pos != null) {
                placeCrystal(pos, event);
                timers[0].syncTime();
                if (placeRotate.GetSwitch()) {
                    rotated = true;
                }
            }
        }
        if (timers[1].getTime(Keyboard.isKeyDown(facePlaceForceKey.GetKey()) && facePlaceSlow.GetSwitch() ? 500 : (long) explodeDelay.GetSlider())) {
            final EntityEnderCrystal entityEnderCrystal = calculateCrystal(player);
            if (entityEnderCrystal != null) {
                explodeCrystal(entityEnderCrystal, event);
                timers[1].syncTime();
                if (explodeRotate.GetSwitch()) {
                    rotated = true;
                }
            }
        }
        if (rotated) {
            ticks++;
        }
    }

    public void explodeCrystal(final EntityEnderCrystal entityEnderCrystal, final MotionUpdateEvent event) {
        boolean switched = false;
        int currentItem = -1;
        final PotionEffect weakness = mc.player.getActivePotionEffect(MobEffects.WEAKNESS);
        if (explodeAntiWeakness.GetSwitch() && weakness != null && !mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_SWORD)) {
            int swordSlot = Main.inventoryManager.getItemFromHotbar(Items.DIAMOND_SWORD);
            if (swordSlot != -1) {
                currentItem = mc.player.inventory.currentItem;
                Main.inventoryManager.switchToSlot(swordSlot);
                switched = true;
            }
        }
        final int slot = Main.inventoryManager.getItemFromHotbar(Items.END_CRYSTAL);
        boolean switched1 = false;
        if (explodeSilentSwitch.GetSwitch() && slot != -1 && getCrystalHand() == null) {
            Main.inventoryManager.switchToSlot(slot);
            switched1 = true;
        }
        if (explodeRotate.GetSwitch() && event != null) {
            switch (inAirRotations.GetCombo()) {
                case "None":
                    Main.rotationManager.faceEntity(entityEnderCrystal, event);
                    break;
                case "Ignore Rotations":
                    if (mc.player.onGround) {
                        Main.rotationManager.faceEntity(entityEnderCrystal, event);
                    }
                    break;
                case "Ignore Full":
                    if (!mc.player.onGround) {
                        return;
                    }
                    Main.rotationManager.faceEntity(entityEnderCrystal, event);
                    break;
            }
        }
        if (explodePacket.GetSwitch()) {
            if (mc.getConnection() != null) {
                mc.getConnection().getNetworkManager().channel().writeAndFlush(new CPacketUseEntity(entityEnderCrystal));
            }
        } else {
            mc.playerController.attackEntity(mc.player, entityEnderCrystal);
        }
        mc.player.swingArm(EnumHand.MAIN_HAND);
        if (setDead.GetCombo().equals("Unsafe")) {
            entityEnderCrystal.setDead();
        }
        if (switched1) {
            Main.inventoryManager.switchBack(currentItem);
        }
        if (switched) {
            Main.inventoryManager.switchBack(currentItem);
        }
        new ArrayList<>(pyroCrystals).forEach(entityEnderCrystal1 -> {
            entityEnderCrystal1.setDead();
            pyroCrystals.remove(entityEnderCrystal1);
            playPyroSound(entityEnderCrystal1.getPosition());
        });
        attackedCrystals.add(entityEnderCrystal);
        if (pos == null){
            this.pos = entityEnderCrystal.getPosition().down();
        }
    }

    protected void playPyroSound(final BlockPos pos) {
        mc.world.playSound(mc.player, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 0);
    }

    public void placeCrystal(final BlockPos pos, final MotionUpdateEvent event) {
        final EnumHand crystalHand = getCrystalHand();
        if ((!placeSilentSwitch.GetSwitch() && crystalHand == null) || (placeInhibit.GetSwitch() && !mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.up())).isEmpty())) {
            return;
        }
        final BlockUtil.EnumOffset enumOffset = BlockUtil.getVisibleEnumFacing(pos);
        float x = 0.5f, y = 0.5f, z = 0.5f;
        if (advancedRaytrace.GetSwitch()) {
            if (enumOffset == null) {
                return;
            } else {
                x = enumOffset.getX();
                y = enumOffset.getY();
                z = enumOffset.getZ();
            }
        }
        final int slot = Main.inventoryManager.getItemFromHotbar(Items.END_CRYSTAL);
        final int currentItem = mc.player.inventory.currentItem;
        boolean switched = false;
        if (placeSilentSwitch.GetSwitch() && slot != -1 && crystalHand == null) {
            Main.inventoryManager.switchToSlot(slot);
            switched = true;
        }
        if (placeRotate.GetSwitch() && event != null) {
            switch (inAirRotations.GetCombo()) {
                case "None":
                    Main.rotationManager.facePos(pos.add(x, y, z), event);
                    break;
                case "Ignore Rotations":
                    if (mc.player.onGround) {
                        Main.rotationManager.facePos(pos.add(x, y, z), event);
                    }
                    break;
                case "Ignore Full":
                    if (!mc.player.onGround) {
                        return;
                    }
                    Main.rotationManager.facePos(pos.add(x, y, z), event);
                    break;
            }
        }
        final EnumHand finalHand = crystalHand == null ? EnumHand.MAIN_HAND : crystalHand;
        if (placePacket.GetSwitch()) {
            if (mc.getConnection() != null) {
                mc.getConnection().getNetworkManager().channel().writeAndFlush(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, finalHand, x, y, z));
            }
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.UP, new Vec3d(mc.player.posX, -mc.player.posY, -mc.player.posZ), finalHand);
        }
        mc.player.swingArm(finalHand);
        if (switched) {
            Main.inventoryManager.switchBack(currentItem);
        }
        if (animation.GetCombo().equals("Fade")) {
            Main.fadeManager.createFadePosition(pos, boxColor.GetColor(), outlineColor.GetColor(), box.GetSwitch(), outline.GetSwitch(), outlineWidth.GetSlider(), fadeSpeed.GetSlider(), boxColor.GetColor().getAlpha());
        }
        if (animation.GetCombo().equals("Shrink")) {
            Main.shrinkManager.createShrinkPosition(pos, boxColor.GetColor(), outlineColor.GetColor(), box.GetSwitch(), outline.GetSwitch(), outlineWidth.GetSlider(), shrinkSpeed.GetSlider());
        }
        if (pyroMode.GetSwitch() && !containsPyro(pos)) {
            final EntityEnderCrystal crystal = new EntityEnderCrystal(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            mc.world.addEntityToWorld(pyroId -= 1, crystal);
            pyroCrystals.add(crystal);
        }
        this.pos = pos;
        if (bb == null) {
            bb = new AxisAlignedBB(pos);
        }
    }

    protected boolean containsPyro(BlockPos pos) {
        pos = new BlockPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
        for (EntityEnderCrystal entityEnderCrystal : pyroCrystals) {
            if (entityEnderCrystal.getDistanceSq(pos) < 2.0f) {
                return true;
            }
        }
        return false;
    }

    protected EntityEnderCrystal calculateCrystal(final PlayerManager.Player player) {
        final HashMap<EntityEnderCrystal, CalculationComponent> crystals = new HashMap<>();
        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) {
                continue;
            }
            final BlockPos pos = entity.getPosition();
            if (mc.player.getDistance(entity) > (BlockUtil.isNotVisible(pos, raytrace(explodeRaytrace.GetCombo()).getOffset()) ? explodeWallRange.GetSlider() : explodeRange.GetSlider()) || pyroCrystals.contains(entity)) {
                continue;
            }
            final EntityEnderCrystal entityEnderCrystal = (EntityEnderCrystal) entity;
            final double damage = BlockUtil.calculateEntityDamage(entityEnderCrystal, player);
            final double selfDamage = BlockUtil.calculateEntityDamage(entityEnderCrystal, mc.player);
            final double selfHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
            final float minDamage = facePlace(player) ? 2.0f : minExplodeDamage.GetSlider();
            if (damage > minDamage && selfDamage < maxSelfExplodeDamage.GetSlider() && selfDamage < selfHealth - explodeAntiSuicide.GetSlider()) {
                crystals.put(entityEnderCrystal, new CalculationComponent(damage, player.getDistanceToPos(pos), selfDamage));
            }
        }
        if (!crystals.isEmpty()) {
            final TreeMap<Double, EntityEnderCrystal> crystals2 = new TreeMap<>();
            for (Map.Entry<EntityEnderCrystal, CalculationComponent> entry : crystals.entrySet()) {
                switch (explodeCalculations.GetCombo()) {
                    case "Highest Damage":
                        crystals2.put(entry.getValue().getDamage(), entry.getKey());
                        break;
                    case "Lowest Distance":
                        crystals2.put(targetRange.GetSlider() - entry.getValue().getDistance(), entry.getKey());
                        break;
                    case "Min Damage Distance":
                        crystals2.put(entry.getValue().getDamage() + entry.getValue().getDistance(), entry.getKey());
                        break;
                    case "Damage Minus Self Damage":
                        crystals2.put(entry.getValue().getDamage() - entry.getValue().getSelfDamage(), entry.getKey());
                        break;
                    case "Adaptive":
                        crystals2.put(entry.getValue().getDamage() - entry.getValue().getSelfDamage() + (targetRange.GetSlider() - entry.getValue().getDistance()), entry.getKey());
                        break;
                }
            }
            if (!crystals2.isEmpty()) {
                return crystals2.lastEntry().getValue();
            }
        }
        return null;
    }

    protected BlockPos calculatePosition(final PlayerManager.Player player) {
        final HashMap<BlockPos, CalculationComponent> posses = new HashMap<>();
        for (BlockPos pos : BlockUtil.getCrystallableBlocks(placeRange.GetSlider(), onePointThirteen.GetSwitch())) {
            final ArrayList<Entity> intersecting = mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up())).stream().filter(entity -> !(entity instanceof EntityEnderCrystal)).collect(Collectors.toCollection(ArrayList::new));
            if (!intersecting.isEmpty() || mc.player.getDistanceSq(pos) / 2 > (BlockUtil.isNotVisible(pos, raytrace(placeRaytrace.GetCombo()).getOffset()) ? placeWallRange.GetSlider(): placeRange.GetSlider())) {
                continue;
            }
            final double damage = BlockUtil.calculatePosDamage(pos, player);
            final double selfDamage = BlockUtil.calculatePosDamage(pos, mc.player);
            final double selfHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
            final float minDamage = facePlace(player) ? 2.0f : minPlaceDamage.GetSlider();
            if (damage > minDamage && selfDamage < maxSelfPlaceDamage.GetSlider() && selfDamage < selfHealth - placeAntiSuicide.GetSlider()) {
                posses.put(pos, new CalculationComponent(damage, player.getDistanceToPos(pos), selfDamage));
            }
        }
        if (!posses.isEmpty()) {
            final TreeMap<Double, BlockPos> posses2 = new TreeMap<>();
            for (Map.Entry<BlockPos, CalculationComponent> entry : posses.entrySet()) {
                switch (placeCalculations.GetCombo()) {
                    case "Highest Damage":
                        posses2.put(entry.getValue().getDamage(), entry.getKey());
                        break;
                    case "Lowest Distance":
                        posses2.put(targetRange.GetSlider() - entry.getValue().getDistance(), entry.getKey());
                        break;
                    case "Min Damage Distance":
                        posses2.put(entry.getValue().getDamage() + entry.getValue().getDistance(), entry.getKey());
                        break;
                    case "Damage Minus Self Damage":
                        posses2.put(entry.getValue().getDamage() - entry.getValue().getSelfDamage(), entry.getKey());
                        break;
                    case "Adaptive":
                        posses2.put(entry.getValue().getDamage() - entry.getValue().getSelfDamage() + (targetRange.GetSlider() - entry.getValue().getDistance()), entry.getKey());
                        break;
                }
            }
            if (!posses2.isEmpty()) {
                return posses2.lastEntry().getValue();
            }
        }
        return null;
    }

    protected void setupEntity(final PlayerManager.Player player, final double[] next) {
        final EntityOtherPlayerMP entityOtherPlayerMP1 = new EntityOtherPlayerMP(mc.world, player.getEntityPlayer().getGameProfile());
        final EntityPlayer entity = player.getEntityPlayer();
        entityOtherPlayerMP1.copyLocationAndAnglesFrom(entity);
        entityOtherPlayerMP1.rotationYawHead = entity.rotationYawHead;
        entityOtherPlayerMP1.prevRotationYawHead = entity.rotationYawHead;
        entityOtherPlayerMP1.rotationYaw = entity.rotationYaw;
        entityOtherPlayerMP1.prevRotationYaw = entity.rotationYaw;
        entityOtherPlayerMP1.rotationPitch = entity.rotationPitch;
        entityOtherPlayerMP1.prevRotationPitch = entity.rotationPitch;
        entityOtherPlayerMP1.cameraYaw = entity.rotationYaw;
        entityOtherPlayerMP1.cameraPitch = entity.rotationPitch;
        entityOtherPlayerMP1.limbSwing = entity.limbSwing;
        entityOtherPlayerMP1.setPosition(next[0], next[1], next[2]);
        entityOtherPlayerMP = entityOtherPlayerMP1;
    }

    protected boolean facePlace(final PlayerManager.Player player) {
        return player.getHealth() < facePlaceHealth.GetSlider() || Keyboard.isKeyDown(facePlaceForceKey.GetKey());
    }

    protected EntityUtil.TargetPriority targetPriority(final String string) {
        return Arrays.stream(EntityUtil.TargetPriority.values()).filter(targetPriority1 -> targetPriority1.toString().equals(string)).findFirst().orElse(null);
    }

    protected Raytrace raytrace(final String string) {
        return Arrays.stream(Raytrace.values()).filter(raytrace -> raytrace.toString().equalsIgnoreCase(string)).findFirst().orElse(null);
    }

    protected EnumHand getCrystalHand() {
        return mc.player.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.MAIN_HAND : mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : null;
    }

    protected static class CalculationComponent {
        protected final double damage, distance, selfDamage;

        public CalculationComponent(double damage, double distance, double selfDamage) {
            this.damage = damage;
            this.distance = distance;
            this.selfDamage = selfDamage;
        }

        public double getDamage() {
            return damage;
        }

        public double getDistance() {
            return distance;
        }

        public double getSelfDamage() {
            return selfDamage;
        }
    }


    protected enum Raytrace {
        CENTER(0.5),
        SINGLE(1.5),
        DOUBLE(2.5),
        TRIPLE(3.5),
        NONE(-100);

        private final double offset;

        Raytrace(double offset) {
            this.offset = offset;
        }

        public double getOffset() {
            return offset;
        }
    }
}
