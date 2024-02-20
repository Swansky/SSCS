package fr.swansky.core.commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandUtils {
    private CommandUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static OptionType getOptionType(Class<?> type) {
        if (type.isAssignableFrom(String.class)) {
            return OptionType.STRING;
        } else if (type.isAssignableFrom(Long.class) || type.isAssignableFrom(long.class) ||
                type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class) ||
                type.isAssignableFrom(Short.class) || type.isAssignableFrom(short.class) ||
                type.isAssignableFrom(Byte.class) || type.isAssignableFrom(byte.class)) {
            return OptionType.INTEGER;
        } else if (type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class) ||
                type.isAssignableFrom(Float.class) || type.isAssignableFrom(float.class)) {
            return OptionType.NUMBER;
        } else if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)) {
            return OptionType.BOOLEAN;
        } else if (type.isAssignableFrom(User.class) || type.isAssignableFrom(Member.class)) {
            return OptionType.USER;
        } else if (type.isAssignableFrom(TextChannel.class) || type.isAssignableFrom(VoiceChannel.class) ||
                type.isAssignableFrom(ForumChannel.class) || type.isAssignableFrom(ThreadChannel.class) ||
                type.isAssignableFrom(MediaChannel.class) || type.isAssignableFrom(Category.class) ||
                type.isAssignableFrom(GuildMessageChannel.class) || type.isAssignableFrom(NewsChannel.class)) {
            return OptionType.CHANNEL;
        } else if (type.isAssignableFrom(Role.class)) {
            return OptionType.ROLE;
        } else {
            throw new IllegalArgumentException("Type " + type + " not supported");
        }
    }


    public static Object getOptionValue(OptionMapping option, Class<?> type) {
        if (type.equals(String.class)) {
            return option.getAsString();
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return option.getAsInt();
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return option.getAsLong();
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return option.getAsDouble();
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            return (float) option.getAsDouble();
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return option.getAsBoolean();
        } else if (type.equals(Channel.class)) {
            return option.getAsChannel();
        } else if (type.equals(TextChannel.class)) {
            return option.getAsChannel().asTextChannel();
        } else if (type.equals(VoiceChannel.class)) {
            return option.getAsChannel().asAudioChannel();
        } else if (type.equals(ForumChannel.class)) {
            return option.getAsChannel().asForumChannel();
        } else if (type.equals(ThreadChannel.class)) {
            return option.getAsChannel().asThreadChannel();
        } else if (type.equals(MediaChannel.class)) {
            return option.getAsChannel().asMediaChannel();
        } else if (type.equals(Category.class)) {
            return option.getAsChannel().asCategory();
        } else if (type.equals(GuildMessageChannel.class)) {
            return option.getAsChannel().asGuildMessageChannel();
        } else if (type.equals(NewsChannel.class)) {
            return option.getAsChannel().asNewsChannel();
        } else if (type.equals(User.class)) {
            return option.getAsUser();
        } else if (type.equals(Member.class)) {
            return option.getAsMember();
        } else if (type.equals(Role.class)) {
            return option.getAsRole();
        } else if (type.equals(IMentionable.class)) {
            return option.getAsMentionable();
        } else if (type.equals(Message.Attachment.class)) {
            return option.getAsAttachment();
        } else if (type.equals(byte.class) || type.equals(Byte.class)) {
            return (byte) option.getAsInt();
        } else if (type.equals(short.class) || type.equals(Short.class)) {
            return (short) option.getAsInt();
        }
        throw new IllegalArgumentException("Type not supported");
    }
}
