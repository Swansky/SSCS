package commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.junit.jupiter.api.Test;
import fr.swansky.core.commands.CommandUtils;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandUtilsTest {

    @Test
    void getOptionType() {
        assertEquals(OptionType.BOOLEAN, CommandUtils.getOptionType(boolean.class));
        assertEquals(OptionType.BOOLEAN, CommandUtils.getOptionType(Boolean.class));

        assertEquals(OptionType.STRING, CommandUtils.getOptionType(String.class));

        assertEquals(OptionType.INTEGER, CommandUtils.getOptionType(int.class));
        assertEquals(OptionType.INTEGER, CommandUtils.getOptionType(Integer.class));
        assertEquals(OptionType.INTEGER, CommandUtils.getOptionType(long.class));
        assertEquals(OptionType.INTEGER, CommandUtils.getOptionType(Long.class));
        assertEquals(OptionType.INTEGER, CommandUtils.getOptionType(short.class));
        assertEquals(OptionType.INTEGER, CommandUtils.getOptionType(Short.class));
        assertEquals(OptionType.INTEGER, CommandUtils.getOptionType(byte.class));
        assertEquals(OptionType.INTEGER, CommandUtils.getOptionType(Byte.class));

        assertEquals(OptionType.NUMBER, CommandUtils.getOptionType(double.class));
        assertEquals(OptionType.NUMBER, CommandUtils.getOptionType(Double.class));
        assertEquals(OptionType.NUMBER, CommandUtils.getOptionType(float.class));
        assertEquals(OptionType.NUMBER, CommandUtils.getOptionType(Float.class));

        assertEquals(OptionType.USER, CommandUtils.getOptionType(User.class));
        assertEquals(OptionType.USER, CommandUtils.getOptionType(Member.class));

        assertEquals(OptionType.CHANNEL, CommandUtils.getOptionType(TextChannel.class));
        assertEquals(OptionType.CHANNEL, CommandUtils.getOptionType(VoiceChannel.class));
        assertEquals(OptionType.CHANNEL, CommandUtils.getOptionType(ForumChannel.class));
        assertEquals(OptionType.CHANNEL, CommandUtils.getOptionType(Category.class));

        assertEquals(OptionType.ROLE, CommandUtils.getOptionType(Role.class));


        assertThrows(IllegalArgumentException.class, () -> CommandUtils.getOptionType(InputStream.class));

    }
}
