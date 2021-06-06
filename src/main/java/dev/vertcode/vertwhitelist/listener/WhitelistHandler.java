package dev.vertcode.vertwhitelist.listener;

import dev.vertcode.vertwhitelist.VertWhitelistPlugin;
import dev.vertcode.vertwhitelist.conf.Conf;
import dev.vertcode.vertwhitelist.conf.Lang;
import dev.vertcode.vertwhitelist.data.UserManager;
import dev.vertcode.vertwhitelist.redis.event.PubSubEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class WhitelistHandler implements Listener {

    private final VertWhitelistPlugin plugin = VertWhitelistPlugin.getInstance();
    private final UserManager userManager = plugin.getUserManager();

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        if (!plugin.getWhitelistManager().isWhitelistEnabled() ||
                (plugin.getWhitelistManager().isWhitelistEnabled() && userManager.isWhitelisted(event.getUniqueId())))
            return;

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Lang.JOIN$NOT_WHITELISTED.getString());
    }

    @EventHandler
    public void onPubSub(PubSubEvent event) {
        if (!event.getChannel().equalsIgnoreCase("vertwhitelist")) return;

        String[] args = event.getMessage().split(":");
        if (args.length < 2) return;

        String action = args[1];
        if (args[0].equalsIgnoreCase("whitelist")) {
            if (action.equalsIgnoreCase("clear")) {
                this.plugin.getUserManager().resetData(false);
                return;
            }

            if (args.length < 3) return;

            UUID uuid = UUID.fromString(args[2]);

            if (uuid == null) return;

            if (action.equalsIgnoreCase("remove")) {
                this.plugin.getUserManager().unWhitelistPlayer(uuid, false);
                return;
            }

            if (action.equalsIgnoreCase("add")) {
                this.plugin.getUserManager().whitelistPlayer(uuid, false);
                return;
            }
            return;
        }

        if (args.length < 3) return;

        if (args[0].equalsIgnoreCase("whitelist-target")) {
            String serverId = args[2];
            if (!serverId.equalsIgnoreCase(Conf.SERVER_ID.getString())) return;

            if (action.equalsIgnoreCase("clear")) {
                this.plugin.getUserManager().resetData(true);
                return;
            }

            if (args.length < 4) return;


            UUID uuid = UUID.fromString(args[3]);

            if (uuid == null) return;

            if (action.equalsIgnoreCase("remove")) {
                this.plugin.getUserManager().unWhitelistPlayer(uuid, true);
                return;
            }

            if (action.equalsIgnoreCase("add")) {
                this.plugin.getUserManager().whitelistPlayer(uuid, true);
                return;
            }
            return;
        }

    }

}
