package fr.swansky.core.commands.providers;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public interface ParamProvider<T> {


    T get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException;

    default OptionType getOptionType() {
        return OptionType.UNKNOWN;
    }

    static <T> ParamProvider<T> staticProvider(T value) {
        return (mapping, event) -> value;
    }

    static ParamProvider<SlashCommandInteractionEvent> eventProvider() {
        return (mapping, event) -> event;
    }

    default void onAutoComplete(CommandAutoCompleteInteractionEvent event) {
    }

    default boolean isAutoComplete() {
        return false;
    }

}
