package com.myra.dev.marian.commands.general;

import com.myra.dev.marian.database.Prefix;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import com.myra.dev.marian.utilities.management.Manager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandSubscribe(
        name = "calculate",
        aliases = {"cal"}
)
public class Calculate implements Command {
    @Override
    public void execute(GuildMessageReceivedEvent event, String[] arguments) throws Exception {
        //usage
        if (arguments.length == 0 || arguments.length > 3) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("calculate", null, event.getMember().getUser().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("\uD83E\uDDEE │ let the bot calculate something for you", "`" + Prefix.getPrefix(event.getGuild()) + "calculate <number> <operator> <number>`", false);
            event.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        //calculate
        try {
            double number1 = Double.parseDouble(arguments[0].replace(",", "."));
            double number2 = Double.parseDouble(arguments[2].replace(",", "."));
            double result = 0;
            switch (arguments[1]) {
                case "+":
                    result = number1 + number2;
                    break;
                case "-":
                    result = number1 - number2;
                    break;
                case "*":
                case "⋅":
                case "x":
                    result = number1 * number2;
                    break;
                case "/":
                case ":":
                    result = number1 / number2;
                    break;
            }
            EmbedBuilder calculated = new EmbedBuilder()
                    .setAuthor("calculated", null, event.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().blue)
                    .setDescription("the result of " + arguments[0] + " " + arguments[1].replace("*", "⋅").replace("x", "⋅").replace("/", ":") + " " + arguments[2] + " = " + result);
            event.getChannel().sendMessage(calculated.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
            Manager.getUtilities().error(event.getChannel(), "calculate", "\uD83E\uDDEE", "Error occurred", e.getMessage(), event.getAuthor().getEffectiveAvatarUrl());
        }
    }
}