package fr.swansky.core.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import fr.swansky.core.commands.exceptions.CommandException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandManager extends ListenerAdapter {

    private static final List<Class<?>> INTERNAL_SUPPORTED_CLASS = List.of(SlashCommandInteractionEvent.class, CommandManager.class);
    private static final Logger LOGGER = Logger.getLogger("CommandManager");

    private final Map<String, SimpleCommand> commandDataMap = new HashMap<>();
    private final Map<Class<?>, Object> providers = new HashMap<>();

    public CommandManager(List<Object> commands, Map<Class<?>, Object> providers) {
        this.providers.putAll(providers);
        for (Object command : commands) {
            addCommandToRegister(command);
        }
    }

    public void addCommandToRegister(Object obj) {
        if (obj.getClass().isAnnotationPresent(Command.class)) {
            Command annotation = obj.getClass().getAnnotation(Command.class);
            SimpleCommand simpleCommand = new SimpleCommand(annotation.name(), annotation.description(), obj);
            simpleCommand.setGuildOnly(annotation.guildOnly());
            simpleCommand.setNsfw(annotation.nsfw());
            simpleCommand.setPerms(Arrays.asList(annotation.permissions()));

            for (Method method : obj.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(MainCommand.class)) {
                    simpleCommand.setMainCommand(method);
                } else if (method.isAnnotationPresent(SubCommand.class)) {
                    simpleCommand.addSubCommand(createSubCommand(method));
                }
            }
            if (simpleCommand.getMainCommand() == null && simpleCommand.getSubCommands().isEmpty()) {
                throw new CommandException("No @MainCommand or @SubCommand annotation found");
            }

            addCommandToRegister(simpleCommand);
        }
    }

    private SimpleSubCommand createSubCommand(Method method) {
        SubCommand subCommand = method.getAnnotation(SubCommand.class);
        SimpleSubCommand simpleSubCommand = new SimpleSubCommand(subCommand.name(), subCommand.description());
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Param.class)) {
                Param param = parameter.getAnnotation(Param.class);
                simpleSubCommand.addOption(parameter.getType(), param);
            } else {
                if (!providers.containsKey(parameter.getType()) && !INTERNAL_SUPPORTED_CLASS.contains(parameter.getType())) {
                    throw new IllegalArgumentException("No provider found for " + parameter.getType().getName());
                }
            }
        }
        simpleSubCommand.setMethod(method);
        return simpleSubCommand;
    }

    private void addCommandToRegister(SimpleCommand simpleCommand) {
        if (commandDataMap.containsKey(simpleCommand.getName()))
            throw new IllegalArgumentException("Command already registered");
        commandDataMap.put(simpleCommand.getName(), simpleCommand);
    }

    public void registerCommands(JDA jda) {
        jda.updateCommands()
                .addCommands(commandDataMap.values()
                        .stream()
                        .map(SimpleCommand::createCommandData)
                        .toList())
                .queue();
    }

    private void onSlashCommand(SlashCommandInteractionEvent event) {
        if (!commandDataMap.containsKey(event.getName())) {
            event.reply("Command not found").setEphemeral(true).queue();
            return;
        }
        SimpleCommand simpleCommand = commandDataMap.get(event.getName());
        if (event.isFromGuild() && !memberCanExecuteCommand(Objects.requireNonNull(event.getMember()), simpleCommand)) {
            event.reply("You don't have the permission to execute this command").setEphemeral(true).queue();
            return;
        }

        String[] command = event.getFullCommandName().split(" ");
        try {
            if (command.length > 1) {
                Optional<SimpleSubCommand> subCommandOptional = simpleCommand.findSubCommand(command[1]);
                if (subCommandOptional.isEmpty()) {
                    event.getHook().sendMessage("SubCommand not found").setEphemeral(true).queue();
                    return;
                }
                SimpleSubCommand subCommand = subCommandOptional.get();
                subCommand.getMethod().invoke(simpleCommand.getInstance(), createParamsList(subCommand.getMethod(), event));
            } else {
                simpleCommand.getMainCommand().invoke(simpleCommand.getInstance(), createParamsList(simpleCommand.getMainCommand(), event));
            }
        } catch (Exception e) {
            event.getHook().sendMessage("An error occurred while executing the command").queue();
            LOGGER.log(Level.SEVERE, "Error while executing command %s".formatted(simpleCommand.getName()), e);
        }
    }

    private Object[] createParamsList(Method method, SlashCommandInteractionEvent event) {
        Object[] params = new Object[method.getParameterCount()];
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];
            if (parameter.isAnnotationPresent(Param.class)) {
                Param annotation = parameter.getAnnotation(Param.class);
                OptionMapping option = event.getOption(annotation.name());
                if (option == null) {
                    throw new IllegalArgumentException("Option not found");
                }
                params[i] = CommandUtils.getOptionValue(option, parameter.getType());
            } else {
                addInstanceOfNoParam(event, parameter, params, i);
            }
        }
        return params;
    }

    private void addInstanceOfNoParam(SlashCommandInteractionEvent event, Parameter parameter, Object[] params, int i) {
        if (INTERNAL_SUPPORTED_CLASS.contains(parameter.getType())) {
            if (parameter.getType().equals(SlashCommandInteractionEvent.class)) {
                params[i] = event;
            } else if (parameter.getType().equals(CommandManager.class)) {
                params[i] = this;
            } else {
                throw new IllegalArgumentException("Unsupported type " + parameter.getType().getName());
            }
        } else if (providers.containsKey(parameter.getType())) {
            params[i] = providers.get(parameter.getType());
        } else {
            throw new IllegalArgumentException("No provider found for " + parameter.getType().getName());
        }
    }

    private boolean memberCanExecuteCommand(Member member, SimpleCommand simpleCommand) {
        assert member != null;
        return member.hasPermission(simpleCommand.getPerms());
    }

    public Map<String, SimpleCommand> getCommands() {
        return commandDataMap;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        onSlashCommand(event);
    }


    @Override
    public void onReady(ReadyEvent event) {
        registerCommands(event.getJDA());
    }
}
