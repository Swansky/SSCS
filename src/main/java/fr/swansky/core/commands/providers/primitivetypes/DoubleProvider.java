package fr.swansky.core.commands.providers.primitivetypes;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class DoubleProvider implements ParamProvider<Double> {
    public static final DoubleProvider INSTANCE = new DoubleProvider();

    private DoubleProvider() {
    }

    @Override
    public Double get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsDouble();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.NUMBER;
    }
}
