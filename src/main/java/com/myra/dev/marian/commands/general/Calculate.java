package com.myra.dev.marian.commands.general;


import com.myra.dev.marian.utilities.management.Manager;
import com.myra.dev.marian.utilities.management.commands.Command;
import com.myra.dev.marian.utilities.management.commands.CommandContext;
import com.myra.dev.marian.utilities.management.commands.CommandSubscribe;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandSubscribe(
        name = "calculate",
        aliases = {"cal"}
)
public class Calculate implements Command {
    @Override
    public void execute(CommandContext ctx) throws Exception {
        //usage
        if (ctx.getArguments().length == 0 || ctx.getArguments().length > 3) {
            EmbedBuilder usage = new EmbedBuilder()
                    .setAuthor("calculate", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().gray)
                    .addField("\uD83E\uDDEE │ let the bot calculate something for you", "`" + ctx.getPrefix() + "calculate <number> <operator> <number>`", false);
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }
        //calculate
        try {
            double number1 = Double.parseDouble(ctx.getArguments()[0].replace(",", "."));
            double number2 = Double.parseDouble(ctx.getArguments()[2].replace(",", "."));
            double result = 0;
            switch (ctx.getArguments()[1]) {
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
                    .setAuthor("calculated", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setColor(Manager.getUtilities().blue)
                    .setDescription("the result of " + ctx.getArguments()[0] + " " + ctx.getArguments()[1].replace("*", "⋅").replace("x", "⋅").replace("/", ":") + " " + ctx.getArguments()[2] + " = " + result);
            ctx.getChannel().sendMessage(calculated.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
            Manager.getUtilities().error(ctx.getChannel(), "calculate", "\uD83E\uDDEE", "Error occurred", e.getMessage(), ctx.getAuthor().getEffectiveAvatarUrl());
        }
    }
}