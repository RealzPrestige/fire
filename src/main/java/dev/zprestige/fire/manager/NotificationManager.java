package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.RegisteredClass;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.ModuleToggleEvent;
import dev.zprestige.fire.ui.hudeditor.components.impl.Notifications;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationManager extends RegisteredClass {
    protected final Minecraft mc = Main.mc;
    protected final Notifications notifications = getNotificationsComponent();

    public NotificationManager(){
        super(true, false);
    }

    public void addNotifications(final String text) {
        if (!notifications.isEnabled()) {
            return;
        }
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        notifications.notifications.add(new Notifications.Notification(text, scaledResolution, notifications.getPosition().getY()));
    }

    @RegisterListener
    public void onModuleEnableEvent(final ModuleToggleEvent.Enable event) {
        addNotifications(event.getModule().getName() + " has been toggled On.");
    }

    @RegisterListener
    public void onModuleDisableEvent(final ModuleToggleEvent.Disable event) {
        addNotifications(event.getModule().getName() + " has been toggled Off.");
    }

    protected Notifications getNotificationsComponent() {
        return (Notifications) Main.hudManager.getHudComponents().stream().filter(hudComponent -> hudComponent.getName().equals("Notifications")).findFirst().orElse(null);
    }
}
