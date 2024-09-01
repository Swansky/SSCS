package fr.swansky.core.commands.providers.primitivetypes;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class IntegerProvider implements ParamProvider<Integer> {
    public static final IntegerProvider INSTANCE = new IntegerProvider();

    private IntegerProvider() {
    }

    @Override
    public Integer get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsInt();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.INTEGER;
    }
}
