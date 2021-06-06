package dev.vertcode.vertwhitelist;

import dev.vertcode.vertlibrary.base.VertPluginBase;
import dev.vertcode.vertwhitelist.command.WhitelistCommand;
import dev.vertcode.vertwhitelist.conf.Conf;
import dev.vertcode.vertwhitelist.conf.Lang;
import dev.vertcode.vertwhitelist.data.CacheManager;
import dev.vertcode.vertwhitelist.data.UserManager;
import dev.vertcode.vertwhitelist.data.WhitelistManager;
import dev.vertcode.vertwhitelist.data.impl.MongoUserManager;
import dev.vertcode.vertwhitelist.data.impl.MySQLUserManager;
import dev.vertcode.vertwhitelist.data.object.SaveMethod;
import dev.vertcode.vertwhitelist.listener.WhitelistHandler;
import dev.vertcode.vertwhitelist.redis.RedisManager;
import lombok.Getter;

@Getter
public final class VertWhitelistPlugin extends VertPluginBase {

    private static VertWhitelistPlugin instance;
    private RedisManager redisManager;
    private CacheManager cacheManager;
    private WhitelistManager whitelistManager;
    private UserManager userManager;
    private WhitelistCommand whitelistCommand;

    public static VertWhitelistPlugin getInstance() {
        return instance;
    }

    @Override
    public void onStartup() {
        instance = this;

        registerConfig("config", Conf.class);
        registerConfig("language", Lang.class);

        this.redisManager = new RedisManager(this);
        this.cacheManager = new CacheManager();
        this.whitelistManager = new WhitelistManager(this);
        this.loadUserManager();

        registerListener(new WhitelistHandler());
        registerCommand(this.whitelistCommand = new WhitelistCommand());
    }

    @Override
    public void onShutdown() {
    }

    private void loadUserManager() {
        SaveMethod saveMethod = SaveMethod.getByString(Conf.DATA$SAVE_METHOD.getString()).orElse(SaveMethod.MONGODB);

        if (saveMethod == SaveMethod.MONGODB) this.userManager = new MongoUserManager();
        else this.userManager = new MySQLUserManager();
    }
}
