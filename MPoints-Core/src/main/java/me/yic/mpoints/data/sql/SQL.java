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
package me.yic.mpoints.data.sql;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.MPointsLoad;
import me.yic.mpoints.data.GetUUID;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.data.caches.PointsCache;
import me.yic.mpoints.data.syncdata.PlayerData;
import me.yic.mpoints.info.RecordInfo;
import me.yic.mpoints.utils.DatabaseConnection;
import me.yic.mpoints.utils.UUIDMode;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class SQL {

    public static String tableName = "mpointsplayerinfo";
    public static String tablePoint = "mpointspt";
    public static String tableRecordName = "mpointsrecord";
    public static String tableUUIDName = "mpointsuuid";
    public static String tableLoginName = "mpointslogin";
    public final static DatabaseConnection database = new DatabaseConnection();
    static final String encoding = MPointsLoad.DConfig.ENCODING;

    public static boolean con() {
        return database.setGlobalConnection();
    }

    public static void close() {
        database.close();
    }

    public static void getwaittimeout() {
        if (MPointsLoad.DConfig.isMySQL()) {
            try {
                Connection connection = database.getConnectionAndCheck();

                String query = "show variables like 'wait_timeout'";

                PreparedStatement statement = connection.prepareStatement(query);

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    int waittime = rs.getInt(2);
                    if (waittime > 50) {
                        database.waittimeout = waittime - 30;
                    }

                }

                rs.close();
                statement.close();
                database.closeHikariConnection(connection);

            } catch (SQLException ignored) {
                MPoints.getInstance().logger("Get 'wait_timeout' error", 1, null);
            }
        }

        database.setHikariValidationTimeout();

    }

    public static void createTable() {
        try {
            Connection connection = database.getConnectionAndCheck();
            Statement statement = connection.createStatement();

            if (statement == null) {
                return;
            }

            String query1;
            String query3 = "create table if not exists " + tableRecordName
                    + "(id int(20) not null auto_increment, type varchar(50) not null, uid varchar(50) not null, player varchar(50) not null,"
                    + "sign varchar(50) not null, balance double(20,2), amount double(20,2) not null, operation varchar(50) not null,"
                    + " command varchar(255) not null, comment varchar(255) not null, datetime datetime not null,"
                    + " primary key (id)) default charset = " + encoding + ";";
            //String query4 = "create table if not exists " + tableLoginName
            //        + "(UUID varchar(50) not null, last_time datetime not null, " + "primary key (UUID)) default charset = " + encoding + ";";

            String query5;
            query1 = "create table if not exists " + tableName
                    + " (UID varchar(50) not null, player varchar(50) not null, "
                    + "primary key (UID)) DEFAULT CHARSET = " + encoding + ";";
            query5 = "create table if not exists " + tableUUIDName
                    + "(UUID varchar(50) not null, DUUID varchar(50) not null, " +
                    "primary key (UUID)) default charset = " + encoding + ";";

            statement.executeUpdate(query1);

            if (MPointsLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                statement.executeUpdate(query5);
            }
            if (MPointsLoad.DConfig.isMySQL() && MPointsLoad.Config.TRANSACTION_RECORD) {
                statement.executeUpdate(query3);
            }

            String query2;
            for (String sign : PointsCache.getPointsList()) {
                query2 = "create table if not exists " + tablePoint + "_" + sign
                        + " (UID varchar(50) not null, balance decimal(30,2) not null, hidden int(5) not null, "
                        + "primary key (UID)) DEFAULT CHARSET = " + encoding + ";";

                statement.executeUpdate(query2);
            }
            statement.close();
            database.closeHikariConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PlayerData getPlayerData(UUID uuid, String psign) {
        PlayerData bd = null;
        try {
            Connection connection = database.getConnectionAndCheck();
            String sql = "select * from " + tableName + ", " + tablePoint + "_" + psign + " where " + tableName + ".UID = ?";
            if (MPointsLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                sql = "select * from " + tableName + ", "  + tablePoint + "_" + psign  + " where " + tableName + ".UID = ifnull((select DUUID from " + tableUUIDName + " where UUID = ?), ?)";
            }
            sql += " and " + tableName + ".UID = " + tablePoint + "_" + psign + ".UID";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid.toString());
            if (MPointsLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                statement.setString(2, uuid.toString());
            }

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                UUID fuuid = uuid;
                if (MPointsLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                    fuuid = UUID.fromString(rs.getString(1));
                    Cache.insertIntoMultiUUIDCache(fuuid, uuid);
                }
                BigDecimal cacheThisAmt = PointsCache.getDataFormat(psign).formatString(rs.getString(4));
                bd = new PlayerData(psign, fuuid, rs.getString(2), cacheThisAmt);
                Cache.insertIntoCache(fuuid, psign, bd);
            }

            rs.close();
            statement.close();
            database.closeHikariConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bd;
    }

    @SuppressWarnings("ConstantConditions")
    public static PlayerData getPlayerData(String name, String psign) {
        PlayerData bd = null;
        try {
            Connection connection = database.getConnectionAndCheck();
            String query;

            if (MPointsLoad.Config.USERNAME_IGNORE_CASE) {
                query = "select * from " + tableName + ", " + tablePoint + "_" + psign + " where " + tableName + ".UID = (select UID from " + tableName + " where player = ?)";
                if (!MPointsLoad.DConfig.isMySQL()) {
                    query += " COLLATE NOCASE";
                }
            } else {
                query = "select * from " + tableName + ", " + tablePoint + "_" + psign + " where " + tableName + ".UID = (select UID from " + tableName;
                if (MPointsLoad.DConfig.isMySQL()) {
                    query += " where binary player = ?)";
                }else{
                    query += " where player = ?)";
                }
            }
            query += " and " + tableName + ".UID = " + tablePoint + "_" + psign + ".UID";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString(1));
                UUID puuid = null;
                if (MPointsLoad.Config.UUIDMODE.equals(UUIDMode.OFFLINE) || MPointsLoad.Config.UUIDMODE.equals(UUIDMode.ONLINE)) {
                    puuid = GetUUID.getUUID(null, name);
                }
                if (MPointsLoad.Config.UUIDMODE.equals(UUIDMode.DEFAULT) || MPointsLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)
                        || (puuid != null && uuid.toString().equalsIgnoreCase(puuid.toString()))) {
                    String username = rs.getString(2);
                    BigDecimal cacheThisAmt = PointsCache.getDataFormat(psign).formatString(rs.getString(4));
                    if (cacheThisAmt != null) {
                        bd = new PlayerData(psign, uuid, username, cacheThisAmt);
                        Cache.insertIntoCache(uuid, psign, bd);
                    }
                    break;
                }
            }

            rs.close();
            statement.close();
            database.closeHikariConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bd;
    }


    public static void save(String psign, PlayerData pd, Boolean isAdd, BigDecimal amount, RecordInfo ri) {
        Connection connection = database.getConnectionAndCheck();
        try {
            String query = " set balance = ? where UID = ?";
//            if (MPointsLoad.Config.DISABLE_CACHE) {
            if (isAdd != null) {
                if (isAdd) {
                    query = " set balance = balance + ? where UID = ?";
                } else {
                    query = " set balance = balance - ? where UID = ?";
                }
            }
//            }
            PreparedStatement statement = connection.prepareStatement("update " + tablePoint + "_" + psign + query);
//            if (!MPointsLoad.Config.DISABLE_CACHE) {
//                statement.setDouble(1, pd.getBalance().doubleValue());
//            } else {
            statement.setDouble(1, amount.doubleValue());
//            }
            statement.setString(2, pd.getUniqueId().toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        record(connection, psign, pd, isAdd, amount, pd.getBalance(), ri);
        database.closeHikariConnection(connection);
    }

    public static void saveall(String psign, String targettype, List<UUID> players, BigDecimal amount, Boolean isAdd,
                               RecordInfo ri) {
        Connection connection = database.getConnectionAndCheck();
        try {
            if (targettype.equalsIgnoreCase("all")) {
                String query;
                if (isAdd) {
                    query = " set balance = balance + " + amount.doubleValue();
                } else {
                    query = " set balance = balance - " + amount.doubleValue();
                }
                PreparedStatement statement = connection.prepareStatement("update " + tablePoint + "_" + psign + query);
                statement.executeUpdate();
                statement.close();
            } else if (targettype.equalsIgnoreCase("online")) {
                StringBuilder query;
                if (isAdd) {
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
                PreparedStatement statement = connection.prepareStatement("update " + tablePoint + "_" + psign + query);
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ri != null) {
            record(connection, psign, null, isAdd, amount, BigDecimal.ZERO, ri);
        }
        database.closeHikariConnection(connection);
    }

    public static void deletePlayerData(String psign, String UUID) {
        Connection connection = database.getConnectionAndCheck();
        try {
            String query = "delete from " + tablePoint + "_" + psign + " where UID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, UUID);
            statement.executeUpdate();
            statement.close();
            if (MPointsLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                query = "delete from " + tablePoint + "_" + psign + " where DUUID = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, UUID);
                statement.executeUpdate();
                statement.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeHikariConnection(connection);
    }

    public static void getBaltop() {
        try {
            Connection connection = database.getConnectionAndCheck();
            for (String sign : PointsCache.getPointsList()) {
                if (PointsCache.getPoint(sign).ENABLE_BALTOP) {
                    PreparedStatement statementa = connection.prepareStatement(
                            "select * from " + tablePoint + "_" + sign + " where hidden != '1' order by balance desc limit 10");
                    ResultSet rsa = statementa.executeQuery();
                    LinkedHashMap<String, BigDecimal> lm = new LinkedHashMap<>();
                    List<String> ls = new ArrayList<>();
                    while (rsa.next()) {
                        UUID uu = UUID.fromString(rsa.getString(1));
                        BigDecimal bb = PointsCache.getDataFormat(sign).formatString(rsa.getString(2));

                        PreparedStatement statementb = connection.prepareStatement(
                                "select * from " + tableName + " where UID = ?");
                        statementb.setString(1, uu.toString());
                        ResultSet rsb = statementb.executeQuery();
                        if (rsb.next()) {
                            String name = rsb.getString(2);
                            lm.put(name, bb);
                            ls.add(name);
                        }
                        rsb.close();
                        statementb.close();
                    }
                    Cache.baltop.put(sign, lm);
                    Cache.baltop_papi.put(sign, ls);
                    rsa.close();
                    statementa.close();

                    PreparedStatement statementc = connection.prepareStatement("select SUM(balance) from " + tablePoint + "_" + sign + " where hidden != '1'");
                    ResultSet rsc = statementc.executeQuery();
                    if (rsc.next()) {
                        String sumb = rsc.getString(1);
                        if (sumb != null) {
                            Cache.sumbalance.put(sign, PointsCache.getDataFormat(sign).formatString(sumb));
                        } else {
                            Cache.sumbalance.put(sign, BigDecimal.ZERO);
                        }
                    } else {
                        Cache.sumbalance.put(sign, BigDecimal.ZERO);
                    }
                    rsc.close();
                    statementc.close();
                }
            }
            database.closeHikariConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String sumBal(String psign) {
        String bal = "0.0";
        try {
            Connection connection = database.getConnectionAndCheck();
            PreparedStatement statement = connection.prepareStatement("select SUM(balance) from " + tablePoint + "_" + psign + " where hidden != '1'");

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                bal = rs.getString(1);
            }

            rs.close();
            statement.close();
            database.closeHikariConnection(connection);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return bal;
    }

    public static void hidetop(String psign, UUID u, int type) {
        Connection connection = database.getConnectionAndCheck();
        try {
            String query = " set hidden = ? where UID = ?";
            PreparedStatement statement = connection.prepareStatement("update " + tablePoint + "_" + psign + query);
            statement.setInt(1, type);
            statement.setString(2, u.toString());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeHikariConnection(connection);
    }

    public static void record(Connection co, String psign, PlayerData pd, Boolean isAdd,
                              BigDecimal amount, BigDecimal newbalance, RecordInfo ri) {
        if (MPointsLoad.DConfig.isMySQL() && MPointsLoad.Config.TRANSACTION_RECORD) {
            String uid = "N/A";
            String name = "N/A";
            String operation;
            if (pd != null) {
                if (pd.getUniqueId() != null) {
                    uid = pd.getUniqueId().toString();
                }
                name = pd.getName();
            }
            if (isAdd != null) {
                if (isAdd) {
                    operation = "DEPOSIT";
                } else {
                    operation = "WITHDRAW";
                }
            } else {
                operation = "SET";
            }
            try {
                String query;
                Date dd = new Date();
                String sd = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dd);
                query = "INSERT INTO " + tableRecordName + "(type,uid,player,sign,balance,amount,operation,command,comment,datetime) values(?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = co.prepareStatement(query);
                statement.setString(1, ri.getType());
                statement.setString(2, uid);
                statement.setString(3, name);
                statement.setString(4, psign);
                statement.setDouble(5, newbalance.doubleValue());
                statement.setDouble(6, amount.doubleValue());
                statement.setString(7, operation);
                statement.setString(8, ri.getCommand());
                statement.setString(9, ri.getComment());
                statement.setString(10, sd);
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}