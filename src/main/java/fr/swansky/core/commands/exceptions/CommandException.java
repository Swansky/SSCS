package fr.swansky.core.commands.exceptions;

public class CommandException extends RuntimeException {
    public CommandException(String message) {
        super(message);
    }
}
