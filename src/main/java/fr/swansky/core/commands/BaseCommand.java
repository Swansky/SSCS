package fr.swansky.core.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.reflect.Method;

public interface BaseCommand {
    void addOption(Class<?> type, Param param, OptionType optionType,boolean autoComplete);

    void setMethod(Method method);
}
