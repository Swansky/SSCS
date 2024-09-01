package fr.swansky.core.commands.providers.channels;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class TextChannelProvider implements ParamProvider<TextChannel> {
    @Override
    public TextChannel get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsChannel().asTextChannel();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.CHANNEL;
    }
}
