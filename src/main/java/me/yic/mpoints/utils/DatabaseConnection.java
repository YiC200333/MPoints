/*
 *  This file (DatabaseConnection.java) is a part of project MPoints
 *  Copyright (C) YiC and contributors
 *
 *  This program is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.yic.mpoints.utils;

import com.zaxxer.hikari.HikariDataSource;
import me.yic.mpoints.MPoints;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class DatabaseConnection {
    private String driver = "com.mysql.jdbc.Driver";
    //============================================================================================
    private static final File dataFolder = new File(MPoints.getInstance().getDataFolder(), "playerdata");
    private static String url = "jdbc:mysql://" + MPoints.config.getString("MySQL.host") + "/"
            + MPoints.config.getString("MySQL.database") + "?characterEncoding="
            + MPoints.config.getString("MySQL.encoding") + "&useSSL=false";
    private static final String username = MPoints.config.getString("MySQL.user");
    private static final String password = MPoints.config.getString("MySQL.pass");
    private static final Integer maxPoolSize = MPoints.config.getInt("Pool-Settings.maximum-pool-size");
    private static final Integer minIdle = MPoints.config.getInt("Pool-Settings.minimum-idle");
    private static final Integer maxLife = MPoints.config.getInt("Pool-Settings.maximum-lifetime");
    private static final Long idleTime = MPoints.config.getLong("Pool-Settings.idle-timeout");
    private static boolean secon = false;
    public static Integer waittimeout = 10;
    //============================================================================================
    public static File userdata = new File(dataFolder, "data.db");
    //============================================================================================
    private Connection connection = null;
    private HikariDataSource hikari = null;
    private boolean isfirstry = true;

    private void createNewHikariConfiguration() {
        hikari = new HikariDataSource();
        hikari.setPoolName("MPoints");
        hikari.setJdbcUrl(url);
        hikari.setUsername(username);
        hikari.setPassword(password);
        hikari.setMaximumPoolSize(maxPoolSize);
        hikari.setMinimumIdle(minIdle);
        hikari.setMaxLifetime(maxLife);
        hikari.addDataSourceProperty("cachePrepStmts", "true");
        hikari.addDataSourceProperty("prepStmtCacheSize", "250");
        hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikari.addDataSourceProperty("userServerPrepStmts", "true");
        if (ServerINFO.DDrivers) {
            hikari.setDriverClassName(driver);
        }
        if (hikari.getMinimumIdle() < hikari.getMaximumPoolSize()) {
            hikari.setIdleTimeout(idleTime);
        } else {
            hikari.setIdleTimeout(0);
        }
    }

    private void setDriver() {
        if (ServerINFO.DDrivers) {
            if (MPoints.config.getBoolean("Settings.mysql")) {
                driver = ("me.yic.libs.mysql.cj.jdbc.Driver");
            } else {
                driver = ("me.yic.libs.sqlite.JDBC");
            }
        } else {
            if (MPoints.config.getBoolean("Settings.mysql")) {
                driver = ("com.mysql.cj.jdbc.Driver");
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    driver = ("com.mysql.jdbc.Driver");
                }
            } else {
                driver = ("org.sqlite.JDBC");
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setTimezone() {
        if (!MPoints.config.getString("MySQL.timezone").equals("")) {
            url = url + "&serverTimezone=" + MPoints.config.getString("MySQL.timezone");
        }
    }

    public boolean setGlobalConnection() {
        setTimezone();
        setDriver();
        try {
            if (ServerINFO.EnableConnectionPool) {
                createNewHikariConfiguration();
                Connection connection = getConnection();
                closeHikariConnection(connection);
            } else {
                Class.forName(driver);
                if (MPoints.config.getBoolean("Settings.mysql")) {
                    connection = DriverManager.getConnection(url, username, password);
                } else {
                    connection = DriverManager.getConnection("jdbc:sqlite:" + userdata.toString());
                }
            }

            if (MPoints.config.getBoolean("Settings.mysql")) {
                if (secon) {
                    MPoints.getInstance().logger("MySQL重新连接成功", null);
                } else {
                    secon = true;
                }
            }
            return true;

        } catch (SQLException e) {
            MPoints.getInstance().logger("无法连接到数据库-----", null);
            e.printStackTrace();
            close();
            return false;

        } catch (ClassNotFoundException e) {
            MPoints.getInstance().logger("JDBC驱动加载失败", null);
        }

        return false;
    }

    public Connection getConnectionAndCheck() {
        if (!canConnect()) {
            return null;
        }
        try {
            return getConnection();
        } catch (SQLException e1) {
            if (isfirstry) {
                isfirstry = false;
                close();
                return getConnectionAndCheck();
            } else {
                isfirstry = true;
                MPoints.getInstance().logger("无法连接到数据库-----", null);
                close();
                e1.printStackTrace();
                return null;
            }
        }
    }

    public Connection getConnection() throws SQLException {
        if (ServerINFO.EnableConnectionPool) {
            return hikari.getConnection();
        } else {
            return connection;
        }
    }

    public boolean canConnect() {
        try {
            if (ServerINFO.EnableConnectionPool) {
                if (hikari == null) {
                    return setGlobalConnection();
                }

                if (hikari.isClosed()) {
                    return setGlobalConnection();
                }

            } else {
                if (connection == null) {
                    return setGlobalConnection();
                }

                if (connection.isClosed()) {
                    return setGlobalConnection();
                }

                if (MPoints.config.getBoolean("Settings.mysql")) {
                    if (!connection.isValid(waittimeout)) {
                        secon = false;
                        return setGlobalConnection();
                    }
                }
            }
        } catch (SQLException e) {
            Arrays.stream(e.getStackTrace()).forEach(d -> Bukkit.getLogger().info(d.toString()));
            return false;
        }
        return true;
    }

    public void closeHikariConnection(Connection connection) {
        if (!ServerINFO.EnableConnectionPool) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (hikari != null) {
                hikari.close();
            }
        } catch (SQLException e) {
            if (MPoints.config.getBoolean("Settings.mysql")) {
                MPoints.getInstance().logger("MySQL连接断开失败", null);
            }
            e.printStackTrace();
        }
    }
}
