package fr.swansky.core.commands;

import fr.swansky.core.commands.providers.ParamProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to build the CommandManager, it's a builder pattern
 */
public class CommandManagerBuilder {

    private final List<Object> commands = new ArrayList<>();

    private final Map<Class<?>, ParamProvider<?>> providers = new HashMap<>();

    private final List<Middleware> middlewares = new ArrayList<>();

    private CommandManagerBuilder() {
    }

    public static CommandManagerBuilder create() {
        return new CommandManagerBuilder();
    }

    /**
     * Add one command or multiple commands to the register, this instance should have the annotation @Command with method also annotated with @MainCommand or @SubCommand
     *
     * @param commands the instance of each commands
     */
    public CommandManagerBuilder addCommands(Object... commands) {
        this.commands.addAll(List.of(commands));
        return this;
    }

    /**
     * This method is used to add a provider for a specific class, this instance will be call to provide param to the command parameter
     *
     * @param providerClass the class of the provider
     * @param provider      the provider instance with the logic to provide the param
     * @param <T>           the type of the provider
     * @return the builder
     */
    public <T> CommandManagerBuilder addProvider(Class<T> providerClass, ParamProvider<T> provider) {
        this.providers.put(providerClass, provider);
        return this;
    }

    /**
     * This method is used to add a middleware(s) to the command manager, each middleware will be called before the command execution or autocomplete event
     *
     * @param middlewares the middleware(s) to add
     * @return the builder
     */
    public CommandManagerBuilder addMiddlewares(Middleware... middlewares) {
        this.middlewares.addAll(List.of(middlewares));
        return this;
    }


    public CommandManager build() {
        return new CommandManager(commands, providers,middlewares);
    }
}
