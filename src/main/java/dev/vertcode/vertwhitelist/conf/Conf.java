package dev.vertcode.vertwhitelist.conf;

import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.config.EnumConfig;
import dev.vertcode.vertwhitelist.data.object.SaveMethod;

import java.util.List;

public enum Conf implements EnumConfig {

    SERVER_ID("server01"),

    DATA$SAVE_METHOD(SaveMethod.MONGODB.name()),
    DATA$MONGO_DB_URI("MONGO_URI_HERE"),
    DATA$MYSQL$HOST_NAME("localhost"),
    DATA$MYSQL$PORT(3306),
    DATA$MYSQL$DATABASE("hexaiplock"),
    DATA$MYSQL$USERNAME("username"),
    DATA$MYSQL$PASSWORD("password"),
    ;

    private Object value;

    Conf(Object value) {
        this.value = value;
    }

    @Override
    public boolean getBoolean() {
        return (boolean) value;
    }

    @Override
    public String getString() {
        return (String) value;
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
