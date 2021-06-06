package dev.vertcode.vertwhitelist.conf;

import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.config.EnumConfig;

import java.util.List;

public enum Lang implements EnumConfig {

    GENERIC$WRONG_USAGE("&c&lVertWhitelist &8» &cWrong usage, usage: &f%usage%"),
    GENERIC$CANNOT_FIND_PLAYER("&c&lVertWhitelist &8» &cCannot find a player with the name &f%player%&c."),
    GENERIC$CANNOT_FIND_SERVER("&c&lVertWhitelist &8» &cCannot find a whitelist server with that id."),

    JOIN$NOT_WHITELISTED("&8&m--------&r&8[ &c&lWhitelist &8&m&r&8]&m-------\n" +
            "\n" +
            "&cYou are not whitelisted to this server.\n" +
            "\n" +
            "&8&m--------&r&8[ &c&lWhitelist &8&m&r&8]&m-------"),

    COMMAND$HELP$HEADER("&8&m------------&r&8[ &c&lCommands &8&m&r&8]&m------------"),
    COMMAND$HELP$ENTRY("&c%usage% &8- &f%description%"),
    COMMAND$HELP$FOOTER("&8&m------------&r&8[ &c&lCommands &8&m&r&8]&m------------"),

    COMMAND$WHITELIST$REMOVE$NOT_WHITELISTED("&c&lVertWhitelist &8» &f%player% &cis not whitelisted."),
    COMMAND$WHITELIST$REMOVE$UNWHITELISTED("&c&lVertWhitelist &8» &7Successfully removed &c%player% &7from the whitelist."),

    COMMAND$WHITELIST$ADD$ALREADY_WHITELISTED("&c&lVertWhitelist &8» &f%player% &cis already whitelisted."),
    COMMAND$WHITELIST$ADD$WHITELISTED("&c&lVertWhitelist &8» &7Successfully whitelisted &c%player%&7."),

    COMMAND$WHITELIST$CLEAR$SUCCESS("&c&lVertWhitelist &8» &7Successfully cleared the whitelist."),

    COMMAND$WHITELIST$LIST$LIST("&c&lVertWhitelist &8» &7Whitelisted players: &c%players%"),

    COMMAND$WHITELIST$ENABLE$ALREADY_ENABLED("&c&lVertWhitelist &8» &cThe whitelist is already enabled."),
    COMMAND$WHITELIST$ENABLE$SUCCESS("&c&lVertWhitelist &8» &7Successfully &aenabled &7the whitelist."),

    COMMAND$WHITELIST$DISABLE$ALREADY_DISABLED("&c&lVertWhitelist &8» &cThe whitelist is already disabled."),
    COMMAND$WHITELIST$DISABLE$SUCCESS("&c&lVertWhitelist &8» &7Successfully &cdisabled &7the whitelist."),
    ;

    private Object value;

    Lang(Object value) {
        this.value = value;
    }

    @Override
    public boolean getBoolean() {
        return (boolean) value;
    }

    @Override
    public String getString() {
        return ChatUtils.getInstance().colorize((String) value);
    }

    @Override
    public Integer getInteger() {
        return (Integer) value;
    }

    @Override
    public Double getDouble() {
        return (Double) value;
    }

    @Override
    public Float getFloat() {
        return (Float) value;
    }

    @Override
    public List<Object> getList() {
        return (List<Object>) value;
    }

    @Override
    public List<String> getStringList() {
        return (List<String>) value;
    }

    @Override
    public Object get() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String getPath() {
        return ChatUtils.getInstance().replacePlaceholders(this.name().toLowerCase(), "$", ".", "_", "-");
    }
}
