## Simple Slash Command System (SSCS)

SSCS is a library designed to make it easy to implement slash commands on your [JDA](https://github.com/discord-jda/JDA)
Discord bot. This library uses annotations and auto-injection of parameters. The annotated methods are automatically
called by the core system when a command is invoked.

### Features

- ✅ **Supports default types**
- ✅ **Supports custom types**
- ✅ **Supports auto-completion**
- ✅ **Supports commands with and without arguments**
- ❌ **Supports ContextMenu**: *In Progress* ⏳
- ❌ **Supports annotation for provided param**: *In Progress* ⏳
- ❌ **Supports auto-completion for built-in param**: *In Progress* ⏳
- ❌ **Supports dynamic permission**: *In Progress* ⏳



### Requirements

To use this project, ensure you have the following dependencies and tools installed:

- **Java 21**
- **JDA 5.0.0**
- **Gradle 8**

### Initialization

```java
public class Main {
    public static void main(String[] args) {
        CommandManager commandManager = CommandManagerBuilder.create()
                .addCommands(new MyCommand())
                .addProvider(InstanceToProvide.class, new InstanceProvider())
                .build();

        JDA jda = JDABuilder.createDefault("token")
                .addEventListeners(commandManager)
                .build();

        commandManager.registerCommands(jda);
    }
}
```

### Creating a command

To create a command you need to create a class and annotate it with `@Command`.
If you want to create a simple command like: `/myCommand <randomParam>` you need to annotate a method
with `@MainCommand <param>`, add a method annotated with `@MainCommand`:

```java

@MainCommand
public void onCommand(SlashCommandInteractionEvent event, @Param(name = "randomParam") String randomParam) {
    event.reply("Hello World!");
}
```

If you want to create a subcommand like: `/myCommand subCommand <user> <count> <message> <channel>` you need to annotate
a method with `@SubCommand` and add a method annotated with `@SubCommand`:

```java

@SubCommand(name = "subCommand", description = "Say somthing x times to a user")
public void sayTo(SlashCommandInteractionEvent event,
                  @Param(name = "user") User user,
                  @Param(name = "count") int count,
                  @Param(name = "message") String message,
                  @Param(name = "channel") TextChannel channel) {
    for (int i = 0; i < count; i++) {
        channel.sendMessage(user.getAsMention() + " " + message).queue();
    }
}
```

### Annotations

#### @Command

Place this annotation on the class that contains the command methods.

Parameters:

- `name` - The name of the command
- `description` - The description of the command
- `guildOnly` - If the command should only be available in guilds
- `nsfw` - If the command should only be available in nsfw channels
- `permissions` - The permissions required to use the command (only for guilds commands)

#### @MainCommand

Place this annotation on method that should be the main command (when you don't have subcommands).
This annotation can be optional if you have subcommands.

#### @SubCommand

Place this annotation on method that should be a subcommand.

Parameters:

- `name` - The name of the subcommand
- `description` - The description of the subcommand

#### @Param

Place this annotation on method parameters that should be provided by the user.

Parameters:

- `name` - The name of the parameter (this name will be displayed on the discord command)
- `description` - The description of the parameter
- `required` - If the parameter is required (default: true)
- `defaultValue` - The default value of the parameter (default: "")

#### Example

```java

@Command(name = "myCommand", description = "Command example", guildOnly = true, nsfw = true, permissions = {Permission.ADMINISTRATOR})
public class MyCommand {

    @MainCommand
    public void onCommand(SlashCommandInteractionEvent event) {
        event.reply("Hello World!");
    }

    @SubCommand(name = "subCommand", description = "Say somthing x times to a user")
    public void sayTo(SlashCommandInteractionEvent event,
                      @Param(name = "user") User user,
                      @Param(name = "count") int count,
                      @Param(name = "message") String message,
                      @Param(name = "channel") TextChannel channel,
                      InstanceToProvide instanceToProvide) {
        for (int i = 0; i < count; i++) {
            channel.sendMessage(user.getAsMention() + " " + message).queue();
        }
    }
}
```

### Providers

If you want to pass a custom type as a parameter to a method/command, whether it needs to be prompted from the user,
this is possible using `ParamProvider`.

#### Example: DateProvider (built-in provider)

```java
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateProvider implements ParamProvider<Date> {
    public static final DateProvider INSTANCE = new DateProvider("dd/MM/yyyy");

    private final DateFormat dateFormat;

    public DateProvider(String format) {
        dateFormat = new SimpleDateFormat(format);
    }

    @Override
    public Date get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        String stringDate = mapping.getAsString();
        try {
            return dateFormat.parse(stringDate);
        } catch (ParseException e) {
            throw new ParamException("Wrong date value");
        }
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.STRING;
    }
}
```

### Usage Example

```java

@SubCommand(name = "date", description = "enter date")
public void enterDate(SlashCommandInteractionEvent event, @Param(name = "date") Date startDate) {
    // do something...
}
```

### Auto-Completion

SSCS supports auto-completion for custom types, allowing you to provide suggestions to the user as they type. This is
handled using the `onAutoComplete` method within a `ParamProvider`.

#### Example: CustomObjectProvider

```java
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.List;

public class CustomObjectProvider implements ParamProvider<MyCustomObject> {
    private final List<MyCustomObject> availableObjects;

    public CustomObjectProvider(List<MyCustomObject> availableObjects) {
        this.availableObjects = availableObjects;
    }

    @Override
    public MyCustomObject get(OptionMapping optionMapping, SlashCommandInteractionEvent event) throws ParamException {
        String id = optionMapping.getAsString();
        return availableObjects.stream()
                .filter(obj -> obj.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ParamException("Invalid ID: " + id));
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.STRING;
    }

    @Override
    public void onAutoComplete(CommandAutoCompleteInteractionEvent event) {
        String input = event.getFocusedOption().getValue();
        List<String> matchingIds = availableObjects.stream()
                .map(MyCustomObject::getId)
                .filter(id -> id.startsWith(input))
                .toList();
        event.replyChoiceStrings(matchingIds).queue();
    }

    @Override
    public boolean isAutoComplete() {
        return true;
    }
}
```

#### Custom Object Definition

```java
public class MyCustomObject {
    private final String id;
    private final String name;

    public MyCustomObject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
```

#### Usage Example

```java

@SubCommand(name = "select-object", description = "Select a custom object by ID")
public void selectCustomObject(SlashCommandInteractionEvent event, @Param(name = "object_id") MyCustomObject selectedObject) {
    // Handle the selected object...
}
```

### Project Purpose and Updates

This project is developed primarily for personal and professional use. As such, updates and new features will be driven
by the needs and requirements that arise in these contexts. While community feedback and contributions are welcome, the
direction of the project will prioritize the features and improvements that align with my personal and professional use
cases.


