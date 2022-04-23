package dev.zprestige.fire.manager.notificationmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.ui.hudeditor.components.impl.Notifications;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationManager {
    protected final Minecraft mc = Main.mc;
    protected final Notifications notifications = getNotificationsComponent();

    public NotificationManager() {
        Main.eventBus.registerListeners(new EventListener[]{
                new ModuleDisableListener(),
                new ModuleEnableListener()
        });
    }

    public void addNotifications(final String text) {
        if (!notifications.isEnabled()) {
            return;
        }
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        notifications.notifications.add(new Notifications.Notification(text, scaledResolution, notifications.getPosition().getY()));
    }

    protected Notifications getNotificationsComponent() {
        return (Notifications) Main.hudManager.getHudComponents().stream().filter(hudComponent -> hudComponent.getName().equals("Notifications")).findFirst().orElse(null);
    }
}
