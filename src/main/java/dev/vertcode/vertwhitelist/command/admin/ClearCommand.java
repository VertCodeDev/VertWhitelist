package dev.vertcode.vertwhitelist.command.admin;

import dev.vertcode.vertwhitelist.VertWhitelistPlugin;
import dev.vertcode.vertwhitelist.command.structure.VertWhitelistCommand;
import dev.vertcode.vertwhitelist.conf.Lang;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ClearCommand extends VertWhitelistCommand {

    public ClearCommand() {
        super("clear", "vertwhitelist.command.whitelist.clear",
                "Clear a server whitelist.", "/whitelist clear <server>", "reset");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            this.plugin.getUserManager().resetData(true);
            sender.sendMessage(Lang.COMMAND$WHITELIST$CLEAR$SUCCESS.getString());
            return;
        }
        String serverId = args[0];

        if (!this.plugin.getWhitelistManager().isWhitelistServer(serverId)) {
            sender.sendMessage(Lang.GENERIC$CANNOT_FIND_SERVER.getString());
            return;
        }

        VertWhitelistPlugin.getInstance().getRedisManager().getRedisClient().getTopic("vertwhitelist")
                .publish("whitelist-target:clear:" + serverId);
        sender.sendMessage(Lang.COMMAND$WHITELIST$CLEAR$SUCCESS.getString());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
