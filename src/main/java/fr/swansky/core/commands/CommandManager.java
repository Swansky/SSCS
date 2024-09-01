package fr.swansky.core.commands;

import fr.swansky.core.commands.exceptions.CommandException;
import fr.swansky.core.commands.providers.ParamProvider;
import fr.swansky.core.commands.providers.ProviderBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.AutoCompleteQuery;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;


public class CommandManager extends ListenerAdapter {

    private static final Logger LOGGER = JDALogger.getLog(CommandManager.class);

    private final Map<String, SimpleCommand> commandDataMap = new HashMap<>();
    private final Map<Class<?>, ParamProvider<?>> providers = new HashMap<>();

    public CommandManager(List<Object> commands, Map<Class<?>, ParamProvider<?>> providers) {
        this.providers.putAll(ProviderBuilder.getBuiltinProviders());
        this.providers.putAll(providers);
        this.providers.put(CommandManager.class, ParamProvider.staticProvider(this));
        this.providers.put(SlashCommandInteractionEvent.class, (arg, event) -> event);


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
                    createOptions(simpleCommand, method);
                } else if (method.isAnnotationPresent(SubCommand.class)) {
                    SubCommand subCommand = method.getAnnotation(SubCommand.class);
                    SimpleSubCommand simpleSubCommand = new SimpleSubCommand(subCommand.name(), subCommand.description());
                    createOptions(simpleSubCommand, method);
                    simpleCommand.addSubCommand(simpleSubCommand);
                }
            }
            if (simpleCommand.getMainCommand() == null && simpleCommand.getSubCommands().isEmpty()) {
                throw new CommandException("No @MainCommand or @SubCommand annotation found");
            }

            addCommandToRegister(simpleCommand);
        } else {
            throw new IllegalArgumentException("This instance don't have annotation @Command");
        }
    }


    private void createOptions(BaseCommand command, Method method) {
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Param.class)) {
                Param param = parameter.getAnnotation(Param.class);
                ParamProvider<?> paramProvider = providers.get(parameter.getType());
                if (paramProvider == null) {
                    throw new IllegalArgumentException("method %s have a parameter(%s) without valid provider"
                            .formatted(method.getName(), param.name()));
                }
                command.addOption(parameter.getType(), param, paramProvider.getOptionType(),paramProvider.isAutoComplete());

            } else {
                if (!providers.containsKey(parameter.getType())) {
                    throw new IllegalArgumentException("No provider found for " + parameter.getType().getName());
                }
                ParamProvider<?> paramProvider = providers.get(parameter.getType());
                if (paramProvider.getOptionType() != OptionType.UNKNOWN) {
                    throw new IllegalArgumentException("Provider for param %s in method %s is for a discord param"
                            .formatted(parameter.getType(), parameter.getName()));
                }
            }
        }
        command.setMethod(method);
    }

    private void addCommandToRegister(SimpleCommand simpleCommand) {
        if (commandDataMap.containsKey(simpleCommand.getName()))
            throw new IllegalArgumentException("Command already registered");
        commandDataMap.put(simpleCommand.getName(), simpleCommand);
    }

    public void registerCommands(JDA jda) {
        LOGGER.info("Commands registered");

        List<SlashCommandData> list = commandDataMap.values().stream()
                .map(SimpleCommand::createCommandData)
                .toList();

        jda.updateCommands()
                .addCommands(list)
                .queue(commands -> {
                    commands.forEach(command -> {
                        List<String> subcommandName = command.getSubcommands()
                                .stream()
                                .map(net.dv8tion.jda.api.interactions.commands.Command.Subcommand::getName)
                                .toList();
                        String subCommand = String.format("(%s)",
                                subcommandName.stream().reduce((s1, s2) -> s1 + ", " + s2).orElse("")
                        );
                        if (subcommandName.isEmpty()) {
                            subCommand = "";
                        }

                        LOGGER.info("Command '%s' registered %s ".formatted(command.getName(), subCommand));
                    });
                });

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
            LOGGER.error("Error while executing command %s".formatted(simpleCommand.getName()), e);
        }
    }

    private void onCommandCompletion(CommandAutoCompleteInteractionEvent event) {
        if (!commandDataMap.containsKey(event.getName())) {
            return;
        }
        SimpleCommand simpleCommand = commandDataMap.get(event.getName());
        if (event.isFromGuild() && !memberCanExecuteCommand(Objects.requireNonNull(event.getMember()), simpleCommand)) {
            return;
        }
        String[] command = event.getFullCommandName().split(" ");
        try {
            if (command.length > 1) {
                Optional<SimpleSubCommand> subCommandOptional = simpleCommand.findSubCommand(command[1]);
                if (subCommandOptional.isEmpty()) {
                    return;
                }
                SimpleSubCommand subCommand = subCommandOptional.get();
                Method method = subCommand.getMethod();
                sendAutoCompleteToProvider(event, method);

            } else {
                Method method = simpleCommand.getMainCommand();
                sendAutoCompleteToProvider(event, method);
            }
        } catch (Exception e) {
            LOGGER.error("Error while executing command %s".formatted(simpleCommand.getName()), e);
        }
    }

    private void sendAutoCompleteToProvider(CommandAutoCompleteInteractionEvent event, Method method) {
        AutoCompleteQuery focusedOption = event.getFocusedOption();
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Param.class)) {
                Param paramAnnotation = parameter.getAnnotation(Param.class);
                if (paramAnnotation.name().equals(focusedOption.getName())) {
                    ParamProvider<?> paramProvider = providers.get(parameter.getType());
                    if (paramProvider != null) {
                        paramProvider.onAutoComplete(event);
                        return;
                    }
                }
            }
        }
    }

    private Object[] createParamsList(Method method, SlashCommandInteractionEvent event) {
        Object[] params = new Object[method.getParameterCount()];
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];
            if (parameter.isAnnotationPresent(Param.class)) {
                Param annotation = parameter.getAnnotation(Param.class);
                OptionMapping option = event.getOption(annotation.name().toLowerCase());
                if (option == null) {
                    throw new IllegalArgumentException("Option " + annotation.name() + " not found");
                }
                if (providers.containsKey(parameter.getType())) {
                    ParamProvider<?> paramProvider = providers.get(parameter.getType());
                    params[i] = paramProvider.get(option, event);
                } else {
                    throw new IllegalArgumentException("Impossible to find provider for parameter " + parameter.getName());
                }
            } else {
                if (providers.containsKey(parameter.getType())) {
                    ParamProvider<?> paramProvider = providers.get(parameter.getType());
                    if (paramProvider.getOptionType() != OptionType.UNKNOWN) {
                        throw new IllegalArgumentException("Provider for param %s in method %s is for a discord param"
                                .formatted(parameter.getType(), parameter.getName()));
                    }
                    params[i] = paramProvider.get(null, event);
                } else {
                    throw new IllegalArgumentException("Impossible to find provider for parameter " + parameter.getName());
                }
            }

        }
        return params;
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
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        onCommandCompletion(event);
    }

}
