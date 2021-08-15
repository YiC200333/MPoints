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
import me.yic.mpoints.utils.DatabaseConnection;
import me.yic.mpoints.utils.PlayerData;
import me.yic.mpoints.utils.Points;
import me.yic.mpoints.utils.ServerINFO;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQL {

    public final static DatabaseConnection database = new DatabaseConnection();
    private static final String encoding = MPoints.config.getString("MySQL.encoding");
    public static final String dataname1 = "playerinfo";
    public static final String dataname2 = "record";
    public static String suffix = "";

    public static boolean con() {
        return database.setGlobalConnection();
    }

    public static void close() {
        database.close();
    }

    public static void getwaittimeout() {
        if (MPoints.config.getBoolean("Settings.mysql") && !ServerINFO.EnableConnectionPool) {
            try {
                Connection connection = database.getConnectionAndCheck();

                String query = "show variables like 'wait_timeout'";

                PreparedStatement statement = connection.prepareStatement(query);

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    int waittime = rs.getInt(2);
                    if (waittime > 50) {
                        DatabaseConnection.waittimeout = waittime - 30;
                    }

                }

                rs.close();
                statement.close();
                database.closeHikariConnection(connection);

            } catch (SQLException ignored) {
                MPoints.getInstance().logger(null, "Get 'wait_timeout' error");
            }
        }
    }

    public static void createTable() {
        try {
            Connection connection = database.getConnectionAndCheck();
            Statement statement = connection.createStatement();

            if (statement == null) {
                return;
            }

            String query1;
            String query2;
            String query3 = "CREATE TABLE IF NOT EXISTS mpoints_" + suffix + dataname2
                    + " (id int(20) not null auto_increment, type varchar(50) not null, UID varchar(50) not null, player varchar(50) not null,"
                    + " sign varchar(50) not null, balance decimal(20,2), amount decimal(20,2) not null, operation varchar(50) not null,"
                    + " date varchar(50) not null, command varchar(50) not null,"
                    + "primary key (id)) DEFAULT CHARSET = " + encoding + ";";
            if (MPoints.config.getBoolean("Settings.mysql")) {
                query1 = "CREATE TABLE IF NOT EXISTS mpoints_" + suffix + dataname1
                        + " (UID varchar(50) not null, player varchar(50) not null, "
                        + "primary key (UID)) DEFAULT CHARSET = " + encoding + ";";
            } else {
                query1 = "CREATE TABLE IF NOT EXISTS mpoints_" + suffix + dataname1
                        + " (UID varchar(50) not null, player varchar(50) not null, "
                        + "primary key (UID));";
            }
            statement.executeUpdate(query1);
            for (String sign : Points.pointsigns.keySet()) {
                if (MPoints.config.getBoolean("Settings.mysql")) {
                    query2 = "CREATE TABLE IF NOT EXISTS mpoints_" + suffix + sign
                            + " (UID varchar(50) not null, balance decimal(30,2) not null, hidden int(5) not null, "
                            + "primary key (UID)) DEFAULT CHARSET = " + encoding + ";";
                } else {
                    query2 = "CREATE TABLE IF NOT EXISTS mpoints_" + suffix + sign
                            + " (UID varchar(50) not null, balance decimal(30,2) not null, hidden int(5) not null, "
                            + "primary key (UID));";
                }
                statement.executeUpdate(query2);
            }
            if (MPoints.config.getBoolean("Settings.mysql") && MPoints.config.getBoolean("Settings.transaction-record")) {
                statement.executeUpdate(query3);
            }
            statement.close();
            database.closeHikariConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createDAccount(String sign, String UID, Connection co_a) {
        try {
            String query1;
            PreparedStatement statement;
            if (MPoints.config.getBoolean("Settings.mysql")) {
                query1 = "INSERT INTO mpoints_" + suffix + sign + "(UID,balance,hidden) values(?,?,?) "
                        + "ON DUPLICATE KEY UPDATE UID = ?";
            } else {
                query1 = "INSERT INTO mpoints_" + suffix + sign + "(UID,balance,hidden) values(?,?,?) ";
            }
            statement = co_a.prepareStatement(query1);
            statement.setString(1, UID);
            statement.setBigDecimal(2, Points.getinitialbal(sign));
            statement.setInt(3, 0);
            if (MPoints.config.getBoolean("Settings.mysql")) {
                statement.setString(4, UID);
            }
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void save(UUID u, String sign, Boolean isAdd, PlayerData pd) {
        Connection connection = database.getConnectionAndCheck();
        BigDecimal amount = pd.getamount();
        try {
            String query;
            if (isAdd == null) {
                query = " set balance = " + amount + " where UID = ?";
            } else if (isAdd) {
                query = " set balance = balance + " + amount + " where UID = ?";
            } else {
                query = " set balance = balance - " + amount + " where UID = ?";
            }
            boolean requirefresh = false;
            if (MPoints.config.getBoolean("Settings.cache-correction") && isAdd != null) {
                requirefresh = true;
                query = query + "AND balance = " + pd.getbalance().toString();
            }
            PreparedStatement statement1 = connection.prepareStatement("update mpoints_" + suffix + sign + query);
            statement1.setString(1, u.toString());
            int rs = statement1.executeUpdate();
            statement1.close();
            if (requirefresh && rs == 0) {
                Cache.refreshFromCache(u, sign);
                Cache.cachecorrection(u, sign, amount, isAdd);
                pd.addcachecorrection();
                if (isAdd) {
                    query = " set balance = balance + " + amount + " where UID = ?";
                } else {
                    query = " set balance = balance - " + amount + " where UID = ?";
                }
                PreparedStatement statement2 = connection.prepareStatement("update mpoints_" + suffix + sign + query);
                statement2.setString(1, u.toString());
                statement2.executeUpdate();
                statement2.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        record(pd, connection);
        database.closeHikariConnection(connection);
    }

    public static void saveall(String sign, String targettype, List<UUID> players, BigDecimal amount, Boolean isAdd, PlayerData x) {
        Connection connection = database.getConnectionAndCheck();
        try {
            if (targettype.equalsIgnoreCase("all")) {
                String query;

                if (isAdd == null) {
                    query = " set balance = " + amount;
                }else if (isAdd) {
                    query = " set balance = balance + " + amount;
                } else {
                    query = " set balance = balance - " + amount;
                }
                PreparedStatement statement = connection.prepareStatement("update mpoints_" + suffix + sign + query);
                statement.executeUpdate();
                statement.close();
            } else if (targettype.equalsIgnoreCase("online")) {
                StringBuilder query;
                if (isAdd == null) {
                    query = new StringBuilder(" set balance = " + amount + " where");
                }else if (isAdd) {
                    query = new StringBuilder(" set balance = balance + " + amount + " where");
                } else {
                    query = new StringBuilder(" set balance = balance - " + amount + " where");
                }

                int jsm = players.size();
                int js = 1;

                for (UUID u : players) {
                    if (js == jsm) {
                        query.append(" UID = '").append(u.toString()).append("'");
                    } else {
                        query.append(" UID = '").append(u.toString()).append("' OR");
                        js = js + 1;
                    }
                }
                PreparedStatement statement = connection.prepareStatement("update mpoints_" + suffix + sign + query);
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (x != null) {
            record(x, connection);
        }
        database.closeHikariConnection(connection);
    }

    public static void select(UUID uuid, String sign) {
        try {
            Connection connection = database.getConnectionAndCheck();
            PreparedStatement statement = connection.prepareStatement("select * from mpoints_" + suffix + sign + " where UID = ?");
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                BigDecimal cacheThisAmt = DataFormat.formatString(sign, rs.getString(2));
                Cache.insertIntoCache(uuid, sign, cacheThisAmt);
            }else{
                createDAccount(sign, uuid.toString(), connection);
            }

            rs.close();
            statement.close();
            database.closeHikariConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void selectUID(String name) {
        try {
            Connection connection = database.getConnectionAndCheck();
            String query;

            if (MPoints.config.getBoolean("Settings.mysql")) {
                query = "select * from mpoints_" + suffix + dataname1 + " where binary player = ?";
            } else {
                query = "select * from mpoints_" + suffix + dataname1 + " where player = ?";
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                UUID id = UUID.fromString(rs.getString(1));
                Cache.insertIntoUUIDCache(name, id);
            }
            rs.close();
            statement.close();
            database.closeHikariConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getBaltop() {
        try {
            Connection connection = database.getConnectionAndCheck();
            int x = 0;
            for (String sign : Points.pointsigns.keySet()) {
                if (Points.getenablebaltop(sign)) {
                    if (x > 30) {
                        break;
                    }
                    PreparedStatement statementa = connection.prepareStatement(
                            "select * from mpoints_" + suffix + sign + " where hidden != '1' order by balance desc limit 10");
                    ResultSet rsa = statementa.executeQuery();
                    List<UUID> uidlist = new ArrayList<>();
                    List<String> namelist = new ArrayList<>();
                    while (rsa.next()) {
                        uidlist.add(UUID.fromString(rsa.getString(1)));
                    }
                    Cache.baltop_uid.put(sign, uidlist);
                    rsa.close();
                    statementa.close();
                    for (UUID uu : uidlist) {
                        PreparedStatement statementb = connection.prepareStatement(
                                "select * from mpoints_" + suffix + dataname1 + " where UID = ?");
                        statementb.setString(1, uu.toString());
                        ResultSet rsb = statementb.executeQuery();
                        while (rsb.next()) {
                            namelist.add(rsb.getString(2));
                        }
                        Cache.baltop_name.put(sign, namelist);
                        rsb.close();
                        statementb.close();
                    }
                    PreparedStatement statementc = connection.prepareStatement("select SUM(balance) from mpoints_" + suffix + sign + " where hidden != '1'");
                    ResultSet rsc = statementc.executeQuery();
                    if (rsc.next()) {
                        String sumb = rsc.getString(1);
                        if (sumb != null) {
                            Cache.sumbalance.put(sign, DataFormat.formatString(sign, sumb));
                        } else {
                            Cache.sumbalance.put(sign, BigDecimal.ZERO);
                        }
                    } else {
                        Cache.sumbalance.put(sign, BigDecimal.ZERO);
                    }
                    rsc.close();
                    statementc.close();
                    x = x + 1;
                }
            }
            database.closeHikariConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void hidetop(UUID u, String sign, Integer type) {
        Connection connection = database.getConnectionAndCheck();
        try {
            String query = " set hidden = ? where UID = ?";
            PreparedStatement statement = connection.prepareStatement("update mpoints_" + suffix + sign + query);
            statement.setInt(1, type);
            statement.setString(2, u.toString());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeHikariConnection(connection);
    }

    public static void record(PlayerData x, Connection co) {
        if (MPoints.config.getBoolean("Settings.mysql") && MPoints.config.getBoolean("Settings.transaction-record")) {
            try {
                String query;
                query = "INSERT INTO mpoints_" + suffix + dataname2 + "(type,uid,player,sign,balance,amount,operation,date,command) values(?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = co.prepareStatement(query);
                statement.setString(1, x.gettype());
                statement.setString(2, x.getuid());
                statement.setString(3, x.getplayer());
                statement.setString(4, x.getsign());
                statement.setDouble(5, x.getnewbalance().doubleValue());
                statement.setDouble(6, x.getamount().doubleValue());
                statement.setString(7, x.getoperation());
                statement.setString(8, x.getdate());
                statement.setString(9, x.getcommand());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
