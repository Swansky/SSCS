package commands.command;


import fr.swansky.core.commands.Command;
import fr.swansky.core.commands.MainCommand;
import fr.swansky.core.commands.SubCommand;

@Command(name = "badParam", description = "Bad Param Command")
public class BadParamCommand {

    @MainCommand
    public void helloWorld() {
    }

    @SubCommand(name = "sub", description = "Hello World Sub Command")
    public void helloWorldSub(int count) {
    }
}
