package dev.vertcode.vertwhitelist.data;

import dev.vertcode.vertwhitelist.VertWhitelistPlugin;
import dev.vertcode.vertwhitelist.conf.Conf;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RList;

import java.text.SimpleDateFormat;

@Getter
@Setter
public class WhitelistManager {

    private final VertWhitelistPlugin plugin;
    private final String serverID;
    private long autoCloseTime = 0L;

    public WhitelistManager(VertWhitelistPlugin plugin) {
        this.plugin = plugin;
        this.serverID = Conf.SERVER_ID.getString();

        RList<String> servers = this.plugin.getRedisManager().getRedisClient().getList("whitelist:servers");
        if (!servers.contains(serverID)) servers.add(serverID);

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa MM/dd/yyyy");

            this.autoCloseTime = simpleDateFormat.parse(Conf.AUTO_DISABLE$DATE.getString()).getTime();
        } catch (Exception ex) {
        }
    }

    /**
     * Enables the whitelist for this server. (Not for the matching id's)
     */
    public void enableWhitelist() {
        this.enableWhitelist(serverID);
    }

    /**
     * Enables the whitelist for all the servers with the matching serverID.
     *
     * @param serverID the id of the server you want to enable whitelist from
     */
    public void enableWhitelist(String serverID) {
        if (isWhitelistEnabled(serverID)) return;
        plugin.getRedisManager().getRedisClient().getList("whitelist:whitelisted").add(serverID);
    }

    /**
     * Disables the whitelist for this server. (Not for the matching id's)
     */
    public void disableWhitelist() {
        this.disableWhitelist(serverID);
    }

    /**
     * Disables the whitelist for all the servers with the matching serverID.
     *
     * @param serverID the id of the server you want to disable the whitelist from
     */
    public void disableWhitelist(String serverID) {
        if (!isWhitelistEnabled(serverID)) return;
        plugin.getRedisManager().getRedisClient().getList("whitelist:whitelisted").remove(serverID);
    }

    /**
     * Checks if this current server is whitelisted or not.
     *
     * @return true | false
     */
    public boolean isWhitelistEnabled() {
        return plugin.getRedisManager().getRedisClient().getList("whitelist:whitelisted").contains(serverID);
    }

    /**
     * Checks the server with the matching id is whitelisted or not.
     *
     * @param serverID the id of the server you want to check it for
     * @return true | false
     */
    public boolean isWhitelistEnabled(String serverID) {
        return plugin.getRedisManager().getRedisClient().getList("whitelist:whitelisted").contains(serverID);
    }

    /**
     * Checks if a serverID is a valid whitelist server id.
     *
     * @param serverID the id you want to check it for
     * @return true | false
     */
    public boolean isWhitelistServer(String serverID) {
        return this.plugin.getRedisManager().getRedisClient().getList("whitelist:servers").contains(serverID);
    }

    /**
     * Returns the auto close time.
     *
     * @return the time of when the whitelist should automatically disable
     */
    public long getAutoCloseTime() {
        return autoCloseTime;
    }
}
