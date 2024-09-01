package fr.swansky.core.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SimpleSubCommand implements BaseCommand {
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

    @Override
    public void addOption(Class<?> type, Param param, OptionType optionType, boolean autoComplete) {
        options.add(new OptionData(optionType, param.name().toLowerCase(), param.description(), param.required(), autoComplete));
    }

    @Override
    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
