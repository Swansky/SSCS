package fr.swansky.core.commands.providers.primitivetypes;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class LongProvider implements ParamProvider<Long> {
    public static final LongProvider INSTANCE = new LongProvider();

    private LongProvider() {
    }

    @Override
    public Long get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsLong();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.NUMBER;
    }
}
