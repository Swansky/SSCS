package commands.command;

import fr.swansky.core.commands.Command;
import fr.swansky.core.commands.MainCommand;
import fr.swansky.core.commands.Param;
import fr.swansky.core.commands.SubCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Command(name = "valid", description = "Valid Command", guildOnly = false, nsfw = true, permissions = {Permission.ADMINISTRATOR})
public class ValidCommand {

    @MainCommand
    public void helloWorld(SlashCommandInteractionEvent event) {
        event.getHook().sendMessage("oui").queue();
    }

    @SubCommand(name = "sub", description = "Hello World Sub Command")
    public void helloWorldSub(SlashCommandInteractionEvent event, @Param(name = "count") int count, @Param(name = "name") String name, @Param(name = "channel") TextChannel channel) {
        if (event == null) throw new IllegalArgumentException("SlashCommandInteractionEvent is null");
        event.getHook().sendMessage("Hello World Sub: " + count + " " + name).queue();
    }
}
