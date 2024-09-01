package fr.swansky.core.commands.providers.channels;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class GuildMessageChannelProvider implements ParamProvider<GuildMessageChannel> {
    @Override
    public GuildMessageChannel get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsChannel().asGuildMessageChannel();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.CHANNEL;
    }
}
