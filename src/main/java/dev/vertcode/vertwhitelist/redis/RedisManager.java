package dev.vertcode.vertwhitelist.redis;

import dev.vertcode.vertwhitelist.VertWhitelistPlugin;
import dev.vertcode.vertwhitelist.redis.event.PubSubEvent;
import lombok.Getter;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.io.File;

@Getter
public class RedisManager {

    private final VertWhitelistPlugin plugin;
    private final File configFile;
    private final Configuration config;
    private Config redisConfig;
    private RedissonClient redisClient;
    private PubSubEvent.Handler pubSubEventHandler;

    public RedisManager(VertWhitelistPlugin plugin) {
        this.plugin = plugin;

        this.configFile = new File(plugin.getDataFolder(), "redis.yml");
        if (!configFile.exists()) plugin.saveResource("redis.yml", false);

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        this.init();
    }

    private void init() {
        try {
            this.redisConfig = Config.fromYAML(this.configFile);
            this.redisConfig.setCodec(JsonJacksonCodec.INSTANCE);
        } catch (Exception ex) {
            System.out.println("Couldn't load redis, shutting down now.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            ex.printStackTrace();
        }
        this.redisClient = Redisson.create(this.redisConfig);

        this.pubSubEventHandler = new PubSubEvent.Handler();
        this.redisClient.getTopic("vertwhitelist").addListener(String.class, pubSubEventHandler);
    }

}
