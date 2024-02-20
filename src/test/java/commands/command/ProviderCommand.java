package commands.command;

import fr.swansky.core.commands.Command;
import fr.swansky.core.commands.CommandManager;
import fr.swansky.core.commands.MainCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.File;

@Command(name = "provider", description = "Provider Command")
public class ProviderCommand {

    @MainCommand
    public void helloWorld(File file, CommandManager commandManager, SlashCommandInteractionEvent event) {
    }
}
