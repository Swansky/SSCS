package fr.swansky.core.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleCommand {

    private final String name;
    private final String description;
    private boolean guildOnly;
    private boolean nsfw;
    private final List<Permission> perms = new ArrayList<>();
    private final List<OptionData> options = new ArrayList<>();
    private final List<SimpleSubCommand> subCommands = new ArrayList<>();
    private final Object instance;
    private Method mainCommand;


    public SimpleCommand(String name, String description, Object instance) {
        this.name = name.toLowerCase();
        this.description = description;
        this.instance = instance;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isGuildOnly() {
        return guildOnly;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public DefaultMemberPermissions getDefaultPermissions() {
        return DefaultMemberPermissions.enabledFor(perms);
    }

    public List<OptionData> getOptions() {
        return options;
    }

    public SlashCommandData createCommandData() {
        SlashCommandData commandData = Commands.slash(getName(), getDescription());
        commandData.setGuildOnly(isGuildOnly());
        commandData.setNSFW(isNsfw());
        commandData.setDefaultPermissions(getDefaultPermissions());
        commandData.addOptions(getOptions());
        commandData.addSubcommands(getSubCommands().stream().map(SimpleSubCommand::createCommandData).toList());
        return commandData;
    }

    public List<SimpleSubCommand> getSubCommands() {
        return subCommands;
    }

    public List<Permission> getPerms() {
        return perms;
    }

    public void setGuildOnly(boolean b) {
        this.guildOnly = b;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public void setPerms(List<Permission> list) {
        perms.addAll(list);
    }

    public void setMainCommand(Method method) {
        this.mainCommand = method;
    }

    public void addSubCommand(SimpleSubCommand simpleSubCommand) {
        subCommands.add(simpleSubCommand);
    }

    public Method getMainCommand() {
        return mainCommand;
    }

    public Object getInstance() {
        return instance;
    }

    public Optional<SimpleSubCommand> findSubCommand(String s) {
        return subCommands.stream().filter(simpleSubCommand -> simpleSubCommand.getName().equals(s)).findFirst();
    }
}
