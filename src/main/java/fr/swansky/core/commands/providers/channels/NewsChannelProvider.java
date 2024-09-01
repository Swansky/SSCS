package fr.swansky.core.commands.providers.channels;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class NewsChannelProvider implements ParamProvider<NewsChannel> {
    @Override
    public NewsChannel get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsChannel().asNewsChannel();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.CHANNEL;
    }
}
