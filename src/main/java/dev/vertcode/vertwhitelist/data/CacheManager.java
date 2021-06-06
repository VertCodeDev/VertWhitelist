package dev.vertcode.vertwhitelist.data;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CacheManager {

    private final Cache<String, UUID> uuidCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();
    private final Cache<UUID, String> nameCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    /**
     * This method gets the Name of a Player
     *
     * @param uuid The UUID of the Player
     * @return The name, null if not found
     */
    public String getPlayerName(UUID uuid) {
        try {
            this.nameCache.cleanUp();
            if (this.nameCache.asMap().containsKey(uuid))
                return this.nameCache.getIfPresent(uuid);

            URL url = new URL("https://api.mojang.com/user/profiles/" + uuid + "/names");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.connect();

            JsonArray array = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonArray.class);
            connection.disconnect();

            if (array == null || array.size() < 1) return null;

            String name = array.get(array.size() - 1).getAsJsonObject().get("name").getAsString();
            this.nameCache.put(uuid, name);
            return name;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * This method gets the UUID of a Player from the Cache
     *
     * @param name The Name of the Player
     * @return The UUID, null if not found
     */
    public UUID getPlayerUUID(String name) {
        try {
            this.uuidCache.cleanUp();
            if (this.uuidCache.asMap().containsKey(name))
                return this.uuidCache.getIfPresent(name);

            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.connect();

            Map<String, String> map = new Gson().fromJson(new InputStreamReader(connection.getInputStream()),
                    new TypeToken<Map<String, String>>() {
                    }.getType());

            connection.disconnect();
            String unDashedUUID = map.get("id");
            String uuidString = unDashedUUID.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            UUID uuid = UUID.fromString(uuidString);
            this.uuidCache.put(name, uuid);
            return uuid;
        } catch (Exception ex) {
            return null;
        }
    }

}
