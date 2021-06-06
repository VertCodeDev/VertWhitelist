package dev.vertcode.vertwhitelist.command;

import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertwhitelist.VertWhitelistPlugin;
import dev.vertcode.vertwhitelist.command.structure.VertWhitelistCommand;
import dev.vertcode.vertwhitelist.conf.Lang;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends VertWhitelistCommand {

    public HelpCommand() {
        super("help", "vertwhitelist.command.command.help", "Sends the help message.",
                "/whitelist help", "commands", "?");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!Lang.COMMAND$HELP$HEADER.getString().isEmpty())
            stringBuilder.append(Lang.COMMAND$HELP$HEADER.getString()).append("\n");
        VertWhitelistPlugin.getInstance().getWhitelistCommand().getWhitelistCommands().forEach(command -> {
            if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) return;
            stringBuilder.append(ChatUtils.getInstance().replacePlaceholders(Lang.COMMAND$HELP$ENTRY.getString(),
                    "%usage%", command.getUsage(),
                    "%description%", command.getDescription())).append("\n");
        });
        if (!Lang.COMMAND$HELP$FOOTER.getString().isEmpty())
            stringBuilder.append(Lang.COMMAND$HELP$FOOTER.getString());

        sender.sendMessage(stringBuilder.toString());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
