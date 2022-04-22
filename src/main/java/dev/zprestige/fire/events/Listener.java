package dev.zprestige.fire.events;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.EventBus;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.*;
import dev.zprestige.fire.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

public class Listener extends RegisteredClass {
    protected final Minecraft mc = Main.mc;
    protected final EventBus eventBus = Main.eventBus;

    public Listener(){
        super(true, true);
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        if (checkNull() && event.getEntity().getEntityWorld().isRemote && event.getEntityLiving().equals(mc.player)) {
            final TickEvent tickEvent = new TickEvent();
            eventBus.post(tickEvent);
            if (mc.currentScreen == null) {
                Main.moduleManager.getModules().stream().filter(module -> module.getKeySetting().isHold()).forEach(module -> {
                    final boolean down = Keyboard.isKeyDown(module.getKeybind());
                    if (module.isEnabled() && !down) {
                        module.disableModule();
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        final Profiler profiler = mc.mcProfiler;
        profiler.startSection("fire");
        if (checkNull()) {
            final FrameEvent.FrameEvent3D frameEvent3D = new FrameEvent.FrameEvent3D(event.getPartialTicks());
            eventBus.post(frameEvent3D);
        }
        profiler.endSection();
    }

    @SubscribeEvent
    public void onRenderGameOverlayTextEvent(RenderGameOverlayEvent.Text event) {
        if (checkNull()) {
            final FrameEvent.FrameEvent2D frameEvent2D = new FrameEvent.FrameEvent2D(event.getPartialTicks());
            eventBus.post(frameEvent2D);
        }
    }

    @SubscribeEvent
    public void onFogColor(final EntityViewRenderEvent.FogColors event) {
        final FogEvent fogEvent = new FogEvent(event);
        eventBus.post(fogEvent);
        event.setRed(fogEvent.getFogColors().getRed());
        event.setGreen(fogEvent.getFogColors().getGreen());
        event.setBlue(fogEvent.getFogColors().getBlue());
    }

    @SubscribeEvent
    public void onDensity(final EntityViewRenderEvent.FogDensity event) {
        final FogDensityEvent fogDensityEvent = new FogDensityEvent(event.getDensity());
        eventBus.post(fogDensityEvent);
        event.setCanceled(true);
        event.setDensity(fogDensityEvent.getDensity());
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent ignoredEvent) {
        final ConnectionEvent.Join connectionEventJoin = new ConnectionEvent.Join();
        eventBus.post(connectionEventJoin);
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent ignoredEvent) {
        final ConnectionEvent.Disconnect connectionEventDisconnect = new ConnectionEvent.Disconnect();
        eventBus.post(connectionEventDisconnect);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        final DeathEvent deathEvent = new DeathEvent(event.getEntity());
        eventBus.post(deathEvent);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (checkNull() && Keyboard.getEventKeyState()) {
            final KeyEvent keyEvent = new KeyEvent(Keyboard.getEventKey());
            eventBus.post(keyEvent);
            Main.moduleManager.getModules().stream().filter(module -> module.getKeybind() == Keyboard.getEventKey()).forEach(Module::toggleModule);
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent event) {
        if (checkNull()) {
            final RenderOverlayEvent renderOverlayEvent = new RenderOverlayEvent(event.getType());
            Main.eventBus.post(renderOverlayEvent);
            if (renderOverlayEvent.isCancelled()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        final ItemInputUpdateEvent itemInputUpdateEvent = new ItemInputUpdateEvent(event.getMovementInput());
        eventBus.post(itemInputUpdateEvent);
    }

    @SubscribeEvent
    public void onLivingEntityUseItem(final LivingEntityUseItemEvent event) {
        final EntityUseItemEvent entityUseItemEvent = new EntityUseItemEvent();
        eventBus.post(entityUseItemEvent);
    }

    @RegisterListener
    public void onPacketReceive(PacketEvent.PacketReceiveEvent event) {
        if (checkNull() && event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            final Entity entity = packet.getEntity(mc.world);
            if (entity instanceof EntityPlayer && packet.getOpCode() == 35) {
                final TotemPopEvent totemPopEvent = new TotemPopEvent((EntityPlayer) entity);
                eventBus.post(totemPopEvent);
            }
        }
    }

    public boolean checkNull() {
        return mc.player != null && mc.world != null;
    }
}
