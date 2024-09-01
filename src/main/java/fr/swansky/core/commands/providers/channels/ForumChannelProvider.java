package fr.swansky.core.commands.providers.channels;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class ForumChannelProvider implements ParamProvider<ForumChannel> {
    @Override
    public ForumChannel get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsChannel().asForumChannel();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.CHANNEL;
    }
}
