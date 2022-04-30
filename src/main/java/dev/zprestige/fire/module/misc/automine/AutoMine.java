package dev.zprestige.fire.module.misc.automine;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.manager.playermanager.PlayerManager;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.ComboBox;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;
import dev.zprestige.fire.util.impl.BlockUtil;
import dev.zprestige.fire.util.impl.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Descriptor(description = "Automatically mines blocks for you to deal damage to enemies")
public class AutoMine extends Module {
    public final Slider targetRange = Menu.Slider("Target Range", 9.0f, 0.1f, 15.0f);
    public final Slider breakRange = Menu.Slider("Break Range", 5.0f, 0.1f, 6.0f);
    public final Switch damage = Menu.Switch("Damage", false);
    public final Switch onePointThirteen = Menu.Switch("One Point Thirteen", false);
    public final ComboBox priority = Menu.ComboBox("Priority", "Burrow-City-Surround", new String[]{
            "City-Surround-Burrow",
            "City-Burrow-Surround",
            "Surround-City-Burrow",
            "Surround-Burrow-City",
            "Burrow-City-Surround",
            "Burrow-Surround-City",
    });
    protected boolean started;
    protected BlockPos interactedPos;
    protected EnumFacing interactedFace;
    protected final Timer timer = new Timer();
    protected PlayerManager.Player target;
    protected Vec3i[] offsets = new Vec3i[]{
            new Vec3i(1, 0, 0),
            new Vec3i(-1, 0, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(0, 0, -1)
    };

    public AutoMine() {
        eventListeners = new EventListener[]{
                new TickListener(this)
        };
    }

    @Override
    public void onEnable() {
        if (!Main.moduleManager.getModuleByClass(PacketMine.class).isEnabled()) {
            Main.chatManager.sendMessage(ChatFormatting.RED + "[WARNING] " + ChatFormatting.RESET + "PacketMine is not enabled, this may cause AutoMine to not function as intended.");
        }
    }

    protected boolean perform(final String priority, final PlayerManager.Player player) {
        switch (priority) {
            case "Burrow":
                final BlockPos burrowPos = getBurrowPos(player);
                if (burrowPos != null) {
                    breakPos(burrowPos);
                    return true;
                }
                break;
            case "Surround":
                final BlockPos surroundPos = getClosestSurroundPos(player);
                if (surroundPos != null) {
                    breakPos(surroundPos);
                    return true;
                }
                break;
            case "City":
                final BlockPos cityPos = getClosestCityPos(player);
                if (cityPos != null) {
                    breakPos(cityPos);
                    return true;
                }
                break;
        }
        return false;
    }

    public PlayerManager.Player getTarget() {
        return target;
    }

    protected BlockPos getClosestCityPos(final PlayerManager.Player player) {
        final BlockPos pos = player.getPosition();
        final boolean thirteen = onePointThirteen.GetSwitch();
        final TreeMap<Double, BlockPos> posses = Arrays.stream(offsets).map(pos::add).filter(pos1 -> canMine(pos1) && (BlockUtil.canPosBeCrystalledSoon(pos1.down(), thirteen) || !hasAnyCrystallableSides(pos1, pos, thirteen).isEmpty())).collect(Collectors.toMap(pos1 -> mc.player.getDistanceSq(pos1), pos1 -> pos1, (a, b) -> b, TreeMap::new));
        if (!posses.isEmpty()) {
            return posses.firstEntry().getValue();
        }
        return null;
    }

    protected TreeMap<Double, BlockPos> hasAnyCrystallableSides(final BlockPos pos, final BlockPos blackListed, final boolean thirteen) {
        return Arrays.stream(offsets).map(pos::add).filter(pos1 -> !pos1.equals(blackListed)).filter(pos1 -> BlockUtil.canPosBeCrystalledSoon(pos1.down(), thirteen)).collect(Collectors.toMap(pos1 -> mc.player.getDistanceSq(pos1), pos1 -> pos1, (a, b) -> b, TreeMap::new));
    }

    protected BlockPos getClosestSurroundPos(final PlayerManager.Player player) {
        final BlockPos pos = player.getPosition();
        final boolean thirteen = onePointThirteen.GetSwitch();
        final TreeMap<Double, BlockPos> crystallablePosses = Arrays.stream(offsets).map(pos::add).filter(pos1 -> canMine(pos1) && surroundValid(pos1, thirteen, false)).collect(Collectors.toMap(pos1 -> mc.player.getDistanceSq(pos1), pos1 -> pos1, (a, b) -> b, TreeMap::new));
        if (!crystallablePosses.isEmpty()) {
            return crystallablePosses.firstEntry().getValue();
        } else {
            final TreeMap<Double, BlockPos> openPosses = Arrays.stream(offsets).map(pos::add).filter(pos1 -> canMine(pos1) && surroundValid(pos1, thirteen, true)).collect(Collectors.toMap(pos1 -> mc.player.getDistanceSq(pos1), pos1 -> pos1, (a, b) -> b, TreeMap::new));
            if (!openPosses.isEmpty()) {
                return openPosses.firstEntry().getValue();
            }
        }
        return null;
    }

    protected boolean surroundValid(final BlockPos pos, boolean thirteen, boolean noExtra) {
        return canMine(pos) && BlockUtil.canPosBeCrystalled(pos, thirteen) && (noExtra || BlockUtil.canPosBeCrystalledSoon(pos.down(), thirteen)) && mc.player.getDistanceSq(pos) < breakRange.GetSlider() * 2;
    }

    protected void breakPos(final BlockPos pos) {
        final EnumFacing enumFacing = Main.interactionManager.closestEnumFacing(pos);
        Main.interactionManager.interactBlock(pos, enumFacing);
        start(pos, enumFacing);
    }

    protected BlockPos getBurrowPos(final PlayerManager.Player player) {
        final BlockPos pos = player.getPosition();
        return canMine(pos) ? pos : null;
    }

    protected boolean canMine(final BlockPos pos) {
        return !mc.world.getBlockState(pos).getMaterial().isReplaceable() && !BlockUtil.getState(pos).equals(Blocks.BEDROCK) && !Main.interactionManager.getVisibleSides(pos).isEmpty() && mc.player.getDistanceSq(pos) < breakRange.GetSlider() * 2;
    }

    protected String getPriority(final int i) {
        final String string = priority.GetCombo();
        return string.split("-")[i];
    }

    protected void start(final BlockPos pos, final EnumFacing enumFacing) {
        timer.syncTime();
        started = true;
        interactedPos = pos;
        interactedFace = enumFacing;
    }
}
