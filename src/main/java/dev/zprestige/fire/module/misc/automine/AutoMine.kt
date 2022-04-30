package dev.zprestige.fire.module.misc.automine

import com.mojang.realmsclient.gui.ChatFormatting
import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.manager.playermanager.PlayerManager
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module
import dev.zprestige.fire.module.player.packetmine.PacketMine
import dev.zprestige.fire.settings.impl.ComboBox
import dev.zprestige.fire.settings.impl.Slider
import dev.zprestige.fire.settings.impl.Switch
import dev.zprestige.fire.util.impl.BlockUtil
import dev.zprestige.fire.util.impl.Timer
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import java.util.*
import java.util.stream.Collectors

@Descriptor(description = "Automatically mines blocks for you to deal damage to enemies")
class AutoMine : Module() {
    val targetRange: Slider = Menu.Slider("Target Range", 9.0f, 0.1f, 15.0f)
    private val breakRange: Slider = Menu.Slider("Break Range", 5.0f, 0.1f, 6.0f)
    val damage: Switch = Menu.Switch("Damage", false)
    val onePointThirteen: Switch = Menu.Switch("One Point Thirteen", false)
    private val priority: ComboBox = Menu.ComboBox("Priority", "Burrow-City-Surround", arrayOf(
        "City-Surround-Burrow",
        "City-Burrow-Surround",
        "Surround-City-Burrow",
        "Surround-Burrow-City",
        "Burrow-City-Surround",
        "Burrow-Surround-City"
    )
    )
    var started = false
    var interactedPos: BlockPos? = null
    var interactedFace: EnumFacing? = null
    val timer = Timer()
    var target: PlayerManager.Player? = null
    private var offsets = arrayOf(
        Vec3i(1, 0, 0),
        Vec3i(-1, 0, 0),
        Vec3i(0, 0, 1),
        Vec3i(0, 0, -1)
    )

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            TickListener(this)
        )
    }

    override fun onEnable() {
        if (!Main.moduleManager.getModuleByClass(PacketMine::class.java).isEnabled) {
            Main.chatManager.sendMessage(ChatFormatting.RED.toString() + "[WARNING] " + ChatFormatting.RESET + "PacketMine is not enabled, this may cause AutoMine to not function as intended.")
        }
    }

    fun perform(priority: String?, player: PlayerManager.Player): Boolean {
        when (priority) {
            "Burrow" -> {
                val burrowPos = getBurrowPos(player)
                if (burrowPos != null) {
                    breakPos(burrowPos)
                    return true
                }
            }
            "Surround" -> {
                val surroundPos = getClosestSurroundPos(player)
                if (surroundPos != null) {
                    breakPos(surroundPos)
                    return true
                }
            }
            "City" -> {
                val cityPos = getClosestCityPos(player)
                if (cityPos != null) {
                    breakPos(cityPos)
                    return true
                }
            }
        }
        return false
    }

    private fun getClosestCityPos(player: PlayerManager.Player): BlockPos? {
        val pos = player.position
        val thirteen = onePointThirteen.GetSwitch()
        val posses = Arrays.stream(offsets).map { vec: Vec3i? -> pos.add(vec!!) }
            .filter { pos1: BlockPos ->
                canMine(pos1) && (BlockUtil.canPosBeCrystalledSoon(pos1.down(),
                    thirteen
                ) || !hasAnyCrystallableSides(pos1,
                    pos,
                    thirteen
                ).isEmpty())
            }.collect(Collectors.toMap<BlockPos, Double, BlockPos?, TreeMap<Double, BlockPos>>(
                { pos1: BlockPos? -> mc.player.getDistanceSq(pos1!!) },
                { pos1: BlockPos? -> pos1 },
                { _: BlockPos?, b: BlockPos? -> b }
            ) { TreeMap() })
        return if (!posses.isEmpty()) {
            posses.firstEntry().value
        } else null
    }

    private fun hasAnyCrystallableSides(pos: BlockPos, blackListed: BlockPos, thirteen: Boolean): TreeMap<Double, BlockPos> {
        return Arrays.stream(offsets).map { vec: Vec3i? ->
            pos.add(vec!!)
        }.filter { pos1: BlockPos -> pos1 != blackListed }.filter { pos1: BlockPos ->
            BlockUtil.canPosBeCrystalledSoon(pos1.down(),
                thirteen
            )
        }.collect(Collectors.toMap<BlockPos, Double, BlockPos?, TreeMap<Double, BlockPos>>(
            { pos1: BlockPos? -> mc.player.getDistanceSq(pos1!!) },
            { pos1: BlockPos? -> pos1 },
            { _: BlockPos?, b: BlockPos? -> b }
        ) { TreeMap() })
    }

    private fun getClosestSurroundPos(player: PlayerManager.Player): BlockPos? {
        val pos = player.position
        val thirteen = onePointThirteen.GetSwitch()
        val crystallablePosses = Arrays.stream(offsets).map { vec: Vec3i? -> pos.add(vec!!) }
            .filter { pos1: BlockPos ->
                canMine(pos1) && surroundValid(pos1, thirteen, false)
            }.collect(Collectors.toMap<BlockPos, Double, BlockPos?, TreeMap<Double, BlockPos>>(
                { pos1: BlockPos? -> mc.player.getDistanceSq(pos1!!) },
                { pos1: BlockPos? -> pos1 },
                { _: BlockPos?, b: BlockPos? -> b }
            ) { TreeMap() })
        if (!crystallablePosses.isEmpty()) {
            return crystallablePosses.firstEntry().value
        } else {
            val openPosses = Arrays.stream(offsets).map { vec: Vec3i? -> pos.add(vec!!) }
                .filter { pos1: BlockPos ->
                    canMine(pos1) && surroundValid(pos1, thirteen, true)
                }.collect(Collectors.toMap<BlockPos, Double, BlockPos?, TreeMap<Double, BlockPos>>(
                    { pos1: BlockPos? -> mc.player.getDistanceSq(pos1!!) },
                    { pos1: BlockPos? -> pos1 },
                    { _: BlockPos?, b: BlockPos? -> b }
                ) { TreeMap() })
            if (!openPosses.isEmpty()) {
                return openPosses.firstEntry().value
            }
        }
        return null
    }

    private fun surroundValid(pos: BlockPos, thirteen: Boolean, noExtra: Boolean): Boolean {
        return canMine(pos) && BlockUtil.canPosBeCrystalled(pos,
            thirteen
        ) && (noExtra || BlockUtil.canPosBeCrystalledSoon(pos.down(),
            thirteen
        )) && mc.player.getDistanceSq(pos) < breakRange.GetSlider() * 2
    }

    private fun breakPos(pos: BlockPos?) {
        val enumFacing = Main.interactionManager.closestEnumFacing(pos)
        Main.interactionManager.interactBlock(pos, enumFacing)
        start(pos, enumFacing)
    }

    private fun getBurrowPos(player: PlayerManager.Player): BlockPos? {
        val pos = player.position
        return if (canMine(pos)) pos else null
    }

    private fun canMine(pos: BlockPos?): Boolean {
        return !mc.world.getBlockState(pos!!).material.isReplaceable && BlockUtil.getState(pos) != Blocks.BEDROCK && Main.interactionManager.getVisibleSides(
            pos
        ).isNotEmpty() && mc.player.getDistanceSq(pos) < breakRange.GetSlider() * 2
    }

    fun getPriority(i: Int): String {
        val string = priority.GetCombo()
        return string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[i]
    }

    private fun start(pos: BlockPos?, enumFacing: EnumFacing?) {
        timer.syncTime()
        started = true
        interactedPos = pos
        interactedFace = enumFacing
    }
}
