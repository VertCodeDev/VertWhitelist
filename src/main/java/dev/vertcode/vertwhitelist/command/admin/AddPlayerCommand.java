package dev.vertcode.vertwhitelist.command.admin;

import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertwhitelist.command.structure.VertWhitelistCommand;
import dev.vertcode.vertwhitelist.conf.Lang;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public class AddPlayerCommand extends VertWhitelistCommand {

    public AddPlayerCommand() {
        super("add", "vertwhitelist.command.whitelist.add",
                "Add a player to a server whitelist.", "/whitelist add <player*> <server>", "addplayer");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatUtils.getInstance().replacePlaceholders(Lang.GENERIC$WRONG_USAGE.getString(),
                    "%usage%", this.getUsage()));
            return;
        }

        UUID uuid = this.plugin.getCacheManager().getPlayerUUID(args[0]);

        if (uuid == null) {
            sender.sendMessage(ChatUtils.getInstance().replacePlaceholders(Lang.GENERIC$CANNOT_FIND_PLAYER.getString(),
                    "%player%", args[0]));
            return;
        }

        String playerName = this.plugin.getCacheManager().getPlayerName(uuid);

        if (args.length < 2) {
            if (this.plugin.getUserManager().isWhitelisted(uuid)) {
                sender.sendMessage(ChatUtils.getInstance().replacePlaceholders(Lang.COMMAND$WHITELIST$ADD$ALREADY_WHITELISTED.getString(),
                        "%player%", playerName));
                return;
            }

            this.plugin.getUserManager().whitelistPlayer(uuid, true);
            sender.sendMessage(ChatUtils.getInstance().replacePlaceholders(Lang.COMMAND$WHITELIST$ADD$WHITELISTED.getString(),
                    "%player%", playerName));
            return;
        }

        String serverId = args[1];

        if (!this.plugin.getWhitelistManager().isWhitelistServer(serverId)) {
            sender.sendMessage(Lang.GENERIC$CANNOT_FIND_SERVER.getString());
            return;
        }

        if (this.plugin.getUserManager().isWhitelisted(uuid, serverId)) {
            sender.sendMessage(ChatUtils.getInstance().replacePlaceholders(Lang.COMMAND$WHITELIST$ADD$ALREADY_WHITELISTED.getString(),
                    "%player%", playerName));
            return;
        }

        this.plugin.getUserManager().whitelistPlayer(uuid, serverId);
        sender.sendMessage(ChatUtils.getInstance().replacePlaceholders(Lang.COMMAND$WHITELIST$ADD$WHITELISTED.getString(),
                "%player%", playerName));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
