package fr.swansky.core.commands;

import fr.swansky.core.commands.providers.ParamProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManagerBuilder {

    private final List<Object> commands = new ArrayList<>();

    private final Map<Class<?>, ParamProvider<?>> providers = new HashMap<>();

    private CommandManagerBuilder() {
    }

    public static CommandManagerBuilder create() {
        return new CommandManagerBuilder();
    }

    public CommandManagerBuilder addCommands(Object... commands) {
        this.commands.addAll(List.of(commands));
        return this;
    }

    public <T> CommandManagerBuilder addProvider(Class<T> providerClass, ParamProvider<T> provider) {
        this.providers.put(providerClass, provider);
        return this;
    }


    public CommandManager build() {
        return new CommandManager(commands, providers);
    }
}
