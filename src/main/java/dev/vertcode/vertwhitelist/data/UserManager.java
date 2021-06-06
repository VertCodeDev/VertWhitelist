package dev.vertcode.vertwhitelist.data;

import dev.vertcode.vertwhitelist.VertWhitelistPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public interface UserManager {

    List<UUID> whitelistedPlayersCache = new ArrayList<>();

    /**
     * Load all the whitelisted players from the database.
     */
    void loadAll();

    /**
     * Saves a whitelisted player to the database.
     *
     * @param uuid the {@link UUID} of the player you want to save to the db
     */
    void save(UUID uuid);

    /**
     * Remove a whitelisted player from the database.
     *
     * @param uuid the {@link UUID} of the player you want to remove from the db
     */
    void remove(UUID uuid);

    /**
     * Resets the whitelist.
     *
     * @param fromDB true if it should remove it from the db, false if it only has to delete the data from the cache
     */
    void resetData(boolean fromDB);

    /**
     * Whitelists a player for this current server.
     *
     * @param uuid    the {@link UUID} of the player you want to whitelist
     * @param addToDB true if it should save it to the db, false if it only has to add the data to the cache
     */
    default void whitelistPlayer(UUID uuid, boolean addToDB) {
        if (!whitelistedPlayersCache.contains(uuid)) whitelistedPlayersCache.add(uuid);
        if (!addToDB) return;
        save(uuid);
        VertWhitelistPlugin.getInstance().getRedisManager().getRedisClient().getTopic("vertwhitelist")
                .publish("whitelist:add:" + uuid);
    }

    /**
     * Whitelists a player for this current server.
     *
     * @param uuid         the {@link UUID} of the player you want to remove from the whitelist
     * @param removeFromDB true if it should remove it from the db, false if it only has to delete the data from the cache
     */
    default void unWhitelistPlayer(UUID uuid, boolean removeFromDB) {
        whitelistedPlayersCache.remove(uuid);
        if (!removeFromDB) return;
        remove(uuid);
        VertWhitelistPlugin.getInstance().getRedisManager().getRedisClient().getTopic("vertwhitelist")
                .publish("whitelist:remove:" + uuid);
    }

    /**
     * Sends a redis message to remove a player from the whitelist of the server with the matching serverId
     *
     * @param uuid     the {@link UUID} of the player you want to whitelist
     * @param serverId the id of the server you want to whitelist the player at
     */
    default void whitelistPlayer(UUID uuid, String serverId) {
        VertWhitelistPlugin.getInstance().getRedisManager().getRedisClient().getTopic("vertwhitelist")
                .publish("whitelist-target:add:" + serverId + ":" + uuid);
    }

    /**
     * @param uuid     the {@link UUID} of the player you want to remove from the whitelist
     * @param serverId the id of the server you want to remove the player from whitelist at
     */
    default void unWhitelistPlayer(UUID uuid, String serverId) {
        VertWhitelistPlugin.getInstance().getRedisManager().getRedisClient().getTopic("vertwhitelist")
                .publish("whitelist-target:remove:" + serverId + ":" + uuid);
    }

    /**
     * Checks if the player is whitelisted in this server.
     *
     * @param uuid the {@link UUID} of the player you want to check it for
     * @return true | false
     */
    default boolean isWhitelisted(UUID uuid) {
        return whitelistedPlayersCache.contains(uuid);
    }

    /**
     * Checks if the player is whitelisted in the server with the matching serverID.
     *
     * @param uuid the {@link UUID} of the player you want to check it for
     * @return true | false
     */
    boolean isWhitelisted(UUID uuid, String serverID);

    /**
     * The list with all the uuids of the whitelisted players their uuids.
     *
     * @return the cached whitelisted players
     */
    default List<UUID> getWhitelistPlayers() {
        return whitelistedPlayersCache;
    }

    /**
     * The list with all the uuids of the whitelisted players their names.
     *
     * @return the cached whitelisted players
     */
    default List<String> getPlayerNames() {
        return whitelistedPlayersCache.stream().map(uuid -> VertWhitelistPlugin.getInstance().getCacheManager().getPlayerName(uuid))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

}
