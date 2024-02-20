package commands.command;

import fr.swansky.core.commands.Command;
import fr.swansky.core.commands.MainCommand;

import java.io.File;

@Command(name = "provider", description = "Provider Command")
public class ProviderCommand {

    @MainCommand
    public void helloWorld(File file) {
    }
}
