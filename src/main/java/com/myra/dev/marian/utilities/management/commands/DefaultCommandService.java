package com.myra.dev.marian.utilities.management.commands;

import com.myra.dev.marian.database.Prefix;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;


/**
 * Default implementation of the {@link CommandService}.
 */
public class DefaultCommandService implements CommandService {

    private final Map<Command, CommandSubscribe> commands;

    public DefaultCommandService() {
        this.commands = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(Command command) {
        if (this.isSubscribed(command.getClass()) && this.hasCommand(command.getClass())) {
            this.commands.put(command, command.getClass().getAnnotation(CommandSubscribe.class));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(Command... commands) {
        for (Command command : commands) {
            this.register(command);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister(Command command) {
        this.commands.remove(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterAll() {
        this.commands.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Command, CommandSubscribe> getCommands() {
        return this.commands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processCommandExecution(GuildMessageReceivedEvent event) throws Exception {
        //get prefix
        String PREFIX = Prefix.getPrefix(event.getGuild());
        //return if message doesn't start with prefix
        if (!event.getMessage().getContentRaw().startsWith(PREFIX)) return;
        //get message without prefix
        String message = event.getMessage().getContentRaw().substring(PREFIX.length());
        //split message
        String[] splitMessage = message.split("\\s+");

        for (Map.Entry<Command, CommandSubscribe> entry : this.commands.entrySet()) {
            //create list for keywords
            List<String[]> executors = new ArrayList<>();
            //Add name of command
            executors.add(entry.getValue().name()
                    .replace("BOT_NAME", event.getJDA().getSelfUser().getName())
                    .replace("GUILD_NAME", event.getGuild().getName())
                    .split("\\s+")
            );
            //check if there are no aliases
            if (!entry.getValue().aliases()[0].equals("")) {
                //Add aliases of command
                for (String alias : entry.getValue().aliases()) {
                    String[] splitAlias = alias
                            .replace("BOT_NAME", event.getJDA().getSelfUser().getName())
                            .replace("GUILD_NAME", event.getGuild().getName())
                            .split("\\s+");
                    executors.add(splitAlias);
                }
            }
            //check for every executor
            for (String[] executor : executors) {
                //check if executor is longer than message
                if (executor.length > splitMessage.length) continue;
                //check for every argument
                boolean Continue = true;
                for (int i = 0; i < executor.length; i++) {
                    //if one argument doesn't equal the executor
                    if (!splitMessage[i].equalsIgnoreCase(executor[i])) {
                        Continue = false;
                        break;
                    }
                }
                if (!Continue) continue;
                /**
                 * run command
                 */
                //filter arguments
                String[] commandArguments = Arrays.copyOfRange(splitMessage, executor.length, splitMessage.length);
                //run command
                entry.getKey().execute(event, commandArguments);
            }
        }
    }

    private boolean isSubscribed(Class<?> cls) {
        return cls.isAnnotationPresent(CommandSubscribe.class);
    }

    private boolean hasCommand(Class<?> cls) {
        for (Class<?> implement : cls.getInterfaces()) {
            if (implement == Command.class) {
                return true;
            }
        }
        return false;
    }
}