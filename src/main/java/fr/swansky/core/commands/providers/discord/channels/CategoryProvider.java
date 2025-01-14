package fr.swansky.core.commands.providers.discord.channels;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CategoryProvider implements ParamProvider<Category> {
    @Override
    public Category get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsChannel().asCategory();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.CHANNEL;
    }
}
