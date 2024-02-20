package fr.swansky.core.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(ElementType.PARAMETER)
public @interface Param {
    String name();

    String description() default "Default desc";

    boolean required() default true;

    String defaultValue() default "";
}
