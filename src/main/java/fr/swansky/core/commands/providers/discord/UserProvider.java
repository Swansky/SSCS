package fr.swansky.core.commands.providers.discord;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class UserProvider implements ParamProvider<User> {
    @Override
    public User get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsUser();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.USER;
    }
}
