package fr.swansky.core.commands.providers.primitivetypes;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class StringProvider implements ParamProvider<String> {
    public static final StringProvider INSTANCE = new StringProvider();

    private StringProvider() {
    }

    @Override
    public String get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsString();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.STRING;
    }
}
