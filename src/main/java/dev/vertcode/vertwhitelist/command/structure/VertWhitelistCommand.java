package dev.vertcode.vertwhitelist.command.structure;

import dev.vertcode.vertwhitelist.VertWhitelistPlugin;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.List;

@Getter
public abstract class VertWhitelistCommand {

    private final String[] aliases;
    private final String description, usage;
    private final String command, permission;
    public VertWhitelistPlugin plugin = VertWhitelistPlugin.getInstance();

    public VertWhitelistCommand(String command, String permission, String description, String usage, String... aliases) {
        this.command = command;
        this.permission = permission;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

}
