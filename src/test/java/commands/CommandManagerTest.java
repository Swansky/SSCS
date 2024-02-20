package commands;

import commands.command.BadCommand;
import commands.command.BadParamCommand;
import commands.command.ProviderCommand;
import commands.command.ValidCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import fr.swansky.core.commands.CommandManager;
import fr.swansky.core.commands.CommandManagerBuilder;
import fr.swansky.core.commands.SimpleCommand;
import fr.swansky.core.commands.exceptions.CommandException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class CommandManagerTest {


    @Test
    @Order(1)
    void addCommandToRegister() {
        ValidCommand validCommand = new ValidCommand();
        CommandManager validCM = CommandManagerBuilder.create().addCommands(validCommand).build();
        assertEquals(1, validCM.getCommands().size());
        assertThrows(IllegalArgumentException.class, () -> validCM.addCommandToRegister(validCommand));

        BadCommand badCommand = new BadCommand();
        assertThrows(CommandException.class, () -> validCM.addCommandToRegister(badCommand));

        BadParamCommand badParamCommand = new BadParamCommand();
        assertThrows(IllegalArgumentException.class, () -> validCM.addCommandToRegister(badParamCommand));

        SimpleCommand simpleCommand = validCM.getCommands().get("valid");
        assertNotNull(simpleCommand);

        assertEquals(1, simpleCommand.getSubCommands().size());
        SlashCommandData commandData = simpleCommand.createCommandData();
        assertNotNull(commandData);
        assertEquals(1, commandData.getSubcommands().size());

        SubcommandData subCommand = commandData.getSubcommands().get(0);
        assertEquals(3, subCommand.getOptions().size());

        assertEquals("count", subCommand.getOptions().get(0).getName());
        assertEquals(OptionType.INTEGER, subCommand.getOptions().get(0).getType());

        assertEquals("name", subCommand.getOptions().get(1).getName());
        assertEquals(OptionType.STRING, subCommand.getOptions().get(1).getType());

        assertEquals("channel", subCommand.getOptions().get(2).getName());
        assertEquals(OptionType.CHANNEL, subCommand.getOptions().get(2).getType());

        assertFalse(simpleCommand.isGuildOnly());
        assertTrue(simpleCommand.isNsfw());

        for (Permission perm : simpleCommand.getPerms()) {
            assertEquals(Permission.ADMINISTRATOR, perm);
        }


    }


    @Test
    @Order(2)
    void provider() {
        File file = new File("test");
        CommandManager validCM = CommandManagerBuilder.create().addCommands(new ProviderCommand()).addProvider(File.class, file).build();
        assertEquals(1, validCM.getCommands().size());
    }

}
