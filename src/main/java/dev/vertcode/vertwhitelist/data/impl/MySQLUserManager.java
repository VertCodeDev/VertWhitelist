package dev.vertcode.vertwhitelist.data.impl;

import com.zaxxer.hikari.HikariDataSource;
import dev.vertcode.vertwhitelist.VertWhitelistPlugin;
import dev.vertcode.vertwhitelist.conf.Conf;
import dev.vertcode.vertwhitelist.data.UserManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLUserManager implements UserManager {

    private final HikariDataSource hikari;
    private final String serverID;
    private Connection connection;

    public MySQLUserManager() {
        this.hikari = new HikariDataSource();
        this.hikari.setMaximumPoolSize(10);
        this.hikari.setMaxLifetime(45000);
        this.hikari.setConnectionTimeout(5000);
        this.hikari.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
        this.hikari.addDataSourceProperty("serverName", Conf.DATA$MYSQL$HOST_NAME.getString());
        this.hikari.addDataSourceProperty("port", String.valueOf(Conf.DATA$MYSQL$PORT.getInteger()));
        this.hikari.addDataSourceProperty("databaseName", Conf.DATA$MYSQL$DATABASE.getString());
        this.hikari.addDataSourceProperty("user", Conf.DATA$MYSQL$USERNAME.getString());
        this.hikari.addDataSourceProperty("password", Conf.DATA$MYSQL$PASSWORD.getString());

        this.serverID = Conf.SERVER_ID.getString();

        createTable();
        loadAll();
        System.out.println("Using MySQL as saving system.");
    }

    @Override
    public void loadAll() {
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `" + serverID + "`");
            ResultSet rs = ps.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                whitelistedPlayersCache.add(uuid);
            }
            System.out.println("Loaded " + whitelistedPlayersCache.size() + " whitelisted players.");
        } catch (Exception ex) {
            System.out.println("Something went wrong while loading the whitelisted players.");
            ex.printStackTrace();
        }
    }

    @Override
    public void save(UUID uuid) {
        try {
            Connection connection = getConnection();

            if (existsInDatabase(uuid)) return;

            PreparedStatement ps = connection.prepareStatement("INSERT INTO `" + serverID +
                    "` (uuid) VALUE " +
                    "(?)");

            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Something went wrong while saving " + uuid + " to the VertWhitelist db.");
        }
    }

    @Override
    public void remove(UUID uuid) {
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM `" + connection + "` WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Something went wrong while removing " + uuid + " from the VertWhitelist db.");
        }
    }

    @Override
    public void resetData(boolean fromDB) {
        whitelistedPlayersCache.clear();
        if (!fromDB) return;
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM `" + serverID + "`");
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Something went wrong while resetting the VertWhitelist db.");
        }

        VertWhitelistPlugin.getInstance().getRedisManager().getRedisClient().getTopic("vertwhitelist")
                .publish("whitelist:clear");
    }

    @Override
    public boolean isWhitelisted(UUID uuid, String serverID) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `" + serverID +
                    "` WHERE uuid=?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean existsInDatabase(UUID uuid) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `" + serverID +
                    "` WHERE uuid=?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (Exception ex) {
            return false;
        }
    }

    private void createTable() {
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + serverID +
                    "` " +
                    "(" +
                    "`uuid` VARCHAR(255) NULL DEFAULT NULL" +
                    ") " +
                    "COLLATE='utf8mb4_general_ci'");
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Something went wrong while creating the VertWhitelist table.");
        }
    }


    public void openConnection() {
        try {
            if (connection != null && !connection.isClosed()) return;
            connection = hikari.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        openConnection();
        return connection;
    }
}
