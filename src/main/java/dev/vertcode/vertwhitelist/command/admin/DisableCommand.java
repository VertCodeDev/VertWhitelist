package dev.vertcode.vertwhitelist.command.admin;

import dev.vertcode.vertwhitelist.command.structure.VertWhitelistCommand;
import dev.vertcode.vertwhitelist.conf.Lang;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DisableCommand extends VertWhitelistCommand {

    public DisableCommand() {
        super("disable", "vertwhitelist.command.whitelist.disable",
                "Disable a server whitelist.", "/whitelist disable <server>", "off");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            if (!this.plugin.getWhitelistManager().isWhitelistEnabled()) {
                sender.sendMessage(Lang.COMMAND$WHITELIST$DISABLE$ALREADY_DISABLED.getString());
                return;
            }

            this.plugin.getWhitelistManager().disableWhitelist();
            sender.sendMessage(Lang.COMMAND$WHITELIST$DISABLE$SUCCESS.getString());
            return;
        }

        String serverId = args[0];

        if (!this.plugin.getWhitelistManager().isWhitelistServer(serverId)) {
            sender.sendMessage(Lang.GENERIC$CANNOT_FIND_SERVER.getString());
            return;
        }

        if (!this.plugin.getWhitelistManager().isWhitelistEnabled(serverId)) {
            sender.sendMessage(Lang.COMMAND$WHITELIST$DISABLE$ALREADY_DISABLED.getString());
            return;
        }

        this.plugin.getWhitelistManager().disableWhitelist(serverId);
        sender.sendMessage(Lang.COMMAND$WHITELIST$DISABLE$SUCCESS.getString());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
