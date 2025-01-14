package fr.swansky.core.commands.providers;

import fr.swansky.core.commands.providers.common.DateProvider;
import fr.swansky.core.commands.providers.discord.AttachmentProvider;
import fr.swansky.core.commands.providers.discord.MemberProvider;
import fr.swansky.core.commands.providers.discord.RoleProvider;
import fr.swansky.core.commands.providers.discord.UserProvider;
import fr.swansky.core.commands.providers.discord.channels.*;
import fr.swansky.core.commands.providers.primitivetypes.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProviderBuilder {
    private ProviderBuilder() {
        throw new IllegalArgumentException("Utility class");
    }

    public static Map<Class<?>, ParamProvider<?>> getBuiltinProviders() {
        Map<Class<?>, ParamProvider<?>> providers = new HashMap<>();
        providers.put(Boolean.class, BooleanProvider.INSTANCE);
        providers.put(boolean.class, BooleanProvider.INSTANCE);
        providers.put(Integer.class, IntegerProvider.INSTANCE);
        providers.put(int.class, IntegerProvider.INSTANCE);
        providers.put(Long.class, LongProvider.INSTANCE);
        providers.put(long.class, LongProvider.INSTANCE);
        providers.put(Double.class, DoubleProvider.INSTANCE);
        providers.put(double.class, DoubleProvider.INSTANCE);
        providers.put(String.class, StringProvider.INSTANCE);

        providers.put(Message.Attachment.class, new AttachmentProvider());
        providers.put(MemberProvider.class, new MemberProvider());
        providers.put(RoleProvider.class, new RoleProvider());
        providers.put(User.class, new UserProvider());

        providers.put(Category.class, new CategoryProvider());
        providers.put(ForumChannelProvider.class, new ForumChannelProvider());
        providers.put(GuildMessageChannelProvider.class, new GuildMessageChannelProvider());
        providers.put(MediaChannel.class, new MediaChannelProvider());
        providers.put(NewsChannel.class, new NewsChannelProvider());
        providers.put(TextChannel.class, new TextChannelProvider());
        providers.put(VoiceChannel.class, new VoiceChannelProvider());
        providers.put(ThreadChannel.class, new ThreadChannelProvider());

        providers.put(Date.class, DateProvider.INSTANCE);


        return providers;
    }

}
