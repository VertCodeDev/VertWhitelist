package dev.vertcode.vertwhitelist.command.admin;

import dev.vertcode.vertwhitelist.command.structure.VertWhitelistCommand;
import dev.vertcode.vertwhitelist.conf.Lang;
import org.bukkit.command.CommandSender;

import java.util.List;

public class EnableCommand extends VertWhitelistCommand {

    public EnableCommand() {
        super("enable", "vertwhitelist.command.whitelist.enable",
                "Enable a server whitelist.", "/whitelist enable <server>", "on");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            if (this.plugin.getWhitelistManager().isWhitelistEnabled()) {
                sender.sendMessage(Lang.COMMAND$WHITELIST$ENABLE$ALREADY_ENABLED.getString());
                return;
            }

            this.plugin.getWhitelistManager().enableWhitelist();
            sender.sendMessage(Lang.COMMAND$WHITELIST$ENABLE$SUCCESS.getString());
            return;
        }

        String serverId = args[0];

        if (!this.plugin.getWhitelistManager().isWhitelistServer(serverId)) {
            sender.sendMessage(Lang.GENERIC$CANNOT_FIND_SERVER.getString());
            return;
        }

        if (this.plugin.getWhitelistManager().isWhitelistEnabled(serverId)) {
            sender.sendMessage(Lang.COMMAND$WHITELIST$ENABLE$ALREADY_ENABLED.getString());
            return;
        }

        this.plugin.getWhitelistManager().enableWhitelist(serverId);
        sender.sendMessage(Lang.COMMAND$WHITELIST$ENABLE$SUCCESS.getString());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
