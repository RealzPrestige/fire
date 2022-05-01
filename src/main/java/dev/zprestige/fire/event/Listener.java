package dev.zprestige.fire.event;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.*;
import dev.zprestige.fire.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

public class Listener {
    protected final Minecraft mc = Main.mc;

    public Listener() {
        MinecraftForge.EVENT_BUS.register(this);
        Main.eventBus.registerListeners(new EventListener[]{
                new PacketReceiveListener()
        });
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        if (checkNull() && event.getEntity().getEntityWorld().isRemote && event.getEntityLiving().equals(mc.player)) {
            final TickEvent tickEvent = new TickEvent();
            Main.eventBus.invokeEvent(tickEvent);
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
            Main.eventBus.invokeEvent(frameEvent3D);
        }
        profiler.endSection();
    }

    @SubscribeEvent
    public void onRenderGameOverlayTextEvent(RenderGameOverlayEvent.Text event) {
        if (checkNull()) {
            final FrameEvent.FrameEvent2D frameEvent2D = new FrameEvent.FrameEvent2D(event.getPartialTicks());
            Main.eventBus.invokeEvent(frameEvent2D);
        }
    }

    @SubscribeEvent
    public void onFogColor(final EntityViewRenderEvent.FogColors event) {
        final FogEvent fogEvent = new FogEvent(event);
        Main.eventBus.invokeEvent(fogEvent);
        event.setRed(fogEvent.getFogColors().getRed());
        event.setGreen(fogEvent.getFogColors().getGreen());
        event.setBlue(fogEvent.getFogColors().getBlue());
    }

    @SubscribeEvent
    public void onDensity(final EntityViewRenderEvent.FogDensity event) {
        final FogDensityEvent fogDensityEvent = new FogDensityEvent(event.getDensity());
        Main.eventBus.invokeEvent(fogDensityEvent);
        event.setCanceled(true);
        event.setDensity(fogDensityEvent.getDensity());
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent ignoredEvent) {
        final ConnectionEvent.Join connectionEventJoin = new ConnectionEvent.Join();
        Main.eventBus.invokeEvent(connectionEventJoin);
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent ignoredEvent) {
        final ConnectionEvent.Disconnect connectionEventDisconnect = new ConnectionEvent.Disconnect();
        Main.eventBus.invokeEvent(connectionEventDisconnect);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        final DeathEvent deathEvent = new DeathEvent(event.getEntity());
        Main.eventBus.invokeEvent(deathEvent);
    }

    @SubscribeEvent
    public void onItemUpdateEvent(final InputUpdateEvent event){
        final ItemInputUpdateEvent itemInputUpdateEvent = new ItemInputUpdateEvent(event.getMovementInput());
        Main.eventBus.invokeEvent(itemInputUpdateEvent);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (checkNull() && Keyboard.getEventKeyState()) {
            final KeyEvent keyEvent = new KeyEvent(Keyboard.getEventKey());
            Main.eventBus.invokeEvent(keyEvent);
            Main.moduleManager.getModules().stream().filter(module -> module.getKeybind() == Keyboard.getEventKey()).forEach(Module::toggleModule);
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent event) {
        if (checkNull()) {
            final RenderOverlayEvent renderOverlayEvent = new RenderOverlayEvent(event.getType());
            Main.eventBus.invokeEvent(renderOverlayEvent);
            if (renderOverlayEvent.isCancelled()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        final ItemInputUpdateEvent itemInputUpdateEvent = new ItemInputUpdateEvent(event.getMovementInput());
        Main.eventBus.invokeEvent(itemInputUpdateEvent);
    }

    @SubscribeEvent
    public void onLivingEntityUseItem(final LivingEntityUseItemEvent event) {
        final EntityUseItemEvent entityUseItemEvent = new EntityUseItemEvent(event.getEntity());
        Main.eventBus.invokeEvent(entityUseItemEvent);
    }

    @SubscribeEvent
    public void onCameraSetup(final EntityViewRenderEvent.CameraSetup event) {
        final CameraSetupEvent cameraSetupEvent = new CameraSetupEvent(event.getYaw(), event.getPitch());
        Main.eventBus.invokeEvent(cameraSetupEvent);
        event.setYaw(cameraSetupEvent.getYaw());
        event.setPitch(cameraSetupEvent.getPitch());
    }

    public boolean checkNull() {
        return mc.player != null && mc.world != null;
    }
}
