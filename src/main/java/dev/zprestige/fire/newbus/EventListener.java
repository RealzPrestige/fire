package dev.zprestige.fire.newbus;


public abstract class EventListener<E>  {
    protected final Class<? extends E> listener;
    protected Event event;

    public EventListener(final Class<? extends E> listener){
        this.listener =  listener;
    }

    public Class<? extends E> getListener() {
        return listener;
    }

    public void invoke(final Object object){
        this.event = (Event) object;
    }
}
