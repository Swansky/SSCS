package fr.swansky.core.commands.providers.channels;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class ThreadChannelProvider implements ParamProvider<ThreadChannel> {
    @Override
    public ThreadChannel get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsChannel().asThreadChannel();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.CHANNEL;
    }
}
