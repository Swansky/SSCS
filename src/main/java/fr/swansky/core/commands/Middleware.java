package fr.swansky.core.commands;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public interface Middleware {
    /**
     * Can be used to check if the user can use the command or not
     *
     * @param event         event of the command interaction execute or autocomplete
     * @param simpleCommand default command definition
     * @return true if the user can use the command
     */
    boolean handle(GenericInteractionCreateEvent event, SimpleCommand simpleCommand);
}
