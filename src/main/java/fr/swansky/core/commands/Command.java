package fr.swansky.core.commands;

import net.dv8tion.jda.api.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(ElementType.TYPE)
public @interface Command {
    String name();

    String description() default "Default desc";

    boolean guildOnly() default true;

    boolean nsfw() default false;

    Permission[] permissions() default {};
}
