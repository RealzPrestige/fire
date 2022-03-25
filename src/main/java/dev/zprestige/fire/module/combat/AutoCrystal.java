package dev.zprestige.fire.module.combat;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.PacketEvent;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.manager.PlayerManager;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.*;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.EntityUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class AutoCrystal extends Module {
    public final Slider targetRange = Menu.Slider("Target Range", 9.0f, 0.1f, 15.0f);
    public final ComboBox targetPriority = Menu.ComboBox("Target Priority", "UnSafe", new String[]{
            "Range",
            "UnSafe",
            "Health",
            "Fov",
    });
    public final Slider placeDelay = Menu.Slider("Place Delay", 0, 0, 500);
    public final Slider explodeDelay = Menu.Slider("Explode Delay", 50, 0, 500);
    public final Slider placeRange = Menu.Slider("Place Range", 5.0f, 0.1f, 6.0f);
    public final Slider explodeRange = Menu.Slider("Explode Range", 5.0f, 0.1f, 6.0f);
    public final ComboBox placeRaytrace = Menu.ComboBox("Place Raytrace", "None", new String[]{
            "Center",
            "Single",
            "Double",
            "Triple",
            "None"
    });
    public final ComboBox explodeRaytrace = Menu.ComboBox("Explode Raytrace", "None", new String[]{
            "Center",
            "Single",
            "Double",
            "Triple",
            "None"
    });
    public final Slider placeWallRange = Menu.Slider("Place Wall Range", 5.0f, 0.1f, 6.0f);
    public final Slider explodeWallRange = Menu.Slider("Explode Wall Range", 5.0f, 0.1f, 6.0f);
    public final ComboBox placeCalculations = Menu.ComboBox("Place Calculations", "Highest Damage", new String[]{
            "Highest Damage",
            "Lowest Distance",
            "Min Damage Distance",
            "Damage Minus Self Damage",
            "Adaptive"
    });
    public final ComboBox explodeCalculations = Menu.ComboBox("Explode Calculations", "Highest Damage", new String[]{
            "Highest Damage",
            "Lowest Distance",
            "Min Damage Distance",
            "Damage Minus Self Damage",
            "Adaptive"
    });
    public final Slider minPlaceDamage = Menu.Slider("Min Place Damage", 6.0f, 0.1f, 12.0f);
    public final Slider minExplodeDamage = Menu.Slider("Min Explode Damage", 6.0f, 0.1f, 12.0f);
    public final Slider maxSelfPlaceDamage = Menu.Slider("Max Self Place Damage", 8.0f, 0.1f, 12.0f);
    public final Slider maxSelfExplodeDamage = Menu.Slider("Max Self Explode Damage", 8.0f, 0.1f, 12.0f);
    public final Slider placeAntiSuicide = Menu.Slider("Place Anti Suicide", 0.0f, 0.0f, 10.0f);
    public final Slider explodeAntiSuicide = Menu.Slider("Explode Anti Suicide", 0.0f, 0.0f, 10.0f);
    public final Switch placeRotate = Menu.Switch("Place Rotate", false);
    public final Switch explodeRotate = Menu.Switch("Explode Rotate", false);
    public final Switch syncRotations = Menu.Switch("Sync Rotations", false);
    public final Switch placePacket = Menu.Switch("Place Packet", false);
    public final Switch explodePacket = Menu.Switch("Explode Packet", false);
    public final Switch placeSilentSwitch = Menu.Switch("Place Silent Switch", false);
    public final Switch placeInhibit = Menu.Switch("Place Inhibit", false);
    public final Switch onePointThirteen = Menu.Switch("One Point Thirteen", false);
    public final Switch explodeAntiWeakness = Menu.Switch("Explode Anti Weakness", false);
    public final Switch multiTask = Menu.Switch("MultiTask", true);
    public final Switch setDead = Menu.Switch("Set Dead", false);
    public final Switch pyroMode = Menu.Switch("Pyro Mode", true);
    public final ComboBox predict = Menu.ComboBox("Predict", "Normal", new String[]{
            "None",
            "Normal",
            "Ultra",
            "UltraChain"
    });
    public final Slider facePlaceHealth = Menu.Slider("Face Place Health", 15.0f, 0.1f, 36.0f);
    public final Switch facePlaceSlow = Menu.Switch("Face Place Slow", false);
    public final Key facePlaceForceKey = Menu.Key("Face Place Force Key", Keyboard.KEY_NONE);
    public final Switch render = Menu.Switch("Render", false);
    public final Slider fadeSpeed = Menu.Slider("Fade Speed", 25.0f, 0.1f, 100.0f);
    public final Switch box = Menu.Switch("Box", false);
    public final ColorBox boxColor = Menu.Color("Box Color", new Color(255, 255, 255, 120));
    public final Switch outline = Menu.Switch("Outline", false);
    public final ColorBox outlineColor = Menu.Color("Outline Color", new Color(255, 255, 255, 255));
    public final Slider outlineWidth = Menu.Slider("Outline Width", 1.0f, 0.1f, 5.0f);

    protected final Timer[] timers = new Timer[]{new Timer(), new Timer()};
    protected final ArrayList<EntityEnderCrystal> pyroCrystals = new ArrayList<>();
    protected int pyroId = -1;

    @Override
    public void onDisable() {
        new ArrayList<>(pyroCrystals).forEach(entityEnderCrystal1 -> {
            entityEnderCrystal1.setDead();
            pyroCrystals.remove(entityEnderCrystal1);
        });
    }

    @RegisterListener
    public void onPacketSend(PacketEvent.PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            if (predict.GetCombo().equals("Ultra") || predict.GetCombo().equals("UltraChain")) {
                final BlockPos eventPos = ((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getPos();
                EntityEnderCrystal entityEnderCrystal = null;
                try {
                    for (Entity entity : mc.world.loadedEntityList) {
                        if (entity instanceof EntityEnderCrystal && entity.getDistanceSq(eventPos.up()) < 1.0f) {
                            entityEnderCrystal = (EntityEnderCrystal) entity;
                        }
                    }
                } catch (ConcurrentModificationException ignored) {
                }
                if (entityEnderCrystal != null) {
                    explodeCrystal(entityEnderCrystal);
                    if (predict.GetCombo().equals("UltraChain")) {
                        placeCrystal(eventPos);
                    }
                }
            }
        }
    }

    @RegisterListener
    public void onPacketReceive(PacketEvent.PacketReceiveEvent event) {
        if (!nullCheck()) {
            return;
        }
        if (predict.GetCombo().equals("None")) {
            return;
        }
        try {
            if (predict.GetCombo().equals("Normal") && event.getPacket() instanceof SPacketSpawnObject && ((SPacketSpawnObject) event.getPacket()).getType() == 51 && mc.world.getEntityByID(((SPacketSpawnObject) event.getPacket()).getEntityID()) instanceof EntityEnderCrystal) {
                final EntityEnderCrystal entityEnderCrystal = (EntityEnderCrystal) mc.world.getEntityByID(((SPacketSpawnObject) event.getPacket()).getEntityID());
                explodeCrystal(entityEnderCrystal);
            }
        } catch (ConcurrentModificationException ignored) {
        }
    }


    @RegisterListener
    public void onTick(TickEvent event) {
        if (!multiTask.GetSwitch() && mc.player.getHeldItemMainhand().getItem().equals(Items.GOLDEN_APPLE) && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            return;
        }
        final PlayerManager.Player player = EntityUtil.getClosestTarget(targetPriority(targetPriority.GetCombo()), targetRange.GetSlider());
        if (player != null) {
            performAutoCrystal(player, facePlace(player) && facePlaceSlow.GetSwitch());
        }
    }

    public void performAutoCrystal(final PlayerManager.Player player, final boolean facePlace) {
        if (timers[0].getTime((long) placeDelay.GetSlider())) {
            final BlockPos pos = calculatePosition(player);
            if (pos != null) {
                placeCrystal(pos);
                timers[0].syncTime();
            }
        }
        if (timers[1].getTime(facePlace ? 500 : (long) explodeDelay.GetSlider())) {
            final EntityEnderCrystal entityEnderCrystal = calculateCrystal(player);
            if (entityEnderCrystal != null) {
                explodeCrystal(entityEnderCrystal);
                timers[1].syncTime();
            }
        }
    }

    public void explodeCrystal(final EntityEnderCrystal entityEnderCrystal) {
        boolean switched = false;
        int currentItem = -1;
        PotionEffect weakness = mc.player.getActivePotionEffect(MobEffects.WEAKNESS);
        if (explodeAntiWeakness.GetSwitch() && weakness != null && !mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_SWORD)) {
            int swordSlot = Main.inventoryManager.getItemFromHotbar(Items.DIAMOND_SWORD);
            currentItem = mc.player.inventory.currentItem;
            Main.inventoryManager.switchToSlot(swordSlot);
            switched = true;
        }
        if (explodeRotate.GetSwitch()) {
            Main.rotationManager.faceEntity(entityEnderCrystal, syncRotations.GetSwitch());
        }
        if (explodePacket.GetSwitch()) {
            if (mc.getConnection() != null) {
                mc.getConnection().getNetworkManager().channel().writeAndFlush(new CPacketUseEntity(entityEnderCrystal));
            }
        } else {
            mc.playerController.attackEntity(mc.player, entityEnderCrystal);
        }
        if (setDead.GetSwitch()) {
            mc.world.removeEntityFromWorld(entityEnderCrystal.entityId);
        }
        mc.player.swingArm(EnumHand.MAIN_HAND);
        if (switched) {
            Main.inventoryManager.switchBack(currentItem);
        }
        new ArrayList<>(pyroCrystals).forEach(entityEnderCrystal1 -> {
            entityEnderCrystal1.setDead();
            pyroCrystals.remove(entityEnderCrystal1);
            playPyroSound(entityEnderCrystal1.getPosition());
        });
    }

    protected void playPyroSound(final BlockPos pos){
        mc.world.playSound(mc.player, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 0);
    }

    public void placeCrystal(final BlockPos pos) {
        final EnumHand crystalHand = getCrystalHand();
        if (!placeSilentSwitch.GetSwitch() && crystalHand == null || (placeInhibit.GetSwitch() && !mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.up())).isEmpty())) {
            return;
        }
        final int slot = Main.inventoryManager.getItemFromHotbar(Items.END_CRYSTAL);
        final int currentItem = mc.player.inventory.currentItem;
        boolean switched = false;
        if (placeSilentSwitch.GetSwitch() && slot != -1 && crystalHand == null) {
            Main.inventoryManager.switchToSlot(slot);
            switched = true;
        }
        if (placeRotate.GetSwitch()) {
            Main.rotationManager.facePos(pos, syncRotations.GetSwitch());
        }
        final EnumHand finalHand = crystalHand == null ? EnumHand.MAIN_HAND : crystalHand;
        if (placePacket.GetSwitch()) {
            if (mc.getConnection() != null) {
                mc.getConnection().getNetworkManager().channel().writeAndFlush(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, finalHand, 0.5f, 0.5f, 0.5f));
            }
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.UP, new Vec3d(mc.player.posX, -mc.player.posY, -mc.player.posZ), finalHand);
        }
        mc.player.swingArm(finalHand);
        if (switched) {
            Main.inventoryManager.switchBack(currentItem);
        }
        Main.fadeManager.createFadePosition(pos, boxColor.GetColor(), outlineColor.GetColor(), box.GetSwitch(), outline.GetSwitch(), outlineWidth.GetSlider(), fadeSpeed.GetSlider(), boxColor.GetColor().getAlpha());
        if (pyroMode.GetSwitch() && !containsPyro(pos)) {
            final EntityEnderCrystal crystal = new EntityEnderCrystal(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            mc.world.addEntityToWorld(pyroId -= 1, crystal);
            pyroCrystals.add(crystal);
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
            return crystals2.lastEntry().getValue();
        }
        return null;
    }

    protected BlockPos calculatePosition(final PlayerManager.Player player) {
        final HashMap<BlockPos, CalculationComponent> posses = new HashMap<>();
        for (BlockPos pos : BlockUtil.getCrystallableBlocks(placeRange.GetSlider() * 2, onePointThirteen.GetSwitch())) {
            final ArrayList<Entity> intersecting = mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up())).stream().filter(entity -> !(entity instanceof EntityEnderCrystal)).collect(Collectors.toCollection(ArrayList::new));
            if (!intersecting.isEmpty() || mc.player.getDistanceSq(pos) > (BlockUtil.isNotVisible(pos, raytrace(placeRaytrace.GetCombo()).getOffset()) ? placeWallRange.GetSlider() * 3 : placeRange.GetSlider() * 3)) {
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
            return posses2.lastEntry().getValue();
        }
        return null;
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


    public enum Raytrace {
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
