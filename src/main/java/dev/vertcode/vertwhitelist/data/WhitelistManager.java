package dev.vertcode.vertwhitelist.data;

import dev.vertcode.vertwhitelist.VertWhitelistPlugin;
import dev.vertcode.vertwhitelist.conf.Conf;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RList;

@Getter
@Setter
public class WhitelistManager {

    private final VertWhitelistPlugin plugin;
    private final String serverID;

    public WhitelistManager(VertWhitelistPlugin plugin) {
        this.plugin = plugin;
        this.serverID = Conf.SERVER_ID.getString();

        RList<String> servers = this.plugin.getRedisManager().getRedisClient().getList("whitelist:servers");
        if (!servers.contains(serverID)) servers.add(serverID);
    }

    public void enableWhitelist() {
        this.enableWhitelist(serverID);
    }

    public void enableWhitelist(String id) {
        if (isWhitelistEnabled()) return;
        plugin.getRedisManager().getRedisClient().getList("whitelist:whitelisted").add(id);
    }

    public void disableWhitelist() {
        this.disableWhitelist(serverID);
    }

    public void disableWhitelist(String id) {
        if (!isWhitelistEnabled(id)) return;
        plugin.getRedisManager().getRedisClient().getList("whitelist:whitelisted").remove(id);
    }

    public boolean isWhitelistEnabled() {
        return plugin.getRedisManager().getRedisClient().getList("whitelist:whitelisted").contains(serverID);
    }

    public boolean isWhitelistEnabled(String id) {
        return plugin.getRedisManager().getRedisClient().getList("whitelist:whitelisted").contains(id);
    }

    public boolean isWhitelistServer(String id) {
        return this.plugin.getRedisManager().getRedisClient().getList("whitelist:servers").contains(id);
    }

}
