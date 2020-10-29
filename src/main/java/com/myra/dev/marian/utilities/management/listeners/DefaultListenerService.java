package com.myra.dev.marian.utilities.management.listeners;

import com.myra.dev.marian.utilities.management.commands.CommandService;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of the {@link CommandService}.
 */
public class DefaultListenerService implements ListenerService {

    private final Map<Listener, ListenerSubscribe> listeners;

    public DefaultListenerService() {
        this.listeners = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(Listener listener) {
        if (this.isSubscribed(listener.getClass()) && this.hasListener(listener.getClass())) {
            this.listeners.put(listener, listener.getClass().getAnnotation(ListenerSubscribe.class));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(Listener... listeners) {
        for (Listener listener : listeners) {
            this.register(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister(Listener listener) {
        this.listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterAll() {
        this.listeners.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Listener, ListenerSubscribe> getListeners() {
        return this.listeners;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processCommandExecution(GuildMessageReceivedEvent event) throws Exception {
        for (Map.Entry<Listener, ListenerSubscribe> entry : this.listeners.entrySet()) {
            //if listener doesn't need an executor
            if (!entry.getValue().needsExecutor()) {
                entry.getKey().execute(event);
            }
            //check for executor
            else {
                //add executors
                List<String> executors = new ArrayList<>();
                executors.add(entry.getValue().name().toLowerCase());
                //check if there are no aliases
                if (!entry.getValue().aliases()[0].equals("")) {
                    //add aliases to executors
                    for (String alias : entry.getValue().aliases()) {
                        executors.add(alias.toLowerCase());
                    }
                }
                //check for executors
                if (executors.stream().anyMatch(event.getMessage().getContentRaw().toLowerCase()::contains)) {
                    //run listener
                    entry.getKey().execute(event);
                }
            }
        }
    }

    private boolean isSubscribed(Class<?> cls) {
        return cls.isAnnotationPresent(ListenerSubscribe.class);
    }

    private boolean hasListener(Class<?> cls) {
        for (Class<?> implement : cls.getInterfaces()) {
            if (implement == Listener.class) {
                return true;
            }
        }
        return false;
    }
}