package dev.vertcode.vertwhitelist.data;

import dev.vertcode.vertwhitelist.VertWhitelistPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public interface UserManager {

    List<UUID> whitelistedPlayersCache = new ArrayList<>();

    void loadAll();

    void save(UUID uuid);

    void remove(UUID uuid);

    void resetData(boolean fromDB);

    default void whitelistPlayer(UUID uuid, boolean addToDB) {
        if (!whitelistedPlayersCache.contains(uuid)) whitelistedPlayersCache.add(uuid);
        if (addToDB) {
            save(uuid);
            VertWhitelistPlugin.getInstance().getRedisManager().getRedisClient().getTopic("vertwhitelist")
                    .publish("whitelist:add:" + uuid);
        }
    }

    default void unWhitelistPlayer(UUID uuid, boolean removeFromDB) {
        whitelistedPlayersCache.remove(uuid);
        if (removeFromDB) {
            remove(uuid);
            VertWhitelistPlugin.getInstance().getRedisManager().getRedisClient().getTopic("vertwhitelist")
                    .publish("whitelist:remove:" + uuid);
        }
    }

    default void whitelistPlayer(UUID uuid, String serverId) {
        VertWhitelistPlugin.getInstance().getRedisManager().getRedisClient().getTopic("vertwhitelist")
                .publish("whitelist-target:add:" + serverId + ":" + uuid);
    }

    default void unWhitelistPlayer(UUID uuid, String serverId) {
        VertWhitelistPlugin.getInstance().getRedisManager().getRedisClient().getTopic("vertwhitelist")
                .publish("whitelist-target:remove:" + serverId + ":" + uuid);
    }

    default boolean isWhitelisted(UUID uuid) {
        return whitelistedPlayersCache.contains(uuid);
    }

    boolean isWhitelisted(UUID uuid, String serverID);

    default List<UUID> getWhitelistPlayers() {
        return whitelistedPlayersCache;
    }

    default List<String> getPlayerNames() {
        return whitelistedPlayersCache.stream().map(uuid -> VertWhitelistPlugin.getInstance().getCacheManager().getPlayerName(uuid))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

}
