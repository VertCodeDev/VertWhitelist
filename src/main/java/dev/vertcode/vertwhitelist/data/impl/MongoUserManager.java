package dev.vertcode.vertwhitelist.data.impl;

import dev.vertcode.vertlibrary.mongo.MongoDB;
import dev.vertcode.vertlibrary.mongo.MongoDBSettings;
import dev.vertcode.vertwhitelist.VertWhitelistPlugin;
import dev.vertcode.vertwhitelist.conf.Conf;
import dev.vertcode.vertwhitelist.data.UserManager;
import dev.vertcode.vertwhitelist.data.object.ServerWhitelist;

import java.util.Optional;
import java.util.UUID;

public class MongoUserManager implements UserManager {

    private final MongoDB mongoDB;
    private ServerWhitelist serverWhitelist;

    public MongoUserManager() {
        this.mongoDB = new MongoDB(new MongoDBSettings(Conf.DATA$MONGO_DB_URI.getString(),
                "vertwhitelist"), VertWhitelistPlugin.class.getClassLoader());
        this.loadAll();
    }

    @Override
    public void loadAll() {
        whitelistedPlayersCache.clear();
        Optional<ServerWhitelist> optional = mongoDB.optional("serverId", Conf.SERVER_ID.getString(), ServerWhitelist.class);
        if (!optional.isPresent()) {
            serverWhitelist = new ServerWhitelist(Conf.SERVER_ID.getString());
            mongoDB.save(serverWhitelist);
            return;
        }

        ServerWhitelist serverWhitelist = optional.get();
        whitelistedPlayersCache.addAll(serverWhitelist.getWhitelistedPlayers());
        System.out.println("Loaded " + serverWhitelist.getWhitelistedPlayers().size() + " whitelisted players.");
    }

    @Override
    public void save(UUID uuid) {
        if (serverWhitelist == null) serverWhitelist = new ServerWhitelist(Conf.SERVER_ID.getString());
        serverWhitelist.getWhitelistedPlayers().add(uuid);
        mongoDB.save(serverWhitelist);
    }

    @Override
    public void remove(UUID uuid) {
        if (serverWhitelist == null) serverWhitelist = new ServerWhitelist(Conf.SERVER_ID.getString());
        serverWhitelist.getWhitelistedPlayers().remove(uuid);
        mongoDB.save(serverWhitelist);
    }

    @Override
    public void resetData(boolean fromDB) {
        if (serverWhitelist == null) serverWhitelist = new ServerWhitelist(Conf.SERVER_ID.getString());
        serverWhitelist.getWhitelistedPlayers().clear();
        whitelistedPlayersCache.clear();
        if (!fromDB) return;
        mongoDB.save(serverWhitelist);
        VertWhitelistPlugin.getInstance().getRedisManager().getRedisClient().getTopic("vertwhitelist")
                .publish("whitelist:clear");
    }

    @Override
    public boolean isWhitelisted(UUID uuid, String serverID) {
        Optional<ServerWhitelist> optional = this.mongoDB.optional("serverId", serverID, ServerWhitelist.class);
        if (!optional.isPresent()) return false;

        System.out.println(optional.get().getWhitelistedPlayers().contains(uuid));
        return optional.get().getWhitelistedPlayers().contains(uuid);
    }

}
