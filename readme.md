# Simple Slash Command System

This is a simple command system for discord slash command for [JDA](https://github.com/discord-jda/JDA) libs. It is
designed to be easy to use and easy to understand. It is also designed to be easy to expand upon.

## Features

- [Possible to provide instances to commands ](#providers)
- _Custom converters_ (not implemented yet)

## Usage

### Create command manager and register it

```java
public class Main {
    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault("token")
                .addEventListeners(CommandManagerBuilder.create()
                        .addCommands(new MyCommand())
                        .addProvider(InstanceToProvide.class, new InstanceToProvide())
                        .build())
                .build();
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

### Providers

If you need to provide an instance to a command you can use the `addProvider` method in the `CommandManagerBuilder`
class.
And add a parameter to the command method with the type of the instance you want to provide.
#### By default

By default, the command manager can provide the following instances:

- CommandManager
- SlashCommandInteractionEvent

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
                      InstanceToProvide instanceToProvide) { // instanceToProvide will be provided if it is registered in the CommandManager (addProvider)
        for (int i = 0; i < count; i++) {
            channel.sendMessage(user.getAsMention() + " " + message).queue();
        }
    }
}
```
