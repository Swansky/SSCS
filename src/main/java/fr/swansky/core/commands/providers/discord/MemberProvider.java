package fr.swansky.core.commands.providers.discord;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class MemberProvider implements ParamProvider<Member> {
    @Override
    public Member get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        return mapping.getAsMember();
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.NUMBER;
    }
}
