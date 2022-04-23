package dev.zprestige.fire.event.bus;

import dev.zprestige.fire.Main;
import net.minecraft.client.Minecraft;

public abstract class EventListener<E, M> {
    protected final Minecraft mc = Main.mc;
    protected final Class<? extends E> listener;
    public M module;

    public EventListener(final Class<? extends E> listener) {
        this.listener = listener;
    }

    public EventListener(final Class<? extends E> listener, M module) {
        this.listener = listener;
        this.module = module;
    }

    public Class<? extends E> getListener() {
        return listener;
    }

    public void invoke(final Object object) {
    }
}
