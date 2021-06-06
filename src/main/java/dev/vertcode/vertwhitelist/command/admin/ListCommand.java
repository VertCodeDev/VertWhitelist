package dev.vertcode.vertwhitelist.command.admin;

import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertwhitelist.command.structure.VertWhitelistCommand;
import dev.vertcode.vertwhitelist.conf.Lang;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand extends VertWhitelistCommand {

    public ListCommand() {
        super("list", "vertwhitelist.command.whitelist.list",
                "Receive a list with whitelisted players.", "/whitelist list", "players");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String whitelistedPlayers = String.join(", ", this.plugin.getUserManager().getPlayerNames());

        if (whitelistedPlayers.isEmpty()) whitelistedPlayers = "N/A";

        sender.sendMessage(ChatUtils.getInstance().replacePlaceholders(Lang.COMMAND$WHITELIST$LIST$LIST.getString(),
                "%players%", whitelistedPlayers));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
