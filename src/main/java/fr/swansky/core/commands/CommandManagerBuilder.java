package fr.swansky.core.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManagerBuilder {

    private final List<Object> commands = new ArrayList<>();

    private final Map<Class<?>, Object> providers = new HashMap<>();

    private CommandManagerBuilder() {
    }

    public static CommandManagerBuilder create() {
        return new CommandManagerBuilder();
    }

    public CommandManagerBuilder addCommands(Object... commands) {
        this.commands.addAll(List.of(commands));
        return this;
    }

    public CommandManagerBuilder addProvider(Class<?> providerClass, Object providers) {
        this.providers.put(providerClass, providers);
        return this;
    }


    public CommandManager build() {
        return new CommandManager(commands, providers);
    }
}
