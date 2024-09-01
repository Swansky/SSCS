package fr.swansky.core.commands.providers.discord;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class RoleProvider implements ParamProvider<Role> {
    @Override
    public Role get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsRole();

    }

    @Override
    public OptionType getOptionType() {
        return OptionType.ROLE;
    }
}
