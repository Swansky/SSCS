package fr.swansky.core.commands;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SimpleSubCommand {
    private final List<OptionData> options = new ArrayList<>();

    private final String name;

    private final String description;

    private Method method;

    public SimpleSubCommand(String name, String description) {
        this.name = name.toLowerCase();
        this.description = description;
    }

    public SubcommandData createCommandData() {
        return new SubcommandData(getName(), getDescription())
                .addOptions(getOptions());

    }

    private List<OptionData> getOptions() {
        return options;
    }

    private String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void addOption(Class<?> type, Param param) {
        options.add(new OptionData(CommandUtils.getOptionType(type), param.name().toLowerCase(), param.description(), param.required()));
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
