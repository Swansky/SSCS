package fr.swansky.core.commands.providers.common;

import fr.swansky.core.commands.providers.ParamException;
import fr.swansky.core.commands.providers.ParamProvider;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateProvider implements ParamProvider<Date> {
    public static final DateProvider INSTANCE = new DateProvider("dd/MM/yyyy");

    private final DateFormat dateFormat;

    public DateProvider(String format) {
        dateFormat = new SimpleDateFormat(format);
    }


    @Override
    public Date get(OptionMapping mapping, SlashCommandInteractionEvent event) throws ParamException {
        String stringDate = mapping.getAsString();
        try {
            return dateFormat.parse(stringDate);
        } catch (ParseException e) {
            throw new ParamException("Wrong date value");
        }
    }

    @Override
    public OptionType getOptionType() {
        return OptionType.STRING;
    }
}
