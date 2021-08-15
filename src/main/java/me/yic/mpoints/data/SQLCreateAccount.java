/*
 *  This file (SQL.java) is a part of project MPoints
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
package me.yic.mpoints.data;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.utils.Points;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLCreateAccount extends SQL {

    public static void newPlayer(Player player) {
        Connection connection = database.getConnectionAndCheck();
        checkUser(player, connection);
        selectUser(player.getUniqueId().toString(), player.getName(), connection);
        database.closeHikariConnection(connection);
    }

    private static void createAccount(String UID, String user, Connection co_a) {
        try {
            String query1;
            int x = 0;
            PreparedStatement statement;
            for (String sign : Points.pointsigns.keySet()) {
                if (x > 30) {
                    break;
                }
                createDAccount(sign, UID, co_a);
                x += 1;
            }
            if (MPoints.config.getBoolean("Settings.mysql")) {
                query1 = "INSERT INTO mpoints_" + suffix + dataname1 + "(UID,player) values(?,?) "
                        + "ON DUPLICATE KEY UPDATE UID = ?";
            } else {
                query1 = "INSERT INTO mpoints_" + suffix + dataname1 + "(UID,player) values(?,?) ";
            }
            statement = co_a.prepareStatement(query1);
            statement.setString(1, UID);
            statement.setString(2, user);
            if (MPoints.config.getBoolean("Settings.mysql")) {
                statement.setString(3, UID);
            }
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void updateUser(String UID, String user, Connection co_a) {
        try {
            PreparedStatement statement = co_a.prepareStatement("update mpoints_" + suffix + dataname1 + " set player = ? where UID = ?");
            statement.setString(1, user);
            statement.setString(2, UID);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void checkUser(Player player, Connection connection) {
        try {
            String query;

            if (MPoints.config.getBoolean("Settings.mysql")) {
                query = "select * from mpoints_" + suffix + dataname1 + " where binary player = ?";
            } else {
                query = "select * from mpoints_" + suffix + dataname1 + " where player = ?";
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getName());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (!player.getUniqueId().toString().equals(rs.getString(1))) {
                    if (player.isOnline()) {
                        Bukkit.getScheduler().runTask(MPoints.getInstance(), () ->
                                player.kickPlayer("[MPoints] The same data exists in the server without different UUID"));
                    }
                }
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectUser(String UID, String name, Connection connection) {
        String user = "#";

        try {
            PreparedStatement statement = connection.prepareStatement("select * from mpoints_" + suffix + dataname1 + " where UID = ?");
            statement.setString(1, UID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = rs.getString(2);
            } else {
                user = name;
                createAccount(UID, user, connection);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!user.equals(name) && !user.equals("#")) {
            Cache.removeFromUUIDCache(name);
            updateUser(UID, name, connection);
            MPoints.getInstance().logger(" 名称已更改!", "<#>" + name);
        }
    }
}
