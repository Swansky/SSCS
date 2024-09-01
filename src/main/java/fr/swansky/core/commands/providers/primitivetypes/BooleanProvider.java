package fr.swansky.core.commands.providers.primitivetypes;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class BooleanProvider implements ParamProvider<Boolean> {
    public static final BooleanProvider INSTANCE = new BooleanProvider();

    private BooleanProvider() {
    }

    @Override
    public Boolean get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsBoolean();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.BOOLEAN;
    }
}
