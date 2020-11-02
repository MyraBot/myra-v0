package com.myra.dev.marian.utilities.management.commands;

import com.myra.dev.marian.database.MongoDb;
import com.myra.dev.marian.database.allMethods.Database;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;


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
        String PREFIX = new Database(event.getGuild()).get("prefix");
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
                // Check if command is disabled
                if (isDisabled(entry.getValue().command(), event.getGuild())) return;
                // Check for required permissions
                if (!hasPermissions(event.getMember(), entry.getValue().requires())) return;
                //filter arguments
                String[] commandArguments = Arrays.copyOfRange(splitMessage, executor.length, splitMessage.length);
                //run command
                entry.getKey().execute(new CommandContext(PREFIX, event, commandArguments));
            }
        }
    }

    private boolean isDisabled(String command, Guild guild) {
        // Get listener document
        Document commands = (Document) MongoDb.getInstance().getCollection("guilds").find(eq("guildId", guild.getId())).first().get("commands");
        // If command isn't in the database
        if (command.equals("")) return false;
        // Return value of command
        return !commands.getBoolean(command);
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

    private boolean hasPermissions(Member member, String requiresPermission) {
        switch (requiresPermission) {
            case "member":
                return true;
            case "moderator":
                return member.hasPermission(Permission.VIEW_AUDIT_LOGS);
            case "administrator":
                return member.hasPermission(Permission.ADMINISTRATOR);
        }
        return false;
    }
}